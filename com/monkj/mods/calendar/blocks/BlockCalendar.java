/**
 * File: BlockCalendar.java
 * created: 15.04.2013 - 11:24:20
 * by: RoboMat
 */

package com.monkj.mods.calendar.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.monkj.mods.calendar.Calendar;
import com.monkj.mods.calendar.tileentity.TileEntityCalendar;

public class BlockCalendar extends BlockContainer
{
    public BlockCalendar(int blockId, String unlocalizedName)
    {
        super(blockId, Material.cloth);
        setUnlocalizedName(unlocalizedName);
        setStepSound(soundClothFootstep);
        setHardness(0.8F);	// same hardness as cloth
       
        float minX = 0.08F;
        float minY = 0.1F;
        float minZ = 0.895F;
        float maxX = 0.92F;
        float maxY = 0.935F;
        float maxZ = 1.0F;
        this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * Block right clicked.
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int plyX, float plyY, float plyZ, float par9)
    {
        TileEntityCalendar ent = (TileEntityCalendar) world.getBlockTileEntity(x, y, z);
        return true;
    }

    /**
     * Block left clicked.
     */
    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
    {
        super.onBlockClicked(world, x, y, z, player);
    }

    /* ---------------------- Bounding Box ------------------------------------ */

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int x, int y, int z)
    {
        return null;
    }

    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(par1World, x, y, z);
        return super.getSelectedBoundingBoxFromPool(par1World, x, y, z);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess access, int x, int y, int z)
    {
        int meta = access.getBlockMetadata(x, y, z);
        float minX = 0.0F;
        float minY = 0.0F;
        float minZ = 0.0F;
        float maxX = 1.0F;
        float maxY = 1.0F;
        float maxZ = 1.0F;
        this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);

        /* Entity faces north */
        if (meta == 2)
        {
            minX = 0.08F;
            minY = 0.1F;
            minZ = 0.95F;
            maxX = 0.92F;
            maxY = 0.935F;
            maxZ = 1.0F;
            this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
        }

        /* Entity faces south */
        if (meta == 3)
        {
            minX = 0.08F;
            minY = 0.1F;
            minZ = 0.0F;
            maxX = 0.92F;
            maxY = 0.935F;
            maxZ = 0.050F;
            this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
        }

        /* Entity faces west */
        if (meta == 4)
        {
            minX = 0.95F;
            minY = 0.1F;
            minZ = 0.08F;
            maxX = 1.0F;
            maxY = 0.935F;
            maxZ = 0.92F;
            this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
        }

        /* Entity faces east */
        if (meta == 5)
        {
            minX = 0.0F;
            minY = 0.1F;
            minZ = 0.08F;
            maxX = 0.05F;
            maxY = 0.935F;
            maxZ = 0.92F;
            this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
        }
    }

    /* ------------------------------------------------------------------------ */

    public Icon getBlockTextureFromSideAndMetadata(int side, int meta)
    {
        return Block.planks.getBlockTextureFromSide(side);
    }

    public int getRenderType()
    {
        return -1;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Calendar.instance.getItemCalendar().itemID;
    }

    public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntityCalendar();
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int neighbourId)
    {
        boolean flag = false;
        int meta = world.getBlockMetadata(x, y, z);
        flag = true;

        if (meta == 2 && world.getBlockMaterial(x, y, z + 1).isSolid())
        {
            flag = false;
        }

        if (meta == 3 && world.getBlockMaterial(x, y, z - 1).isSolid())
        {
            flag = false;
        }

        if (meta == 4 && world.getBlockMaterial(x + 1, y, z).isSolid())
        {
            flag = false;
        }

        if (meta == 5 && world.getBlockMaterial(x - 1, y, z).isSolid())
        {
            flag = false;
        }

        if (flag)
        {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
        }

        super.onNeighborBlockChange(world, x, y, z, neighbourId);
    }

    public int idPicked(World par1World, int par2, int par3, int par4)
    {
        return Calendar.instance.getItemCalendar().itemID;
    }


	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		this.blockIcon = par1IconRegister.registerIcon("calendar:blockCalendar");
	}    
}