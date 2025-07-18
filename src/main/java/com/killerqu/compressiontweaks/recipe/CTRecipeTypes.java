package com.killerqu.compressiontweaks.recipe;

import com.killerqu.compressiontweaks.CompressionTweaks;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

//How many of these have i done already?
//Anyway, this is where the recipe types are defined.
public class CTRecipeTypes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CompressionTweaks.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, CompressionTweaks.MODID);

    public static final RegistryObject<RecipeType<BoulderInfoRecipe>> BOULDER_INFO = RECIPE_TYPES.register("boulder_info",
            () -> new RecipeType<BoulderInfoRecipe>() {
                @Override
                public String toString() {
                    return new ResourceLocation(CompressionTweaks.MODID, "boulder_info").toString();
                }
            });
    public static final RegistryObject<RecipeSerializer<BoulderInfoRecipe>> BOULDER_SERIALIZER = RECIPE_SERIALIZERS.register("boulder_info", BoulderInfoRecipe.Serializer::new);

    public static final RegistryObject<RecipeType<LeftoversOverrideRecipe>> LEFTOVERS_OVERRIDE = RECIPE_TYPES.register("leftovers_override",
            () -> new RecipeType<LeftoversOverrideRecipe>() {
                @Override
                public String toString() {
                    return new ResourceLocation(CompressionTweaks.MODID, "leftovers_override").toString();
                }
            });
    public static final RegistryObject<RecipeSerializer<LeftoversOverrideRecipe>> LEFTOVERS_SERIALIZER = RECIPE_SERIALIZERS.register("leftovers_override", LeftoversOverrideRecipe.Serializer::new);

}
