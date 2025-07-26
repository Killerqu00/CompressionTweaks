package com.killerqu.compressiontweaks.mixin.vintageimprovements;

import com.negodya1.vintageimprovements.compat.jei.category.VacuumizingCategory;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

//Look at the pressurizing one, they're identical.
@Mixin(VacuumizingCategory.class)
public class VacuumizingCategoryMixin {
    @Redirect(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lcom/simibubi/create/content/processing/basin/BasinRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V",
            at = @At(value = "INVOKE", target = "Lcom/negodya1/vintageimprovements/compat/jei/category/VacuumizingCategory;withImprovedVisibility(Lnet/minecraftforge/fluids/FluidStack;)Lnet/minecraftforge/fluids/FluidStack;"), remap = false)
    private FluidStack resetFluidAmount(FluidStack fluidStack){
        return fluidStack;
    }

    @Redirect(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lcom/simibubi/create/content/processing/basin/BasinRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V",
            at = @At(value = "INVOKE", target = "Lcom/negodya1/vintageimprovements/compat/jei/category/VacuumizingCategory;withImprovedVisibility(Ljava/util/List;)Ljava/util/List;"), remap = false)
    private List<FluidStack> resetFluidAmount(List<FluidStack> list){
        return list;
    }
}
