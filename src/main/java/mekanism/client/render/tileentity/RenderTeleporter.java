package mekanism.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.ParametersAreNonnullByDefault;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.render.MekanismRenderer.Model3D;
import mekanism.common.base.ProfilerConstants;
import mekanism.common.registries.MekanismGases;
import mekanism.common.tile.TileEntityTeleporter;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.profiler.IProfiler;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
public class RenderTeleporter extends MekanismTileEntityRenderer<TileEntityTeleporter> {

    private static final Map<AxisAlignedBB, Model3D> cache = new HashMap<>();

    public static void resetCachedModels() {
        cache.clear();
    }

    public RenderTeleporter(TileEntityRendererDispatcher renderer) {
        super(renderer);
    }

    @Override
    protected void render(
            TileEntityTeleporter tile, float partialTick, MatrixStack matrix, IRenderTypeBuffer renderer, int light,
            int overlayLight, IProfiler profiler) {
        if (tile.shouldRender && tile.getWorld() != null) {
            MekanismRenderer.renderObject(
                    getOverlayModel(tile.getRenderBoundingBox(), tile.getTilePos()),
                    matrix,
                    renderer.getBuffer(Atlases.getTranslucentCullBlockType()),
                    MekanismRenderer.getColorARGB(tile.getColor(), 0.75F),
                    MekanismRenderer.FULL_LIGHT, overlayLight);
        }
    }

    @Override
    protected String getProfilerSection() {
        return ProfilerConstants.TELEPORTER;
    }

    private Model3D getOverlayModel(AxisAlignedBB boundingBox, BlockPos tilePosition) {
        AxisAlignedBB offsetBoundingBox = getOffsetBoundingBox(boundingBox, tilePosition);
        if (!cache.containsKey(offsetBoundingBox)) {
            Model3D model = new Model3D();
            model.setTexture(MekanismRenderer.getChemicalTexture(MekanismGases.HYDROGEN.getChemical()));
            cache.put(offsetBoundingBox, model);
            model.minX = offsetBoundingBox.minX;
            model.maxX = offsetBoundingBox.maxX;
            model.minY = offsetBoundingBox.minY;
            model.maxY = offsetBoundingBox.maxY;
            model.minZ = offsetBoundingBox.minZ;
            model.maxZ = offsetBoundingBox.maxZ;
        }
        return cache.get(offsetBoundingBox);
    }

    @NotNull
    private AxisAlignedBB getOffsetBoundingBox(AxisAlignedBB boundingBox, BlockPos tilePosition) {
        return boundingBox.offset(-tilePosition.getX(), -tilePosition.getY(), -tilePosition.getZ());
    }

    @Override
    public boolean isGlobalRenderer(TileEntityTeleporter tile) {
        return tile.shouldRender && tile.getWorld() != null;
    }
}