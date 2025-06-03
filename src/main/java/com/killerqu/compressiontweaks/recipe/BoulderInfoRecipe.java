package com.killerqu.compressiontweaks.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.util.JsonUtils;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BoulderInfoRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final List<ItemStack> ores;
    private final List<ItemStack> conglomerates;
    private final List<ItemStack> primary_resources;
    private final List<ItemStack> secondary_items;
    private final List<FluidStack> secondary_fluids;

    public BoulderInfoRecipe(ResourceLocation id, List<ItemStack> ores, List<ItemStack> conglomerates, List<ItemStack> primary_resources, List<ItemStack> secondary_items , List<FluidStack> secondary_fluids){
        this.id = id;
        this.ores = ores;
        this.conglomerates = conglomerates;
        this.primary_resources = primary_resources;
        this.secondary_items = secondary_items;
        this.secondary_fluids = secondary_fluids;
    }

    public static class Serializer implements RecipeSerializer<BoulderInfoRecipe>{

        @Override
        public BoulderInfoRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonArray oresList = json.getAsJsonArray("ores");
            List<ItemStack> ores = new ArrayList<>();
            oresList.forEach((jsonElement -> {
                ores.add(ShapedRecipe.itemStackFromJson(jsonElement.getAsJsonObject()));
            }));
            JsonArray blocksList = json.getAsJsonArray("conglomerates");
            List<ItemStack> blocks = new ArrayList<>();
            blocksList.forEach((jsonElement -> {
                blocks.add(ShapedRecipe.itemStackFromJson(jsonElement.getAsJsonObject()));
            }));
            JsonArray primaryList = json.getAsJsonArray("primary_resources");
            List<ItemStack> primaries = new ArrayList<>();
            primaryList.forEach((jsonElement -> {
                primaries.add(ShapedRecipe.itemStackFromJson(jsonElement.getAsJsonObject()));
            }));

            JsonArray secondariesList = json.getAsJsonArray("secondary_resources");
            List<ItemStack> secondaries = new ArrayList<>();
            secondariesList.forEach((jsonElement -> {
                secondaries.add(ShapedRecipe.itemStackFromJson(jsonElement.getAsJsonObject()));
            }));

            JsonArray fluidsList = json.getAsJsonArray("secondary_fluids");
            List<FluidStack> fluids = new ArrayList<>();
            fluidsList.forEach((jsonElement -> {
                fluids.add(jsonToFluidStack(jsonElement.getAsJsonObject()));
            }));



            return new BoulderInfoRecipe(id, ores, blocks, primaries, secondaries, fluids);
        }

        @Override
        public @Nullable BoulderInfoRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            int oresLength = buf.readInt();
            List<ItemStack> ores = new ArrayList<>();
            for(int i = 0; i<oresLength;i++){
                ores.add(buf.readItem());
            }
            int blocksLength = buf.readInt();
            List<ItemStack> blocks = new ArrayList<>();
            for(int i = 0; i<blocksLength;i++){
                blocks.add(buf.readItem());
            }
            int primariesLength = buf.readInt();
            List<ItemStack> primaries = new ArrayList<>();
            for(int i = 0; i<primariesLength;i++){
                primaries.add(buf.readItem());
            }
            int secondariesLength = buf.readInt();
            List<ItemStack> secondaries = new ArrayList<>();
            for(int i = 0; i<secondariesLength;i++){
                secondaries.add(buf.readItem());
            }
            int fluidsLength = buf.readInt();
            List<FluidStack> fluids = new ArrayList<>();
            for(int i = 0; i<fluidsLength;i++){
                fluids.add(buf.readFluidStack());
            }
            return new BoulderInfoRecipe(id, ores, blocks, primaries, secondaries, fluids);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, BoulderInfoRecipe recipe) {
            List<ItemStack> ores = recipe.getOres();
            List<ItemStack> blocks = recipe.getConglomerates();
            List<ItemStack> primaries = recipe.getPrimaries();
            List<ItemStack> secondaries = recipe.getSecondaries();
            List<FluidStack> fluids = recipe.getFluids();
            buf.writeInt(ores.size());
            for(int i = 0; i<ores.size();i++){
                buf.writeItem(ores.get(i));
            }
            buf.writeInt(blocks.size());
            for(int i = 0; i<blocks.size();i++){
                buf.writeItem(blocks.get(i));
            }
            buf.writeInt(primaries.size());
            for(int i = 0; i<primaries.size();i++){
                buf.writeItem(primaries.get(i));
            }
            buf.writeInt(secondaries.size());
            for(int i = 0; i<secondaries.size();i++){
                buf.writeItem(secondaries.get(i));
            }
            buf.writeInt(fluids.size());
            for(int i = 0; i<fluids.size();i++){
                buf.writeFluidStack(fluids.get(i));
            }
        }
    }


    public static FluidStack jsonToFluidStack(JsonObject jsonObject)
    {
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(GsonHelper.getAsString(jsonObject, "fluid")));
        int amount = GsonHelper.getAsInt(jsonObject, "amount");
        FluidStack fluidStack = new FluidStack(fluid, amount);
        if(GsonHelper.isValidNode(jsonObject, "tag"))
            fluidStack.setTag(JsonUtils.readNBT(jsonObject, "tag"));
        return fluidStack;
    }



    public List<ItemStack> getOres(){
        return ores;
    }
    public List<ItemStack> getConglomerates(){
        return conglomerates;
    }
    public List<ItemStack> getPrimaries(){
        return primary_resources;
    }
    public List<ItemStack> getSecondaries(){
        return secondary_items;
    }
    public List<FluidStack> getFluids(){
        return secondary_fluids;
    }

    @Override
    public boolean matches(Container p_44002_, Level p_44003_) {
        return false;
    }

    @Override
    public ItemStack assemble(Container p_44001_) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CTRecipeTypes.BOULDER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CTRecipeTypes.BOULDER_INFO.get();
    }
}
