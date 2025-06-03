package com.killerqu.compressiontweaks.jei;

import com.killerqu.compressiontweaks.CompressionTweaks;
import com.killerqu.compressiontweaks.recipe.BoulderInfoRecipe;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.quark.base.module.config.Config;

import java.util.ArrayList;
import java.util.List;

public class BoulderInfoCategory implements IRecipeCategory<BoulderInfoRecipe> {
    public static final RecipeType<BoulderInfoRecipe> TYPE = RecipeType.create(CompressionTweaks.MODID, "boulders", BoulderInfoRecipe.class);
    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slot;

    public static final Component TEXT_ORES = Component.translatable("compressiontweaks.jei.bouldercategory.ores").withStyle(ChatFormatting.UNDERLINE);
    public static final Component TEXT_CONGLOMERATES = Component.translatable("compressiontweaks.jei.bouldercategory.conglomerates").withStyle(ChatFormatting.UNDERLINE);
    public static final Component TEXT_PRIMARIES = Component.translatable("compressiontweaks.jei.bouldercategory.primaries").withStyle(ChatFormatting.UNDERLINE);
    public static final Component TEXT_SECONDARIES = Component.translatable("compressiontweaks.jei.bouldercategory.secondaries").withStyle(ChatFormatting.UNDERLINE);

