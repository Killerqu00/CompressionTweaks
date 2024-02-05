package com.killerqu.compressiontweaks.biomes;

import com.killerqu.compressiontweaks.CompressionTweaks;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class CTRegion extends Region {
    public static final ResourceLocation LOCATION = new ResourceLocation(CompressionTweaks.MODID, "overworld_main");
    public CTRegion(int weight) {
        super(LOCATION, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper)
    {
        this.addModifiedVanillaOverworldBiomes(mapper, builder -> {
            builder.replaceBiome(Biomes.DESERT, com.killerqu.compressiontweaks.biomes.Biomes.VALLEY_BIOME);
            builder.replaceBiome(Biomes.PLAINS, com.killerqu.compressiontweaks.biomes.Biomes.END_BIOME);
            builder.replaceBiome(Biomes.FOREST, com.killerqu.compressiontweaks.biomes.Biomes.DELTAS_BIOME);
            builder.replaceBiome(Biomes.TAIGA, com.killerqu.compressiontweaks.biomes.Biomes.CRIMSON_BIOME);
            builder.replaceBiome(Biomes.JUNGLE, com.killerqu.compressiontweaks.biomes.Biomes.WARPED_BIOME);
        });
    }
}
