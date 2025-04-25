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
            //This would result in breaking conglomerate ores never clearing the chunk's flag and always be marked for the compass.
            level.getServer().execute(() -> {
                CompassService.notifyBlockChange(level, e.getPos());
            });
        }
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent e){
        //I had a note here saying to add configs to filter out player placed blocks.
        //But i don't know how i'd stop a player from placing two, then breaking one.
        if(e.getLevel() instanceof ServerLevel level && e.getPlacedBlock().is(CompressionTweaks.ATTRACTS_COMPASS)){
            CompassService.notifyBlockChange(level, e.getPos());
        }
    }

}
