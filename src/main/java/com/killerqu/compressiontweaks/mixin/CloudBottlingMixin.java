package com.killerqu.compressiontweaks.mixin;

import com.killerqu.compressiontweaks.config.CTCommonConfig;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.quark.content.tools.module.BottledCloudModule;

//This mixin is to allow the cloud in a bottle to be created by the bottle's use method instead of a player interact event.
//This way, the process can be automated with a deployer. If someone asks, no, doing it with dispensers is a lot more complex.
@Mixin(BottleItem.class)
public class CloudBottlingMixin {
    @Inject(method = "use", at = @At("RETURN"), cancellable = true)
    private void getCloudInABottle(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir){
        InteractionResultHolder<ItemStack> resultHolder = cir.getReturnValue();
        //PASS is the case where nothing happens. If something did happen, don't do anything else.
        //This is actually inverted from quark's logic, where the cloud bottling takes priority.
        if(resultHolder.getResult() != InteractionResult.PASS) return;
        if(player.getY() > CTCommonConfig.CLOUD_BOTTLING_MIN_Y.get() && player.getY() < CTCommonConfig.CLOUD_BOTTLING_MAX_Y.get()){
            resultHolder.getObject().shrink(1);
            //TODO: Evaluate whether it's worth it to ditch the quark dependency and replace this with a call to forge registries
            ItemStack bottledCloud = new ItemStack(BottledCloudModule.bottled_cloud);
            //It's how quark does it.
            if(!player.addItem(bottledCloud)) player.drop(bottledCloud, false);
            cir.setReturnValue(InteractionResultHolder.success(resultHolder.getObject()));
        }
    }
}
