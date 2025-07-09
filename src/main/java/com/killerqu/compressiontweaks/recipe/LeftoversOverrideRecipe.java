package com.killerqu.compressiontweaks.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.killerqu.compressiontweaks.CompressionTweaks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeftoversOverrideRecipe implements Recipe<Inventory> {

    public static Map<ResourceLocation, List<ItemStack>> LEFTOVERS_CACHE = new HashMap<>();
    public static Map<ResourceLocation, List<ItemStack>> LEFTOVERS_TEMP_CACHE = new HashMap<>();

    private final ResourceLocation id;
    private final ResourceLocation target;
    private final List<ItemStack> items;

    public LeftoversOverrideRecipe(ResourceLocation id, ResourceLocation target, List<ItemStack> items){
        this.id = id;
        this.target = target;
        this.items = items;
        LEFTOVERS_TEMP_CACHE.put(target, items);
    }

    public ResourceLocation getTarget(){ return target; }
    public List<ItemStack> getItems(){ return items; }
    @Override public ResourceLocation getId() { return id; }
    @Override public RecipeSerializer<?> getSerializer(){
        return CTRecipeTypes.LEFTOVERS_SERIALIZER.get();
    }
    @Override public RecipeType<?> getType() { return CTRecipeTypes.LEFTOVERS_OVERRIDE.get(); }

    @Override public boolean matches(Inventory p_44002_, Level p_44003_) { return false; }
    @Override public ItemStack assemble(Inventory p_44001_) { return null; }
    @Override public boolean canCraftInDimensions(int p_43999_, int p_44000_) { return false; }
    @Override public ItemStack getResultItem() { return ItemStack.EMPTY; }

    public static class Serializer implements RecipeSerializer<LeftoversOverrideRecipe> {
        public @NotNull LeftoversOverrideRecipe fromJson(ResourceLocation id, JsonObject json){
            try {
                ResourceLocation target = new ResourceLocation(GsonHelper.getAsString(json, "target"));
                List<ItemStack> items = new ArrayList<>();
                JsonArray list = GsonHelper.getAsJsonArray(json, "items");
                list.forEach(jsonElement -> {
                    if(jsonElement.isJsonNull()) items.add(ItemStack.EMPTY);
                    else items.add(ShapedRecipe.itemStackFromJson(jsonElement.getAsJsonObject()));
                });
                return new LeftoversOverrideRecipe(id, target, items);
            } catch (Exception e) {
                e.printStackTrace();
                return new LeftoversOverrideRecipe(id, new ResourceLocation("dummy"), new ArrayList<>());
            }
        }
        public @Nullable LeftoversOverrideRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf){
            ResourceLocation target = new ResourceLocation(buf.readUtf());
            int size = buf.readInt();
            List<ItemStack> items = new ArrayList<>();
            for(int i = 0; i<size; i++){
                items.add(buf.readItem());
            }
            return new LeftoversOverrideRecipe(id, target, items);
        }
        public void toNetwork(FriendlyByteBuf buf, LeftoversOverrideRecipe recipe){
            buf.writeUtf(recipe.getTarget().toString());
            buf.writeInt(recipe.getItems().size());
            for(ItemStack item : recipe.getItems()){
                buf.writeItem(item);
            }
        }
    }

}
