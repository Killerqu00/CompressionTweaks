package com.killerqu.compressiontweaks.recipe;

import com.google.gson.JsonObject;
import com.killerqu.compressiontweaks.CompressionTweaks;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
//compressiontweaks:shaped_crafting_tool
//This is a clone of shaped crafting which does deduct durability from tools which had that function removed.
public class ShapedToolRecipe extends ShapedRecipe {

    public ShapedToolRecipe(ResourceLocation p_44153_, String p_44154_, int p_44155_, int p_44156_, NonNullList<Ingredient> p_44157_, ItemStack p_44158_) {
        super(p_44153_, p_44154_, p_44155_, p_44156_, p_44157_, p_44158_);
    }
    public ShapedToolRecipe(ShapedRecipe r){
        super(r.getId(), r.getGroup(), r.getRecipeWidth(), r.getRecipeHeight(), r.getIngredients(), r.getResultItem());
    }

    //The base logic is mostly from AE2's fix, but adapted to be more generic.
    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingContainer input){
        NonNullList<ItemStack> remainingItems = NonNullList.withSize(input.getContainerSize(), ItemStack.EMPTY);
        for(int i = 0; i<remainingItems.size(); i++){
            ItemStack item = input.getItem(i).copy();
            if(item.is(CompressionTweaks.DAMAGEABLE_TOOLS)){
                if(item.hurt(1, CompressionTweaks.RANDOM, null)) item = ItemStack.EMPTY;
                remainingItems.set(i, item);
            } else if (item.hasCraftingRemainingItem()) {
                remainingItems.set(i, item.getCraftingRemainingItem());
            }
        }
        return remainingItems;
    }


    @Override
    public RecipeSerializer<?> getSerializer(){
        return CTRecipeTypes.SHAPED_TOOL_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<ShapedToolRecipe> {
        public static ShapedRecipe.Serializer serializer = new ShapedRecipe.Serializer();
        public @NotNull ShapedToolRecipe fromJson(ResourceLocation id, JsonObject json){
            ShapedRecipe r = serializer.fromJson(id, json);
            //No, you can't just cast it, for SOME reason.
            return new ShapedToolRecipe(r);
        }
        public @Nullable ShapedToolRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf){
            return new ShapedToolRecipe(serializer.fromNetwork(id, buf));
        }
        public void toNetwork(FriendlyByteBuf buf, ShapedToolRecipe recipe){
            serializer.toNetwork(buf, recipe);
        }
    }

}
