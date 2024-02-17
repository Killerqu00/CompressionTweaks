package com.killerqu.compressiontweaks.biomes;

import com.killerqu.compressiontweaks.CompressionTweaks;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class Biomes {
    public static final ResourceKey<Biome> END_BIOME = register("end");
    public static final ResourceKey<Biome> VALLEY_BIOME = register("soulsand_valley");
    public static final ResourceKey<Biome> DELTAS_BIOME = register("basalt_deltas");
    public static final ResourceKey<Biome> CRIMSON_BIOME = register("crimson_forest");
    public static final ResourceKey<Biome> WARPED_BIOME = register("warped_forest");
    public static final ResourceKey<Biome> VOID_BIOME = register("void_biome");

    private static ResourceKey<Biome> register(String name) {
        return ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(CompressionTweaks.MODID, name));
    }
}
