package com.killerqu.compressiontweaks.mixin.bottled_cloud;

import com.simibubi.create.compat.jei.ConversionRecipe;
import com.simibubi.create.compat.jei.category.MysteriousItemConversionCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.quark.content.tools.module.BottledCloudModule;

import java.util.ArrayList;
import java.util.List;

//This is to show the cloud in a bottle as a "mysterious conversion"
//It's the same logic as compressed create recipes, i HOPE they interact nicely
@Mixin(MysteriousItemConversionCategory.class)
public class JEIConversionCategoryMixin {
    //This serves as a sort of cache for the original recipes, as we need to rebuild the list on a /reload
    //Also, the recipe didn't show up when it had the same name as the other one which is...concerning.
    @Unique
    private static List<ConversionRecipe> originalList = new ArrayList<>();

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(CallbackInfo ci){
        if(originalList.isEmpty()){
            originalList.addAll(MysteriousItemConversionCategory.RECIPES);
            MysteriousItemConversionCategory.RECIPES.add(
                    ConversionRecipe.create(new ItemStack(Items.GLASS_BOTTLE), new ItemStack(BottledCloudModule.bottled_cloud))
            );
        }else{ //We need to do this, otherwise it just dupes the recipes on reload.
            MysteriousItemConversionCategory.RECIPES.clear();
            MysteriousItemConversionCategory.RECIPES.addAll(originalList);
            MysteriousItemConversionCategory.RECIPES.add(
                    ConversionRecipe.create(new ItemStack(Items.GLASS_BOTTLE), new ItemStack(BottledCloudModule.bottled_cloud))
            );
        }

    }
}
