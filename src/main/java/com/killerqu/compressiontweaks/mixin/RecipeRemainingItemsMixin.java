package com.killerqu.compressiontweaks.mixin;

import com.killerqu.compressiontweaks.CompressionTweaks;
import com.simibubi.create.content.kinetics.crafter.MechanicalCrafterBlock;
import com.simibubi.create.foundation.data.recipe.MechanicalCraftingRecipeBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
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
//Whenever a recipe calls the default getRemainingItems, we get a chance to replace the output with a list of items.
//The list is determined by the LeftoversOverriceRecipe cache.
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
        //This cache is built as the recipes are registered.
        if(LEFTOVERS_CACHE.containsKey(getId())){
            List<ItemStack> items = LEFTOVERS_CACHE.get(getId());
            if(container.getContainerSize() < items.size()){
                //If the output is overstuffed, abort and return nothing.
                CompressionTweaks.LOGGER.error("Warning: Leftovers override for recipe {} larger than container size!", getId());
                return output;
            }

            int i = -1; //this gets incremented at the start of the loop.
            for (ItemStack item : items){
                i++;
                //Putting "null" in place of an item makes an Itemstack.EMPTY. This serves to pad out spaces.
                //This is to make leftovers in shaped crafting recipes appear at the correct spot.
                if(item.isEmpty())  continue;
                boolean found = false;
                //If an item in the override table was used in the recipe, we try to damage it
                for(int j = 0; j<container.getContainerSize(); j++){
                    if(container.getItem(j).is(item.getItem()) && container.getItem(j).isDamageableItem()){
                        if(!item.hurt(1, CompressionTweaks.RANDOM, null)) {
                            output.set(i, container.getItem(j).getCraftingRemainingItem());
                        }
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
