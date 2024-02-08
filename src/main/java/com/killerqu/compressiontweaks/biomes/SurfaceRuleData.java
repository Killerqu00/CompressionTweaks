package com.killerqu.compressiontweaks.biomes;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;

public class SurfaceRuleData {
    private static final SurfaceRules.RuleSource NETHERRACK = makeStateRule(Blocks.NETHERRACK);
    private static final SurfaceRules.RuleSource ENDSTONE = makeStateRule(Blocks.END_STONE);
    private static final SurfaceRules.RuleSource SOULSOIL = makeStateRule(Blocks.SOUL_SOIL);
    private static final SurfaceRules.RuleSource BASALT = makeStateRule(Blocks.BASALT);
    private static final SurfaceRules.RuleSource BEDROCK = makeStateRule(Blocks.BEDROCK);

    public static SurfaceRules.RuleSource makeRules() {
        return SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.verticalGradient("compressiontweaks:bedrock_floor", new VerticalAnchor.AboveBottom(0), new VerticalAnchor.AboveBottom(5)), BEDROCK),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.VALLEY_BIOME), SOULSOIL),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.CRIMSON_BIOME), NETHERRACK),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WARPED_BIOME), NETHERRACK),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.DELTAS_BIOME), BASALT),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.END_BIOME), ENDSTONE)
        );
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}
