package com.killerqu.compressiontweaks.biomes;

import com.killerqu.compressiontweaks.CompressionTweaks;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BiomeRegistry {
    public static DeferredRegister<Biome> BIOME_REGISTER = DeferredRegister.create(Registry.BIOME_REGISTRY, CompressionTweaks.MODID);

    public static void registerBiomes() {
        register(Biomes.END_BIOME, OverworldBiomes::endBiome);
        register(Biomes.VALLEY_BIOME, OverworldBiomes::valleyBiome);
        register(Biomes.DELTAS_BIOME, OverworldBiomes::deltasBiome);
        register(Biomes.CRIMSON_BIOME, OverworldBiomes::crimsonBiome);
        register(Biomes.WARPED_BIOME, OverworldBiomes::warpedBiome);
        register(Biomes.VOID_BIOME, OverworldBiomes::voidBiome);
    }

    public static void register(ResourceKey<Biome> key, Supplier<Biome> biomeSupplier) {
        BIOME_REGISTER.register(key.location().getPath(), biomeSupplier);
    }
}
