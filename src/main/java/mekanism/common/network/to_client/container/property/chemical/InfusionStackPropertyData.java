package mekanism.common.network.to_client.container.property.chemical;

import javax.annotation.Nonnull;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.common.network.to_client.container.property.PropertyType;

public class InfusionStackPropertyData extends ChemicalStackPropertyData<InfusionStack> {

    public InfusionStackPropertyData(short property, @Nonnull InfusionStack value) {
        super(PropertyType.INFUSION_STACK, property, value);
    }
}