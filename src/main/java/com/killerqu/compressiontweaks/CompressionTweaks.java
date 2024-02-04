package com.killerqu.compressiontweaks;

import com.killerqu.compressiontweaks.biomes.BiomeRegistry;
import com.killerqu.compressiontweaks.biomes.Biomes;
import com.killerqu.compressiontweaks.biomes.CTRegion;
import com.killerqu.compressiontweaks.biomes.SurfaceRuleData;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;


@Mod(CompressionTweaks.MODID)
public class CompressionTweaks {
    public static final String MODID = "compressiontweaks";
    private static final Logger LOGGER = LogUtils.getLogger();
    public CompressionTweaks() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        BiomeRegistry.BIOME_REGISTER.register(modEventBus);
        BiomeRegistry.registerBiomes();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Setting up CompressionTweaks");
        event.enqueueWork(() ->
        {
            Regions.register(new CTRegion(10));
            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MODID, SurfaceRuleData.makeRules());
        });
    }
}
