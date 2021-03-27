package mekanism.tasks

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet

class MergeJars {

    static List<String> getGeneralPathsToExclude(Project project) {
        List<String> toExclude = new ArrayList<>()
        toExclude.add('META-INF/mods.toml')
        toExclude.add('META-INF/accesstransformer.cfg')
        int baseLength = "$project.buildDir/generated/".length()
        project.fileTree(dir: "$project.buildDir/generated/data/").each {
            File file -> toExclude.add(file.getPath().substring(baseLength))
        }
        return toExclude
    }

    static Closure createExcludeClosure(List<String> baseExcludeData, String... extraExclusions) {
        List<String> toExcludeFromAll = new ArrayList<>(baseExcludeData)
        for (String extraExclusion : extraExclusions) {
            toExcludeFromAll.add(extraExclusion)
        }
        return {
            exclude(toExcludeFromAll)
        }
    }

    static Closure merge(Project project, SourceSet... sourceSets) {
        return {
            //Generate folders, merge the access transformers and mods.toml files
            project.mkdir("$project.buildDir/generated/META-INF")
            (new File("$project.buildDir/generated/META-INF/accesstransformer.cfg")).text = mergeATs(sourceSets)
            (new File("$project.buildDir/generated/META-INF/mods.toml")).text = mergeModsTOML(sourceSets)
            //Delete the data directory so that we don't accidentally leak bad old data into it
            project.file("$project.buildDir/generated/data").deleteDir()
            //And then recreate the directory so we can put stuff in it
            project.mkdir("$project.buildDir/generated/data")
            mergeTags(project, sourceSets)
        }
    }

    static List<Closure> getGeneratedClosures(def version_properties) {
        List<Closure> generated = new ArrayList<>()
        generated.add({
            include 'META-INF/mods.toml'
            expand version_properties
        })
        generated.add({
            include 'META-INF/accesstransformer.cfg'
            include 'data/**'
        })
        return generated
    }

    private static String mergeATs(SourceSet... sourceSets) {
        String text = ""
        for (SourceSet sourceSet : sourceSets) {
            sourceSet.resources.matching {
                include 'META-INF/accesstransformer.cfg'
            }.each {
                file -> text = text.isEmpty() ? file.getText() : text + "\n" + file.getText()
            }
        }
        return text
    }

    private static String mergeModsTOML(SourceSet... sourceSets) {
        String text = ""
        for (SourceSet sourceSet : sourceSets) {
            sourceSet.resources.matching {
                include 'META-INF/mods.toml'
            }.each { file ->
                if (text.isEmpty()) {
                    //Nothing added yet, take it all
                    text = file.getText()
                } else {
                    //Otherwise add all but the first four lines (which are duplicated between the files)
                    String[] lines = file.getText().split("\n")
                    for (int i = 4; i < lines.length; i++) {
                        text = text + "\n" + lines[i]
                    }
                }
            }
        }
        return text
    }

    private static void mergeTags(Project project, SourceSet... sourceSets) {
        Closure tagFilter = { include '**/data/*/tags/**/*.json' }
        Map<String, List<String>> reverseTags = new HashMap<>()
        for (SourceSet sourceSet : sourceSets) {
            sourceSet.resources.srcDirs.each { srcDir ->
                int srcDirPathLength = srcDir.getPath().length()
                project.fileTree(srcDir).matching(tagFilter).each { file ->
                    //Add the sourceSet to the reverse lookup
                    String path = file.getPath()
                    String tag = path.substring(srcDirPathLength)
                    if (!reverseTags.containsKey(tag)) {
                        reverseTags.put(tag, new ArrayList<>())
                    }
                    reverseTags.get(tag).add(path)
                }
            }
        }
        //Go through the reverse tag index and if there are multiple sourceSets that contain the same tag
        // properly merge that tag
        reverseTags.each { tag, tagPaths ->
            if (tagPaths.size() > 1) {
                mergeTag(project, tag, tagPaths)
            }
        }
    }

    private static void mergeTag(Project project, String tag, List<String> tagPaths) {
        //println(tag + " appeared " + tagPaths.size() + " times")
        Object outputTagAsJson = null
        tagPaths.each {path ->
            Object tagAsJson = new JsonSlurper().parse(project.file(path))
            if (outputTagAsJson == null) {
                outputTagAsJson = tagAsJson
            } else {
                outputTagAsJson.values += tagAsJson.values
            }
        }
        if (outputTagAsJson != null) {
            File outputFile = new File("$project.buildDir/generated" + tag)
            //Make all parent directories needed
            outputFile.getParentFile().mkdirs()
            outputFile.text = JsonOutput.toJson(outputTagAsJson)
        }
    }
}