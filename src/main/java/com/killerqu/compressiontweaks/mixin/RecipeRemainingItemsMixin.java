package com.killerqu.compressiontweaks.mixin;

import com.killerqu.compressiontweaks.CompressionTweaks;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static com.killerqu.compressiontweaks.recipe.LeftoversOverrideRecipe.LEFTOVERS_CACHE;

//How does this get more sinful every time?
@Mixin(Recipe.class)
public abstract class RecipeRemainingItemsMixin<C extends Container> {
    @Shadow public abstract ResourceLocation getId();

    @Inject(method = "getRemainingItems", at = @At("HEAD"), cancellable = true)
    private void redirectLeftoverOutput(C container, CallbackInfoReturnable<NonNullList<ItemStack>> cir){
        if(LEFTOVERS_CACHE.containsKey(getId())){
            List<ItemStack> items = LEFTOVERS_CACHE.get(getId());
            if(container.getContainerSize() < items.size()){
                CompressionTweaks.LOGGER.error("Warhing: Leftovers override for recipe {} larger than container size!", getId());
                return;
            }
            NonNullList<ItemStack> output = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
            int i = -1;
            for (ItemStack item : items){
                i++;
                if(item.isEmpty())  continue;
                boolean found = false;
                for(int j = 0; j<container.getContainerSize(); j++){
                    if(container.getItem(j).is(item.getItem()) && container.getItem(j).hasCraftingRemainingItem()){
                        output.add(i, container.getItem(j).getCraftingRemainingItem());
                        found = true;
                        break;
                    }
                }
                if(!found) output.add(i, item.copy());
            }
        }
    }
}
