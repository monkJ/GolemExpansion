package com.monkj.mods.golemexpansion.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.monkj.mods.golemexpansion.golem.medium.EntityGlowstoneGolem;
import com.monkj.mods.golemexpansion.golem.medium.EntityRedstoneGolem;

public class BlockGlowingRedstoneAir extends BlockContainer {
    
    public static final int LIGHT=1, REDSTONE=2;
    
    public BlockGlowingRedstoneAir(int par1) {
        super(par1, Material.air);
        this.setBlockBounds(0, 0, 0, 0, 0, 0);
    }
    
    @Override
    public boolean isAirBlock(World world, int x, int y, int z){
        return true;
    }
    
    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z){
        return (world.getBlockMetadata(x, y, z) & 1) * 15;
    }
    
    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side){
        return (world.getBlockMetadata(x, y, z) & 2) * 15;
    }
    
    @Override
    public boolean canProvidePower(){
        return true;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z){
        return null;
    }
    
    @Override
    public boolean isOpaqueCube(){
        return false;
    }
    
    @Override
    public boolean renderAsNormalBlock(){
        return false;
    }
    
    @Override
    public int getRenderType(){
        return -1;
    }
    
    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side){
        return false;
    }
    
    @Override
    public void updateTick(World world, int x, int y, int z, Random rand){
        this.recheckBlockState(world, x, y, z);
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int id){
        this.recheckBlockState(world, x, y, z);
    }
    
    public void recheckBlockState(World world, int x, int y, int z){
        AxisAlignedBB myAABB=AxisAlignedBB.getAABBPool().getAABB(x, y, z, x+1, y+1, z+1);
        List collidingGlowstoneGolems=world.getEntitiesWithinAABB(EntityGlowstoneGolem.class, myAABB);
        List collidingRedstoneGolems=world.getEntitiesWithinAABB(EntityRedstoneGolem.class, myAABB);
        if(collidingGlowstoneGolems.isEmpty() && collidingRedstoneGolems.isEmpty()){
            world.setBlock(x, y, z, 0);
        }else{
            world.setBlockMetadataWithNotify(x, y, z, (collidingGlowstoneGolems.isEmpty()?0:BlockGlowingRedstoneAir.LIGHT) | (collidingRedstoneGolems.isEmpty()?0:BlockGlowingRedstoneAir.REDSTONE), 3);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEntityTicker(10);
    }
}
