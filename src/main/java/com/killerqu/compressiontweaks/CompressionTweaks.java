package com.killerqu.compressiontweaks;

import com.killerqu.compressiontweaks.biomes.BiomeRegistry;
import com.killerqu.compressiontweaks.biomes.CTRegion;
import com.killerqu.compressiontweaks.biomes.SurfaceRuleData;
import com.killerqu.compressiontweaks.config.CTCommonConfig;
import com.killerqu.compressiontweaks.recipe.CTRecipeTypes;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;


@Mod(CompressionTweaks.MODID)
public class CompressionTweaks {
    public static final String MODID = "compressiontweaks";
    private static final Logger LOGGER = LogUtils.getLogger();

    //NOTE: This tag does not need to contain the AE2 Mysterious cube, that is still tracked.
    public final static TagKey<Block> ATTRACTS_COMPASS = BlockTags.create(new ResourceLocation(CompressionTweaks.MODID, "attracts_meteorite_compass"));
    //For our purposes so far, this should have the engineer's hammer and quartz knives.
    public final static TagKey<Item> DAMAGEABLE_TOOLS = ItemTags.create(new ResourceLocation(CompressionTweaks.MODID, "crafting_damageable_tools"));

    public CompressionTweaks() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        BiomeRegistry.BIOME_REGISTER.register(modEventBus);
        BiomeRegistry.registerBiomes();
        CTRecipeTypes.RECIPE_TYPES.register(modEventBus);
        CTRecipeTypes.RECIPE_SERIALIZERS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CTCommonConfig.SPEC);
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
