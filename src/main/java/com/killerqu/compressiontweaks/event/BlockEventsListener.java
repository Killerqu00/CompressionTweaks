package com.killerqu.compressiontweaks.event;

import appeng.server.services.compass.CompassService;
import com.killerqu.compressiontweaks.CompressionTweaks;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = CompressionTweaks.MODID)
public class BlockEventsListener {
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent e){
        if(e.getState().is(CompressionTweaks.ATTRACTS_COMPASS) && e.getLevel() instanceof ServerLevel level){
            //NOTE: This is because when the event is fired, the block is actually still there and gets caught in the scan.
            //This would result breaking conglomerate ores never clearing the chunk's flag and always be marked for the compass.
            level.getServer().execute(() -> {
                CompassService.notifyBlockChange(level, e.getPos());
            });
        }
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent e){
        //TODO: Add config option (To be able to control whether player-placed blocks are tracked)
        if(e.getLevel() instanceof ServerLevel level && e.getPlacedBlock().is(CompressionTweaks.ATTRACTS_COMPASS)){
            CompassService.notifyBlockChange(level, e.getPos());
        }
    }

}
