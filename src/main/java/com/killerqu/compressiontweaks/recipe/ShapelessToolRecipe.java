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
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
//compressiontweaks:shapeless_crafting_tool
//This is a clone of shapeless crafting which does deduct durability from tools which had that function removed.
public class ShapelessToolRecipe extends ShapelessRecipe {
    private final RandomSource random = RandomSource.create();

    public ShapelessToolRecipe(ResourceLocation p_44246_, String p_44247_, ItemStack p_44248_, NonNullList<Ingredient> p_44249_) {
        super(p_44246_, p_44247_, p_44248_, p_44249_);
    }
    public ShapelessToolRecipe(ShapelessRecipe r){
        super(r.getId(), r.getGroup(), r.getResultItem(), r.getIngredients());
    }

    //The base logic is mostly from AE2's fix, but adapted to be more generic.
    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingContainer input){
        NonNullList<ItemStack> remainingItems = NonNullList.withSize(input.getContainerSize(), ItemStack.EMPTY);
        for(int i = 0; i<remainingItems.size(); i++){
            ItemStack item = input.getItem(i).copy();
            if(item.is(CompressionTweaks.DAMAGEABLE_TOOLS)){
                if(item.hurt(1, random, null)) item = ItemStack.EMPTY;
                remainingItems.set(i, item);
            } else if (item.hasCraftingRemainingItem()) {
                remainingItems.set(i, item.getCraftingRemainingItem());
            }
        }
        return remainingItems;
    }


    @Override
    public RecipeSerializer<?> getSerializer(){
        return CTRecipeTypes.SHAPELESS_TOOL_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<ShapelessToolRecipe> {
        public static ShapelessRecipe.Serializer serializer = new ShapelessRecipe.Serializer();
        public @NotNull ShapelessToolRecipe fromJson(ResourceLocation id, JsonObject json){
            ShapelessRecipe r = serializer.fromJson(id, json);
            //No, you can't just cast it, for SOME reason.
            return new ShapelessToolRecipe(r);
        }
        public @Nullable ShapelessToolRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf){
            return new ShapelessToolRecipe(serializer.fromNetwork(id, buf));
        }
        public void toNetwork(FriendlyByteBuf buf, ShapelessToolRecipe recipe){
            serializer.toNetwork(buf, recipe);
        }
    }

}
