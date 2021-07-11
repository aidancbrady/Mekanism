package mekanism.generators.client;

import mekanism.client.lang.BaseLanguageProvider;
import mekanism.generators.common.GeneratorsLang;
import mekanism.generators.common.MekanismGenerators;
import mekanism.generators.common.registries.GeneratorsBlocks;
import mekanism.generators.common.registries.GeneratorsFluids;
import mekanism.generators.common.registries.GeneratorsGases;
import mekanism.generators.common.registries.GeneratorsItems;
import mekanism.generators.common.registries.GeneratorsSounds;
import net.minecraft.data.DataGenerator;

public class GeneratorsLangProvider extends BaseLanguageProvider {

    public GeneratorsLangProvider(DataGenerator gen) {
        super(gen, MekanismGenerators.MODID);
    }

    @Override
    protected void addTranslations() {
        addItems();
        addBlocks();
        addFluids();
        addGases();
        addSubtitles();
        addMisc();
    }

    private void addItems() {
        add(GeneratorsItems.SOLAR_PANEL, "Solar Panel");
        add(GeneratorsItems.HOHLRAUM, "Hohlraum");
        add(GeneratorsItems.TURBINE_BLADE, "Turbine Blade");
    }

    private void addBlocks() {
        add(GeneratorsBlocks.ADVANCED_SOLAR_GENERATOR, "Advanced Solar Generator");
        add(GeneratorsBlocks.BIO_GENERATOR, "Bio-Generator");
        add(GeneratorsBlocks.ELECTROMAGNETIC_COIL, "Electromagnetic Coil");
        add(GeneratorsBlocks.GAS_BURNING_GENERATOR, "Gas-Burning Generator");
        add(GeneratorsBlocks.HEAT_GENERATOR, "Heat Generator");
        add(GeneratorsBlocks.SOLAR_GENERATOR, "Solar Generator");
        add(GeneratorsBlocks.WIND_GENERATOR, "Wind Generator");
        add(GeneratorsBlocks.REACTOR_GLASS, "Reactor Glass");
        add(GeneratorsBlocks.FISSION_REACTOR_CASING, "Fission Reactor Casing");
        add(GeneratorsBlocks.FISSION_REACTOR_PORT, "Fission Reactor Port");
        add(GeneratorsBlocks.FISSION_REACTOR_LOGIC_ADAPTER, "Fission Reactor Logic Adapter");
        add(GeneratorsBlocks.FISSION_FUEL_ASSEMBLY, "Fission Fuel Assembly");
        add(GeneratorsBlocks.CONTROL_ROD_ASSEMBLY, "Control Rod Assembly");
        add(GeneratorsBlocks.LASER_FOCUS_MATRIX, "Laser Focus Matrix");
        add(GeneratorsBlocks.FUSION_REACTOR_CONTROLLER, "Fusion Reactor Controller");
        add(GeneratorsBlocks.FUSION_REACTOR_FRAME, "Fusion Reactor Frame");
        add(GeneratorsBlocks.FUSION_REACTOR_LOGIC_ADAPTER, "Fusion Reactor Logic Adapter");
        add(GeneratorsBlocks.FUSION_REACTOR_PORT, "Fusion Reactor Port");
        add(GeneratorsBlocks.ROTATIONAL_COMPLEX, "Rotational Complex");
        add(GeneratorsBlocks.SATURATING_CONDENSER, "Saturating Condenser");
        add(GeneratorsBlocks.TURBINE_CASING, "Turbine Casing");
        add(GeneratorsBlocks.TURBINE_ROTOR, "Turbine Rotor");
        add(GeneratorsBlocks.TURBINE_VALVE, "Turbine Valve");
        add(GeneratorsBlocks.TURBINE_VENT, "Turbine Vent");
    }

    private void addFluids() {
        addFluid(GeneratorsFluids.BIOETHANOL, "Bioethanol");
        addFluid(GeneratorsFluids.DEUTERIUM, "Liquid Deuterium");
        addFluid(GeneratorsFluids.FUSION_FUEL, "Liquid D-T Fuel");
        addFluid(GeneratorsFluids.TRITIUM, "Liquid Tritium");
    }

    private void addGases() {
        add(GeneratorsGases.DEUTERIUM, "Deuterium");
        add(GeneratorsGases.FUSION_FUEL, "D-T Fuel");
        add(GeneratorsGases.TRITIUM, "Tritium");
    }

