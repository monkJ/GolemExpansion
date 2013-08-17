package com.monkj.mods.golemexpansion.golem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;


/**
 * The inventory of a golem (this is also a base class for other smart golems'
 * inventories. As smart golems are currently NYI, this is subject to heavy
 * change.
 * 
 * @author Solonarv
 * 
 */
public abstract class InventoryGolem extends EntityCustomGolem implements
        IInventory {
    public InventoryGolem(World world) {
        super(world);
    }
    
    /**
     * The inventory per se
     */
    protected ItemStack[] invBuffer = new ItemStack[18];
    /**
     * The golem's current 'working' slot index.
     */
    protected ItemStack   activeStack;
    
    @Override
    public int getSizeInventory() {
        return invBuffer.length;
    }
    
    @Override
    public ItemStack getStackInSlot(int i) {
        return this.invBuffer[i];
    }
    
    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if (this.invBuffer[slot] != null) {
            ItemStack itemstack;
            
            if (this.invBuffer[slot].stackSize <= amount) {
                itemstack = this.invBuffer[slot];
                this.invBuffer[slot] = null;
                return itemstack;
            } else {
                itemstack = this.invBuffer[slot].splitStack(amount);
                
                if (this.invBuffer[slot].stackSize == 0) {
                    this.invBuffer[slot] = null;
                }
                
                return itemstack;
            }
        } else {
            return null;
        }
    }
    
    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if (this.invBuffer[slot] != null) {
            ItemStack itemstack = this.invBuffer[slot];
            this.invBuffer[slot] = null;
            return itemstack;
        } else {
            return null;
        }
    }
    
    @Override
    public void setInventorySlotContents(int slot, ItemStack is) {
        this.invBuffer[slot] = is;
        
        if (is != null && is.stackSize > this.getInventoryStackLimit()) {
            is.stackSize = this.getInventoryStackLimit();
        }
    }
    
    /**
     * Called when an the contents of an Inventory change, usually
     */
    @Override
    public void onInventoryChanged() {
    }
    
    /**
     * Do not make give this method the name canInteractWith because it clashes
     * with Container
     */
    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
        return this.isDead ? false : par1EntityPlayer
                .getDistanceSqToEntity(this) <= 64.0D;
    }
    
    @Override
    public void openChest() {
    }
    
    @Override
    public void closeChest() {
    }
    
    /**
     * Returns true if automation is allowed to insert the given stack (ignoring
     * stack size) into the given slot.
     */
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack is) {
        return true;
    }
    
    @Override
    public String getInvName() {
        return this.stats().name;
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public boolean isInvNameLocalized() {
        return true;
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setCompoundTag("activeStack",
                this.activeStack.writeToNBT(new NBTTagCompound()));
        for (int i = 0; i < this.invBuffer.length; i++) {
            nbt.setCompoundTag(String.valueOf(i),
                    this.invBuffer[i].writeToNBT(new NBTTagCompound()));
        }
    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.activeStack = ItemStack.loadItemStackFromNBT(nbt
                .getCompoundTag("activeStack"));
        for (int i = 0; i < this.invBuffer.length; i++) {
            this.invBuffer[i] = ItemStack.loadItemStackFromNBT(nbt
                    .getCompoundTag(String.valueOf(i)));
        }
    }
}
