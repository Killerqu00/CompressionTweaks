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
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

import static com.killerqu.compressiontweaks.recipe.LeftoversOverrideRecipe.LEFTOVERS_CACHE;
import static com.killerqu.compressiontweaks.recipe.LeftoversOverrideRecipe.LEFTOVERS_TEMP_CACHE;


@Mod(CompressionTweaks.MODID)
public class CompressionTweaks {
    public static final String MODID = "compressiontweaks";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final RandomSource RANDOM = RandomSource.create();
    //NOTE: This tag does not need to contain the AE2 Mysterious cube, that is still tracked.
    public final static TagKey<Block> ATTRACTS_COMPASS = BlockTags.create(new ResourceLocation(CompressionTweaks.MODID, "attracts_meteorite_compass"));
    //For our purposes so far, this should have the engineer's hammer and quartz knives.
    public final static TagKey<Item> DAMAGEABLE_TOOLS = ItemTags.create(new ResourceLocation(CompressionTweaks.MODID, "consumable_tools"));

    public CompressionTweaks() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        BiomeRegistry.BIOME_REGISTER.register(modEventBus);
        BiomeRegistry.registerBiomes();
        CTRecipeTypes.RECIPE_TYPES.register(modEventBus);
        CTRecipeTypes.RECIPE_SERIALIZERS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
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


    public static class EventHandler {
        @SubscribeEvent
        public static void onReloadServerResources(OnDatapackSyncEvent e){
            //We don't have a clean way to wipe the cache before resources start reloading.
            //So the recipes are dumped to a temporary cache and when that's done, we wipe the main cache and move in the contents of the temp cache.
            //Then wipe the temp cache, of course.
            LEFTOVERS_CACHE.clear();
            LEFTOVERS_CACHE.putAll(LEFTOVERS_TEMP_CACHE);
            LEFTOVERS_TEMP_CACHE.clear();
        }
    }

}