    private void addSubtitles() {
        add(GeneratorsSounds.BIO_GENERATOR, "Bio generator hums");
        add(GeneratorsSounds.GAS_BURNING_GENERATOR, "Gas generator hums");
        add(GeneratorsSounds.FISSION_REACTOR, "Fission reactor hums");
        add(GeneratorsSounds.FUSION_REACTOR, "Fusion reactor hums");
        add(GeneratorsSounds.HEAT_GENERATOR, "Heat generator hums");
        add(GeneratorsSounds.SOLAR_GENERATOR, "Solar generator hums");
        add(GeneratorsSounds.WIND_GENERATOR, "Wind generator wooshes");
    }

    private void addMisc() {
        add(GeneratorsLang.REACTOR_LOGIC_ACTIVE_COOLING, "Active cooling: %s");
        add(GeneratorsLang.GAS_BURN_RATE, "Burn Rate: %s mB/t");
        add(GeneratorsLang.STATS_TAB, "Stats");
        add(GeneratorsLang.FUEL_TAB, "Fuel");
        add(GeneratorsLang.HEAT_TAB, "Heat");
        add(GeneratorsLang.INSUFFICIENT_FUEL, "Insufficient Fuel");
        add(GeneratorsLang.IS_LIMITING, "(Limiting)");
        add(GeneratorsLang.TURBINE_MAX_WATER_OUTPUT, "Max Water Output: %s mB/t");
        add(GeneratorsLang.NO_WIND, "No wind");
        add(GeneratorsLang.REACTOR_LOGIC_OUTPUTTING, "Outputting");
        add(GeneratorsLang.REACTOR_LOGIC_ACTIVATION, "Activation");
        add(GeneratorsLang.REACTOR_LOGIC_TEMPERATURE, "High Temperature");
        add(GeneratorsLang.REACTOR_LOGIC_EXCESS_WASTE, "Excess Waste");
        add(GeneratorsLang.REACTOR_LOGIC_DAMAGED, "Damage Critical");
        add(GeneratorsLang.REACTOR_LOGIC_POWERED, "Powered");
        add(GeneratorsLang.OUTPUT_RATE_SHORT, "Out: %s/t");
        add(GeneratorsLang.POWER, "Power: %s");
        add(GeneratorsLang.PRODUCING_AMOUNT, "Producing: %s/t");
        add(GeneratorsLang.TURBINE_PRODUCTION, "Production");
        add(GeneratorsLang.TURBINE_PRODUCTION_AMOUNT, "Production: %s");
        add(GeneratorsLang.REACTOR_ACTIVE, "Water-Cooled");
        add(GeneratorsLang.REACTOR_LOGIC_CAPACITY, "Heat Capacity Met");
        add(GeneratorsLang.REACTOR_CASE, "Case: %s");
        add(GeneratorsLang.REACTOR_LOGIC_DEPLETED, "Insufficient Fuel");
        add(GeneratorsLang.REACTOR_LOGIC_DISABLED, "Disabled");
        add(GeneratorsLang.REACTOR_EDIT_RATE, "Edit Rate:");
        add(GeneratorsLang.REACTOR_IGNITION, "Ignition Temp: %s");
        add(GeneratorsLang.REACTOR_INJECTION_RATE, "Injection Rate: %s");
        add(GeneratorsLang.REACTOR_MAX_CASING, "Max. Casing Temp: %s");
        add(GeneratorsLang.REACTOR_MAX_PLASMA, "Max. Plasma Temp: %s");
        add(GeneratorsLang.REACTOR_MIN_INJECTION, "Min. Inject Rate: %s");
        add(GeneratorsLang.FUSION_REACTOR, "Fusion Reactor");
        add(GeneratorsLang.REACTOR_PASSIVE, "Air-Cooled");
        add(GeneratorsLang.REACTOR_PASSIVE_RATE, "Passive Generation: %s/t");
        add(GeneratorsLang.REACTOR_PLASMA, "Plasma: %s");
        add(GeneratorsLang.REACTOR_PORT_EJECT, "Toggled Reactor Port eject mode to: %s.");
        add(GeneratorsLang.REACTOR_LOGIC_READY, "Ready for Ignition");
        add(GeneratorsLang.REACTOR_STEAM_PRODUCTION, "Steam Production: %s mB/t");
        add(GeneratorsLang.READY_FOR_REACTION, "Ready for Reaction!");
        add(GeneratorsLang.REACTOR_LOGIC_REDSTONE_MODE, "Redstone mode: %s");
        add(GeneratorsLang.SKY_BLOCKED, "Sky blocked");
        add(GeneratorsLang.TURBINE_STEAM_FLOW, "Steam Flow");
        add(GeneratorsLang.TURBINE_STEAM_INPUT_RATE, "Steam Input: %s mB/t");
        add(GeneratorsLang.STORED_BIO_FUEL, "BioFuel: %s");
        add(GeneratorsLang.TURBINE_TANK_VOLUME, "Tank Volume: %s");
        add(GeneratorsLang.REACTOR_LOGIC_TOGGLE_COOLING, "Toggle Cooling Measurements");

        //Industrial Turbine
        add(GeneratorsLang.TURBINE_INVALID_BAD_COMPLEX, "Couldn't form, found improperly placed Rotational Complex at %s. Complex must be centered above Turbine Rotors.");
        add(GeneratorsLang.TURBINE_INVALID_BAD_ROTOR, "Couldn't form, found invalid Turbine Rotor at %s. Turbine Rotors must be centered below Rotational Complex.");
        add(GeneratorsLang.TURBINE_INVALID_BAD_ROTORS, "Couldn't form, invalid Turbine Rotor arrangement.");
        add(GeneratorsLang.TURBINE_INVALID_CONDENSER_BELOW_COMPLEX, "Couldn't form, found improperly placed Saturating Condenser at %s. Saturating Condensers must be above Pressure Disperser layer.");
        add(GeneratorsLang.TURBINE_INVALID_EVEN_LENGTH, "Couldn't form, width and length of structure must be odd.");
        add(GeneratorsLang.TURBINE_INVALID_MALFORMED_COILS, "Couldn't form, Electromagnetic Coil arrangement is malformed. Coils must be connected to Rotational Complex and adjacently connected.");
        add(GeneratorsLang.TURBINE_INVALID_MALFORMED_DISPERSERS, "Couldn't form, Pressure Disperser arrangement is malformed. Dispersers must create complete horizontal layer surrounding Rotational Complex.");
        add(GeneratorsLang.TURBINE_INVALID_MISSING_COMPLEX, "Couldn't form, no Rotational Complex present.");
        add(GeneratorsLang.TURBINE_INVALID_MISSING_DISPERSER, "Couldn't form, expected but didn't find Pressure Disperser at %s.");
        add(GeneratorsLang.TURBINE_INVALID_ROTORS_NOT_CONTIGUOUS, "Couldn't form, rotors are invalid (non-contiguous).");
        add(GeneratorsLang.TURBINE_INVALID_TOO_NARROW, "Couldn't form, structure is too narrow to support turbine size.");
        add(GeneratorsLang.TURBINE_INVALID_VENT_BELOW_COMPLEX, "Couldn't form, found a Turbine Vent below Pressure Disperser layer. Vents must be at or above vertical position of disperser layer.");
        add(GeneratorsLang.TURBINE_INVALID_MISSING_COILS, "Couldn't form, no Electromagnetic Coils present.");

        add(GeneratorsLang.TURBINE, "Industrial Turbine");
        add(GeneratorsLang.TURBINE_BLADES, "Blades: %s %s");
        add(GeneratorsLang.TURBINE_CAPACITY, "Capacity: %s mB");
        add(GeneratorsLang.TURBINE_COILS, "Coils: %s %s");
        add(GeneratorsLang.TURBINE_DISPERSERS, "Dispersers: %s %s");
        add(GeneratorsLang.TURBINE_FLOW_RATE, "Flow rate: %s mB/t");
        add(GeneratorsLang.TURBINE_MAX_FLOW, "Max flow: %s mB/t");
        add(GeneratorsLang.TURBINE_MAX_PRODUCTION, "Max Production: %s");
        add(GeneratorsLang.TURBINE_STATS, "Turbine Statistics");
        add(GeneratorsLang.TURBINE_VENTS, "Vents: %s %s");
        //Fission Reactor
        add(GeneratorsLang.FISSION_INVALID_BAD_CONTROL_ROD, "Couldn't form, improper placement for Control Rod Assembly at %s.");
        add(GeneratorsLang.FISSION_INVALID_MISSING_CONTROL_ROD, "Couldn't form, missing control rod for fuel assembly at %s.");
        add(GeneratorsLang.FISSION_INVALID_BAD_FUEL_ASSEMBLY, "Couldn't form, missing fuel assembly for control rod at %s.");
        add(GeneratorsLang.FISSION_INVALID_EXTRA_CONTROL_ROD, "Couldn't form, found extra Control Rod Assembly at %s.");
        add(GeneratorsLang.FISSION_INVALID_MALFORMED_FUEL_ASSEMBLY, "Couldn't form, invalid Fission Fuel Assembly placement at %s.");
        add(GeneratorsLang.FISSION_INVALID_MISSING_FUEL_ASSEMBLY, "Couldn't form, no fuel assembly structures present.");

        add(GeneratorsLang.FISSION_REACTOR, "Fission Reactor");
        add(GeneratorsLang.FISSION_REACTOR_STATS, "Fission Reactor Statistics");
        add(GeneratorsLang.FISSION_ACTIVATE, "Activate");
        add(GeneratorsLang.FISSION_SCRAM, "SCRAM");
        add(GeneratorsLang.FISSION_DAMAGE, "Damage: %s");
        add(GeneratorsLang.FISSION_HEAT_STATISTICS, "Heat Statistics");
        add(GeneratorsLang.FISSION_FUEL_STATISTICS, "Fuel Statistics");
        add(GeneratorsLang.FISSION_HEAT_CAPACITY, "Heat Capacity: %s J/K");
        add(GeneratorsLang.FISSION_SURFACE_AREA, "Fuel Surface Area: %s m2");
        add(GeneratorsLang.FISSION_BOIL_EFFICIENCY, "Boil Efficiency: %s");
        add(GeneratorsLang.FISSION_MAX_BURN_RATE, "Max Burn Rate: %s mB/t");
        add(GeneratorsLang.FISSION_RATE_LIMIT, "Rate Limit: %s mB/t");
        add(GeneratorsLang.FISSION_CURRENT_BURN_RATE, "Current Burn Rate:");
        add(GeneratorsLang.FISSION_HEATING_RATE, "Heating Rate: %s mB/t");
        add(GeneratorsLang.FISSION_SET_RATE_LIMIT, "Set Rate Limit:");
        add(GeneratorsLang.FISSION_COOLANT_TANK, "Coolant Tank");
        add(GeneratorsLang.FISSION_FUEL_TANK, "Fuel Tank");
        add(GeneratorsLang.FISSION_HEATED_COOLANT_TANK, "Heated Coolant Tank");
        add(GeneratorsLang.FISSION_WASTE_TANK, "Waste Tank");
        add(GeneratorsLang.FISSION_HEAT_GRAPH, "Heat Graph:");
        add(GeneratorsLang.FISSION_PORT_MODE_CHANGE, "Port mode changed to: %s");
        add(GeneratorsLang.FISSION_PORT_MODE_INPUT, "input only");
        add(GeneratorsLang.FISSION_PORT_MODE_OUTPUT_WASTE, "output waste");
        add(GeneratorsLang.FISSION_PORT_MODE_OUTPUT_COOLANT, "output coolant");
        //Descriptions
        add(GeneratorsLang.DESCRIPTION_REACTOR_CAPACITY, "Output when the reactor's core heat capacity has been met");
        add(GeneratorsLang.DESCRIPTION_REACTOR_ACTIVATION, "Activate the reactor when powered, and deactivate when unpowered");
        add(GeneratorsLang.DESCRIPTION_REACTOR_TEMPERATURE, "Output when the reactor reaches dangerous temperatures");
        add(GeneratorsLang.DESCRIPTION_REACTOR_DAMAGED, "Output when the reactor reaches critical damage levels (100%+).");
        add(GeneratorsLang.DESCRIPTION_REACTOR_EXCESS_WASTE, "Output when the reactor has excess waste");
        add(GeneratorsLang.DESCRIPTION_REACTOR_DEPLETED, "Output when the reactor has insufficient fuel to sustain a reaction");
        add(GeneratorsLang.DESCRIPTION_REACTOR_DISABLED, "Will not handle redstone");
        add(GeneratorsLang.DESCRIPTION_REACTOR_READY, "Output when the reactor has reached the required heat level to ignite");
        //Generators
        add(GeneratorsLang.DESCRIPTION_ADVANCED_SOLAR_GENERATOR, "An advanced generator that directly absorbs the sun's rays with little loss to produce energy.");
        add(GeneratorsLang.DESCRIPTION_BIO_GENERATOR, "A generator that burns organic materials of the world to produce energy.");
        add(GeneratorsLang.DESCRIPTION_ELECTROMAGNETIC_COIL, "A block that converts kinetic energy from a Rotational Complex into usable electricity. These can be placed in any arrangement above your Rotational Complex, as long as they are all touching each other and the complex itself.");
        add(GeneratorsLang.DESCRIPTION_GAS_BURNING_GENERATOR, "A generator that harnesses the varying molecular gases to produce energy.");
        add(GeneratorsLang.DESCRIPTION_HEAT_GENERATOR, "A generator that uses the heat of lava or other burnable resources to produce energy.");
        add(GeneratorsLang.DESCRIPTION_SOLAR_GENERATOR, "A generator that uses the power of the sun to produce energy.");
        add(GeneratorsLang.DESCRIPTION_WIND_GENERATOR, "A generator that uses the strength of the wind to produce energy, with greater efficiency at higher levels.");
        //Fission Reactor
        add(GeneratorsLang.DESCRIPTION_FISSION_REACTOR_CASING, "Lead-infused steel casing used to create a Fission Reactor. Mostly heat-resistant, mostly radiation-resistant, and mostly safe!");
        add(GeneratorsLang.DESCRIPTION_FISSION_REACTOR_PORT, "A port which can be placed on a Fission Reactor multiblock to transfer coolant, fuel, and waste.");
        add(GeneratorsLang.DESCRIPTION_FISSION_REACTOR_LOGIC_ADAPTER, "A block that can be used to monitor or control the Fission Reactor with redstone.");
        add(GeneratorsLang.DESCRIPTION_FISSION_FUEL_ASSEMBLY, "A cluster of fuel rods used to house fission fuel within a Fission Reactor. These can be stacked on top of each other.");
        add(GeneratorsLang.DESCRIPTION_CONTROL_ROD_ASSEMBLY, "A collection of control rods used to halt a fission chain reaction. Placed on top of a tower of Fission Fuel Assemblies.");
        //Fusion Reactor
        add(GeneratorsLang.DESCRIPTION_LASER_FOCUS_MATRIX, "A panel of Fusion Reactor Glass that is capable of absorbing optical energy and thereby heating up the Fusion Reactor.");
        add(GeneratorsLang.DESCRIPTION_FUSION_REACTOR_CONTROLLER, "The controlling block for the entire Fusion Reactor structure.");
        add(GeneratorsLang.DESCRIPTION_FUSION_REACTOR_FRAME, "Reinforced framing that can be used in the Fusion Reactor multiblock.");
        add(GeneratorsLang.DESCRIPTION_REACTOR_GLASS, "Reinforced glass that can be used in the Fission Reactor and Fusion Reactor multiblocks (as well as any others!).");
        add(GeneratorsLang.DESCRIPTION_FUSION_REACTOR_LOGIC_ADAPTER, "A block that can be used to allow basic monitoring of a reactor using redstone.");
        add(GeneratorsLang.DESCRIPTION_FUSION_REACTOR_PORT, "A block of reinforced framing that is capable of managing both the gas and energy transfer of the Fusion Reactor.");
        //Turbine
        add(GeneratorsLang.DESCRIPTION_ROTATIONAL_COMPLEX, "A connector that is placed on the highest Turbine Rotor of an Industrial Turbine to carry kinetic energy into its Electromagnetic Coils.");
        add(GeneratorsLang.DESCRIPTION_SATURATING_CONDENSER, "A block that condenses steam processed by an Industrial Turbine into reusable water. These can be placed in any arrangement above your rotational complex.");
        add(GeneratorsLang.DESCRIPTION_TURBINE_CASING, "Pressure-resistant casing used in the creation of an Industrial Turbine.");
        add(GeneratorsLang.DESCRIPTION_TURBINE_ROTOR, "The steel rod that is used to house Turbine Blades within an Industrial Turbine.");
        add(GeneratorsLang.DESCRIPTION_TURBINE_VALVE, "A type of Turbine Casing that includes a port for the transfer of energy and steam.");
        add(GeneratorsLang.DESCRIPTION_TURBINE_VENT, "A type of Turbine Casing with an integrated vent for the release of steam. These should be placed on the level of or above the turbine's Rotational Complex.");
    }
}