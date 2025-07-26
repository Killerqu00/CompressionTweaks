package com.killerqu.compressiontweaks.mixin.vintageimprovements;

import com.negodya1.vintageimprovements.compat.jei.category.PressurizingCategory;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

//So VI calls a create method to "make a fluidstack more visible". What it does is it takes the amount. multiplies it by 0.75, then adds 250.
//This new value is normally hidden, but with emi, it leaks through. So this just....doesn't call it, and returns the originals.

@Mixin(PressurizingCategory.class)
public class PressurizingCategoryMixin {
    @Redirect(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lcom/simibubi/create/content/processing/basin/BasinRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V",
            at = @At(value = "INVOKE", target = "Lcom/negodya1/vintageimprovements/compat/jei/category/PressurizingCategory;withImprovedVisibility(Lnet/minecraftforge/fluids/FluidStack;)Lnet/minecraftforge/fluids/FluidStack;"), remap = false)
    private FluidStack resetFluidAmount(FluidStack fluidStack){
        return fluidStack;
    }
    //There's two of them because one uses a list, the other a single fluidstack.
    @Redirect(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lcom/simibubi/create/content/processing/basin/BasinRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V",
            at = @At(value = "INVOKE", target = "Lcom/negodya1/vintageimprovements/compat/jei/category/PressurizingCategory;withImprovedVisibility(Ljava/util/List;)Ljava/util/List;"), remap = false)
    private List<FluidStack> resetFluidAmount(List<FluidStack> list){
        return list;
    }
}
