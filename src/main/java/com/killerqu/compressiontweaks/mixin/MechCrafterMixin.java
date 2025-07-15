package com.killerqu.compressiontweaks.mixin;

import com.killerqu.compressiontweaks.CompressionTweaks;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.crafter.MechanicalCrafterBlockEntity;
import com.simibubi.create.content.kinetics.crafter.MechanicalCraftingInventory;
import com.simibubi.create.content.kinetics.crafter.RecipeGridHandler;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.*;

import static com.killerqu.compressiontweaks.recipe.LeftoversOverrideRecipe.LEFTOVERS_CACHE;

@Mixin(MechanicalCrafterBlockEntity.class)
public class MechCrafterMixin {
    @Shadow(remap = false) protected RecipeGridHandler.GroupedItems groupedItems;

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/Map;values()Ljava/util/Collection;"), remap = false)
    private Collection<ItemStack> cancelLeftovers(Map<Pair<Integer, Integer>, ItemStack> grid, @Local(index = 4) List<ItemStack> containers){
        MechanicalCrafterBlockEntity self = (MechanicalCrafterBlockEntity) (Object) this;
        if(self.getLevel() == null) return grid.values();
        CraftingContainer inventory = new MechanicalCraftingInventory(groupedItems);
        List<ItemStack> items = null;
        CraftingRecipe craftingRecipe = self.getLevel().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, inventory, self.getLevel())
                .filter(r -> RecipeGridHandler.isRecipeAllowed(r, inventory))
                .orElse(null);
        if(craftingRecipe != null && LEFTOVERS_CACHE.containsKey(craftingRecipe.getId())){
                items = LEFTOVERS_CACHE.get(craftingRecipe.getId());
        } else {
            Recipe<CraftingContainer> mechRecipe = AllRecipeTypes.MECHANICAL_CRAFTING.find(inventory, self.getLevel()).orElse(null);
            if(mechRecipe != null && LEFTOVERS_CACHE.containsKey(mechRecipe.getId())) {
                items = LEFTOVERS_CACHE.get(mechRecipe.getId());
            }
        }
        if(items != null) {
            for(ItemStack item : items){
                if(item.isEmpty()) continue;
                boolean found = false;
                for(int i = 0;i<inventory.getContainerSize();i++){
                    if(inventory.getItem(i).is(item.getItem()) && !item.hurt(1, CompressionTweaks.RANDOM, null)){
                        containers.add(inventory.getItem(i).getCraftingRemainingItem());
                        found = true;
                        break;
                    }
                }
                if(!found) containers.add(item);
            }
            return new ArrayList<>();
        }
        else return grid.values();
    }
}
