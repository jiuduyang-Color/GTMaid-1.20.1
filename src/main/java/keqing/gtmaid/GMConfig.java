package keqing.gtmaid;

import net.minecraftforge.common.ForgeConfigSpec;

public class GMConfig {
    public static final ForgeConfigSpec SPEC;

    private static final ForgeConfigSpec.IntValue RUN_DELAY_ORE;
    private static final ForgeConfigSpec.IntValue RUN_DELAY_TREE;
    private static final ForgeConfigSpec.IntValue RUN_DELAY_BROKE;
    private static final ForgeConfigSpec.BooleanValue CHAIN_SWITCH;
    private static final ForgeConfigSpec.IntValue CHAIN_RANGE;
    private static final ForgeConfigSpec.BooleanValue ORE_MODE;

    public static int runDelayOre = 40;
    public static int runDelayTree = 40;
    public static int runDelayBroke = 40;
    public static boolean chainSwitch = true;
    public static int chainRange = 1;
    public static boolean oreMode = true;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("GTMaid settings").push("general");

        RUN_DELAY_ORE = builder
                .comment("Ore mining task check interval (ticks)")
                .defineInRange("runDelayOre", 40, 1, 72000);
        RUN_DELAY_TREE = builder
                .comment("Tree chopping task check interval (ticks)")
                .defineInRange("runDelayTree", 40, 1, 72000);
        RUN_DELAY_BROKE = builder
                .comment("Digging (dirt / sand / gravel / clay) task check interval (ticks)")
                .defineInRange("runDelayBroke", 40, 1, 72000);
        CHAIN_SWITCH = builder
                .comment("Enable chain mining / chain chopping")
                .define("chainSwitch", true);
        CHAIN_RANGE = builder
                .comment("Chain mining radius")
                .defineInRange("chainRange", 1, 0, 8);
        ORE_MODE = builder
                .comment("Legacy ore-dictionary mode flag, kept for compatibility")
                .define("oreMode", true);

        builder.pop();
        SPEC = builder.build();
    }

    public static void bake() {
        runDelayOre = RUN_DELAY_ORE.get();
        runDelayTree = RUN_DELAY_TREE.get();
        runDelayBroke = RUN_DELAY_BROKE.get();
        chainSwitch = CHAIN_SWITCH.get();
        chainRange = CHAIN_RANGE.get();
        oreMode = ORE_MODE.get();
    }
}
