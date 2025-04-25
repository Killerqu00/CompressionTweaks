package com.killerqu.compressiontweaks.mixin;

import com.killerqu.compressiontweaks.CompressionTweaks;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

//What i am about to do has not been approved by the vatican
@Mixin(IForgeItemStack.class)
public interface RemainingItemMixin {
    //Ignore the thing about invalid access modifiers, it works.
    @Shadow(remap = false)
    ItemStack self();
    /**
     * @author Big Barza
     * @reason This is to filter out items that shouldn't, in fact, have a remaining crafting item.
     * Also, overwrites yell at you if you don't comment them like this.
     */
    @Overwrite(remap = false)
    default boolean hasCraftingRemainingItem(){
        if(self().is(CompressionTweaks.DAMAGEABLE_TOOLS)) return false;
        else return self().getItem().hasCraftingRemainingItem(self());
    }

}
