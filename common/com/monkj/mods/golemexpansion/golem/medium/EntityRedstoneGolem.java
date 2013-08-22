package com.monkj.mods.golemexpansion.golem.medium;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.monkj.mods.golemexpansion.block.BlockGlowingRedstoneAir;
import com.monkj.mods.golemexpansion.block.ModBlocks;
import com.monkj.mods.golemexpansion.golem.EntityCustomGolem;
import com.monkj.mods.golemexpansion.golem.GolemStats;
import com.monkj.mods.golemexpansion.lib.Reference;

public class EntityRedstoneGolem extends EntityCustomGolem {
    
    public static final GolemStats stats = new GolemStats();
    
    static {
        stats.maxHealth = 20;
        stats.attackDamageMean = 5f;
        stats.attackDamageStdDev = 1f;
        stats.name = "Redstone Golem";
        stats.texture = Reference.mobTexture("redstone_golem");
        stats.droppedItems(new ItemStack(Item.redstone, 5));
    }
    
    public int lastX, lastY, lastZ;
    
    public EntityRedstoneGolem(World world) {
        super(world);
    }
    
    @Override
    public void onLivingUpdate(){
        super.onLivingUpdate();
        int mx=MathHelper.floor_double(this.posX);
        int my=MathHelper.floor_double(this.posY);
        int mz=MathHelper.floor_double(this.posZ);
        if(mx!=this.lastX || my!=this.lastY || mz!=this.lastZ){
            this.lastX=mx; this.lastY=my; this.lastZ=mz;
            for(int x=mx; x<mx+2; x++) for(int y=my; y<my+3; y++) for(int z=mz; z<mz+2; z++)
                if(this.worldObj.getBlockId(x, y, z) == ModBlocks.glowingRedstoneAir.blockID){
                    this.worldObj.setBlockMetadataWithNotify(x, y, z, this.worldObj.getBlockMetadata(x, y, z) | BlockGlowingRedstoneAir.REDSTONE, 3);
                }else if(this.worldObj.isAirBlock(x, y, z)){
                    this.worldObj.setBlock(x, y, z, ModBlocks.glowingRedstoneAir.blockID, BlockGlowingRedstoneAir.REDSTONE, 3);
                }
        }
    }
    
    @Override
    public void onDeath(DamageSource dmg){
        super.onDeath(dmg);
        int mx=MathHelper.floor_double(this.posX);
        int my=MathHelper.floor_double(this.posY);
        int mz=MathHelper.floor_double(this.posZ);
        for(int x=mx; x<mx+2; x++) for(int y=my; y<my+3; y++) for(int z=mz; z<mz+2; z++)
            if(this.worldObj.getBlockId(x, y, z)==ModBlocks.glowingRedstoneAir.blockID){
                this.worldObj.setBlockToAir(x, y, z);
            }
    }
    
}
