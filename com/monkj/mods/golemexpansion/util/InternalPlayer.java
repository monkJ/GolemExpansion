package com.monkj.mods.golemexpansion.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class InternalPlayer extends EntityPlayer {
    
    public InternalPlayer(World w, double x, double y, double z) {
        super(w, "[GolemExpansion]");
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        
    }
    
    @Override
    public void sendChatToPlayer(ChatMessageComponent chatmessagecomponent) {
    }
    
    @Override
    public boolean canCommandSenderUseCommand(int i, String s) {
        return false;
    }
    
    @Override
    public ChunkCoordinates getPlayerCoordinates() {
        return null;
    }
    
}
