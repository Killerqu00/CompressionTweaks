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

    public static final RegistryObject<RecipeType<ShapedToolRecipe>> SHAPED_TOOL_RECIPE_TYPE = RECIPE_TYPES.register("shaped_crafting_tool",
            () -> new RecipeType<ShapedToolRecipe>() {
                @Override
                public String toString() {
                    return new ResourceLocation(CompressionTweaks.MODID, "shaped_crafting_tool").toString();
                }
            });
    public static final RegistryObject<RecipeSerializer<ShapedToolRecipe>> SHAPED_TOOL_SERIALIZER = RECIPE_SERIALIZERS.register("shaped_crafting_tool", ShapedToolRecipe.Serializer::new);

    public static final RegistryObject<RecipeType<ShapelessToolRecipe>> SHAPELESS_TOOL_RECIPE_TYPE = RECIPE_TYPES.register("shapeless_crafting_tool",
            () -> new RecipeType<ShapelessToolRecipe>() {
                @Override
                public String toString() {
                    return new ResourceLocation(CompressionTweaks.MODID, "shapeless_crafting_tool").toString();
                }
            });
    public static final RegistryObject<RecipeSerializer<ShapelessToolRecipe>> SHAPELESS_TOOL_SERIALIZER = RECIPE_SERIALIZERS.register("shapeless_crafting_tool", ShapelessToolRecipe.Serializer::new);


}