    public BoulderInfoCategory(IGuiHelper helper){
        this.title = Component.translatable("compressiontweaks.jei.bouldercategory_title");
        this.background = helper.createBlankDrawable(180, 140);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("nyagibits_bytes", "core_conglomerate_redstone_ore"))));
        this.slot = helper.getSlotDrawable();
    }

    @Override
    public RecipeType<BoulderInfoRecipe> getRecipeType() {
        return TYPE;
    }
    @Override
    public Component getTitle(){
        return title;
    }
    @Override
    public IDrawable getBackground(){
        return background;
    }
    @Override
    public IDrawable getIcon(){
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BoulderInfoRecipe recipe, IFocusGroup focuses) {
        int xOffset = background.getWidth()/2 - (9*recipe.getOres().size())+1;
        for(int i = 0;i<recipe.getOres().size();i++) {
            ItemStack item = recipe.getOres().get(i);
            builder.addSlot(RecipeIngredientRole.OUTPUT, xOffset, 24).addItemStack(item.getItem().getDefaultInstance())
                    .addTooltipCallback((recipeSlotView, tooltip) -> {
                        if(item.getCount() > 1) tooltip.add(Component.translatable("compressiontweaks.jei.bouldercategory_expectedyield", item.getCount()));
                    });
            xOffset += 18;
        }
        xOffset = background.getWidth()/2 - (9*recipe.getConglomerates().size())+1;
        for(int i = 0;i<recipe.getConglomerates().size();i++) {
            int index = i;
            ItemStack item = recipe.getConglomerates().get(i);
            builder.addSlot(RecipeIngredientRole.INPUT, xOffset, 57).addItemStack(item.getItem().getDefaultInstance())
                    .addTooltipCallback((recipeSlotView, tooltip) -> {
                        if(item.getCount() > 1) tooltip.add(Component.translatable("compressiontweaks.jei.bouldercategory_expectedyield", item.getCount()));
                        tooltip.add(getConglomerateTooltip(index));
                    });
            xOffset += 18;
        }
        xOffset = background.getWidth()/2 - (9*recipe.getPrimaries().size())+1;
        for(int i = 0;i<recipe.getPrimaries().size();i++) {
            ItemStack item = recipe.getPrimaries().get(i);
            builder.addSlot(RecipeIngredientRole.OUTPUT, xOffset, 90).addItemStack(item.getItem().getDefaultInstance())
                    .addTooltipCallback((recipeSlotView, tooltip) -> {
                        if(item.getCount() > 1) tooltip.add(Component.translatable("compressiontweaks.jei.bouldercategory_expectedyield", item.getCount()));
                    });
            xOffset += 18;
        }

        xOffset = background.getWidth()/2 - (9*(recipe.getSecondaries().size() + recipe.getFluids().size()))+1;
        for(int i = 0;i<recipe.getSecondaries().size();i++) {
            ItemStack item = recipe.getSecondaries().get(i);
            builder.addSlot(RecipeIngredientRole.OUTPUT, xOffset, 123).addItemStack(item.getItem().getDefaultInstance())
                    .addTooltipCallback((recipeSlotView, tooltip) -> {
                        if(item.getCount() > 1) tooltip.add(Component.translatable("compressiontweaks.jei.bouldercategory_expectedyield", item.getCount()));
                    });
            xOffset += 18;
        }
        for(int i = 0;i<recipe.getFluids().size();i++) {
            FluidStack fluid = recipe.getFluids().get(i);
            builder.addSlot(RecipeIngredientRole.OUTPUT, xOffset, 123).addFluidStack(fluid.getFluid(), fluid.getAmount())
                    .addTooltipCallback((recipeSlotView, tooltip) -> {
                        tooltip.add(Component.translatable("compressiontweaks.jei.bouldercategory_expectedyield", fluid.getAmount()+"mb"));
                    });
            xOffset += 18;
        }

    }

    @Override
    public void draw(BoulderInfoRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack ms, double mouseX, double mouseY) {
        RenderSystem.enableBlend();
        Font font = Minecraft.getInstance().font;
        Component boulderName = Component.translatable("compressiontweaks.jei.boulders."+recipe.getId().getPath());
        float textPos = ((float) background.getWidth()/2) - ((float) font.width(boulderName.getString())/2);
        font.draw(ms, boulderName, textPos, 1, 0x888888);
        GuiComponent.fill(ms, 10, 10, 170,11, 0xFF666666);
        textPos = ((float) background.getWidth()/2) - ((float) font.width(TEXT_ORES.getString())/2);
        font.draw(ms, TEXT_ORES, textPos, 13, 0x888888);
        GuiComponent.fill(ms, 10, 43, 170,44, 0xFF666666);
        textPos = ((float) background.getWidth()/2) - ((float) font.width(TEXT_CONGLOMERATES.getString())/2);
        font.draw(ms, TEXT_CONGLOMERATES, textPos, 46, 0x888888);
        GuiComponent.fill(ms, 10, 76, 170,77, 0xFF666666);
        textPos = ((float) background.getWidth()/2) - ((float) font.width(TEXT_PRIMARIES.getString())/2);
        font.draw(ms, TEXT_PRIMARIES, textPos, 79, 0x888888);
        GuiComponent.fill(ms, 10, 109, 170,110, 0xFF666666);
        textPos = ((float) background.getWidth()/2) - ((float) font.width(TEXT_SECONDARIES.getString())/2);
        font.draw(ms, TEXT_SECONDARIES, textPos, 112, 0x888888);


        int xOffset = background.getWidth()/2 - (9*recipe.getOres().size());
        for(int i = 0;i<recipe.getOres().size();i++) {
            slot.draw(ms, xOffset, 23);
            xOffset += 18;
        }
        xOffset = background.getWidth()/2 - (9*recipe.getConglomerates().size());
        for(int i = 0;i<recipe.getConglomerates().size();i++) {
            slot.draw(ms, xOffset, 56);
            xOffset += 18;
        }
        xOffset = background.getWidth()/2 - (9*recipe.getPrimaries().size());
        for(int i = 0;i<recipe.getPrimaries().size();i++) {
            slot.draw(ms, xOffset, 89);
            xOffset += 18;
        }

        xOffset = background.getWidth()/2 - (9*(recipe.getSecondaries().size() + recipe.getFluids().size()));
        for(int i = 0;i<recipe.getSecondaries().size();i++) {
            slot.draw(ms, xOffset, 122);
            xOffset += 18;
        }
        for(int i = 0;i<recipe.getFluids().size();i++) {
            slot.draw(ms, xOffset, 122);
            xOffset += 18;
        }


        RenderSystem.disableBlend();
    }

    @Override
    public List<Component> getTooltipStrings(BoulderInfoRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY){
        List<Component> tooltips = new ArrayList<>();
        Font font = Minecraft.getInstance().font;
        float textPos = ((float) background.getWidth()/2) - ((float) font.width(TEXT_ORES.getString())/2);
        if(mouseX > textPos && mouseX < textPos+font.width(TEXT_ORES.getString()) && mouseY >= 13 && mouseY <= 13 + font.lineHeight){
            tooltips.add(Component.translatable("compressiontweaks.jei.bouldercategory.info.ores"));
        }
        textPos = ((float) background.getWidth()/2) - ((float) font.width(TEXT_CONGLOMERATES.getString())/2);
        if(mouseX > textPos && mouseX < textPos+font.width(TEXT_CONGLOMERATES.getString()) && mouseY >= 46 && mouseY <= 46 + font.lineHeight){
            tooltips.add(Component.translatable("compressiontweaks.jei.bouldercategory.info.conglomerates"));
        }
        textPos = ((float) background.getWidth()/2) - ((float) font.width(TEXT_PRIMARIES.getString())/2);
        if(mouseX > textPos && mouseX < textPos+font.width(TEXT_PRIMARIES.getString()) && mouseY >= 79 && mouseY <= 79 + font.lineHeight){
            tooltips.add(Component.translatable("compressiontweaks.jei.bouldercategory.info.primaries"));
        }
        textPos = ((float) background.getWidth()/2) - ((float) font.width(TEXT_SECONDARIES.getString())/2);
        if(mouseX > textPos && mouseX < textPos+font.width(TEXT_SECONDARIES.getString()) && mouseY >= 112 && mouseY <= 112 + font.lineHeight){
            tooltips.add(Component.translatable("compressiontweaks.jei.bouldercategory.info.secondaries"));
        }

        return tooltips;
    }


    private Component getConglomerateTooltip(int index){
        switch (index){
            case 0: return Component.translatable("compressiontweaks.jei.bouldercategory_yield_conglomerate", 4);
            case 1: return Component.translatable("compressiontweaks.jei.bouldercategory_yield_conglomerate", 16);
            case 2: return Component.translatable("compressiontweaks.jei.bouldercategory_yield_conglomerate", 64);
            case 3: return Component.translatable("compressiontweaks.jei.bouldercategory_yield_conglomerate", 256);
            case 4: return Component.translatable("compressiontweaks.jei.bouldercategory_yield_conglomerate_core"); //Core -> Tectonic
        }
        //This only happens if more than 5 things are listed under conglomerates. Don't do that!
        return Component.literal("Error: This should not happen");
    }


}
