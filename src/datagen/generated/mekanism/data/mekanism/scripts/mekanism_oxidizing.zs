//Adds an Oxidizing Recipe that allows converting Salt Blocks into 60 mB of Gaseous Brine.

// <recipetype:mekanism:oxidizing>.addRecipe(arg0 as string, arg1 as ItemStackIngredient, arg2 as ICrTGasStack)

<recipetype:mekanism:oxidizing>.addRecipe("oxidize_salt_block", mekanism.api.ingredient.ItemStackIngredient.from(<item:mekanism:block_salt>), <gas:mekanism:brine> * 60);
//An alternate implementation of the above recipe are shown commented below. This implementation makes use of implicit casting to allow easier calling:
// <recipetype:mekanism:oxidizing>.addRecipe("oxidize_salt_block", <item:mekanism:block_salt>, <gas:mekanism:brine> * 60);


//Removes the Oxidizing Recipe that allows Sulfur Dioxide from Sulfur Dust.

// <recipetype:mekanism:oxidizing>.removeByName(name as string)

<recipetype:mekanism:oxidizing>.removeByName("mekanism:oxidizing/sulfur_dioxide");