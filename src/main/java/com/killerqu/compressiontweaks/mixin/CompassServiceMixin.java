package com.killerqu.compressiontweaks.mixin;

import appeng.server.services.compass.CompassService;
import com.killerqu.compressiontweaks.CompressionTweaks;
import com.killerqu.compressiontweaks.config.CTCommonConfig;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.concurrent.atomic.AtomicInteger;

//I would like to thank that one user on the Sponge discord, them attempting to do this showed me that you can mixin into lambdas.
//Anyway, this mixin injects into AE2's scanning for mysterious cubes and adds a check against a tag to define what blocks get tracked.

@Mixin(CompassService.class)
public class CompassServiceMixin {

    @Inject(
            method = "lambda$updateArea$0", //We cannot inject anywhere else, but this is the bit that matters.
            at = @At("HEAD"), remap = false //Damn you, CompassRegion. Private and all that, can't even use ATs on it.
    )
    private static void lambda(BlockState desiredState, AtomicInteger blockCount, BlockState state, int count, CallbackInfo ci){
        //The config flag here should dictate whether tagged blocks get marked by the compass.
        if(CTCommonConfig.BOULDER_COMPASS.get() && state.is(CompressionTweaks.ATTRACTS_COMPASS)) {
            blockCount.getAndIncrement(); //For some reason, it won't go past one...but it doesn't matter, as long as the chunk is marked correctly.
            }
        //We aren't cancelling here, so the rest of the lambda, which checks for the cube still runs.
        }

    }
