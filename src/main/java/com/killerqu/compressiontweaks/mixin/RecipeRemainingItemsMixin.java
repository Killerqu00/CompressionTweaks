package com.killerqu.compressiontweaks.mixin;

import com.killerqu.compressiontweaks.CompressionTweaks;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static com.killerqu.compressiontweaks.recipe.LeftoversOverrideRecipe.LEFTOVERS_CACHE;

//How does this get more sinful every time?
@Mixin(Recipe.class)
public interface RecipeRemainingItemsMixin<C extends Container> {
    @Shadow ResourceLocation getId();

    /**
     * @author Big Barza
     * @reason Thanks, I hate it. You can't inject in an interface's default method. So what do? ...this.
     */
    @Overwrite
    default NonNullList<ItemStack> getRemainingItems(C container){
        NonNullList<ItemStack> output = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
        if(LEFTOVERS_CACHE.containsKey(getId())){
            List<ItemStack> items = LEFTOVERS_CACHE.get(getId());
            if(container.getContainerSize() < items.size()){
                CompressionTweaks.LOGGER.error("Warning: Leftovers override for recipe {} larger than container size!", getId());
                return output;
            }

            int i = -1;
            for (ItemStack item : items){
                i++;
                if(item.isEmpty())  continue;
                boolean found = false;
                for(int j = 0; j<container.getContainerSize(); j++){
                    if(container.getItem(j).is(item.getItem()) && container.getItem(j).hasCraftingRemainingItem()){
                        output.set(i, container.getItem(j).getCraftingRemainingItem());
                        found = true;
                        break;
                    }
                }
                if(!found) output.set(i, item.copy());
            }
        }else{ //Use vanilla logic instead.
            for(int i = 0; i < output.size(); ++i) {
                ItemStack item = container.getItem(i);
                if (item.hasCraftingRemainingItem()) {
                    output.set(i, item.getCraftingRemainingItem());
                }
            }
        }
        return output;
    }

}
