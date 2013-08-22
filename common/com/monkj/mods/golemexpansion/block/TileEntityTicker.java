package com.monkj.mods.golemexpansion.block;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityTicker extends TileEntity {
    public int interval;
    private int timer=0;

    public TileEntityTicker(int tickInterval){
        this.interval=tickInterval;
    }
    
    @Override
    public void updateEntity(){
        if(++this.timer > this.interval){
            this.timer=0;
            Block b=Block.blocksList[this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)];
            if(b!=null){
                b.updateTick(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.worldObj.rand);
            }
        }
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt){
        super.writeToNBT(nbt);
        nbt.setInteger("TickInterval", this.interval);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt){
        super.readFromNBT(nbt);
        this.interval=nbt.getInteger("TickInterval");
    }
}
