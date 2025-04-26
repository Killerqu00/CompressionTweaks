package com.killerqu.compressiontweaks.mixin.compass;

import appeng.server.services.compass.CompassService;
import com.killerqu.compressiontweaks.config.CTCommonConfig;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Structure.class)
public class StructureMixin {
    //This method runs after a structure is placed down. In the base structure class it is empty.
    @Inject(method = "afterPlace", at = @At("TAIL"))
    private void onPlaced(WorldGenLevel p_226560_, StructureManager p_226561_, ChunkGenerator p_226562_, RandomSource p_226563_, BoundingBox p_226564_, ChunkPos p_226565_, PiecesContainer p_226566_, CallbackInfo ci){
        Structure struct = (Structure) (Object) this;
        //Explicitly checking for jigsaws should trim down the amount of scanned chunks. All boulders are jigsaws.
        //We also check for the flag in config to cut down on unnecessary scanning.
        if(CTCommonConfig.BOULDER_COMPASS.get() && struct.type() == StructureType.JIGSAW) CompassService.updateArea(p_226560_.getLevel(), p_226560_.getChunk(p_226565_.x,p_226565_.z));
    }
}
