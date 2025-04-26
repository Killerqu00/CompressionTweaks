package com.killerqu.compressiontweaks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CTCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> BOULDER_COMPASS;

    static {
        BUILDER.push("CompressionTweaks Config");
        BOULDER_COMPASS = BUILDER.comment("Whether to enable the AE2 meteor compass to also track tagged blocks spawned in jigsaw structures. Note that disabling this option down the line cannot automatically cleanup existing targets.")
                .define("Enable Boulder Compass", true);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
