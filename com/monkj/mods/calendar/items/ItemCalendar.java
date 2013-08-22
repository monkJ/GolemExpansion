/**
 * File: ItemCalendar.java
 * created: 10.01.2013 - 14:43:49
 * by: RoboMat
 */

package com.monkj.mods.calendar.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.monkj.mods.calendar.Calendar;

public class ItemCalendar extends ItemBase
{
    public ItemCalendar(int i, String name)
    {
        super(i, name);
        maxStackSize = 64;
        setCreativeTab(CreativeTabs.tabTools);
    }

	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int sideOfTheBlock, float par8, float par9, float par10)
    {
        /* Can only be placed on solid blocks */
        if (!world.getBlockMaterial(x, y, z).isSolid())
        {
            return false;
        }

        /* SideOfTheBlock:
         * 0 = -y
         * 1 = +y
         * 2 = -z
         * 3= +z
         * 4= -x
         * 5= +x */
        if (sideOfTheBlock == 0)
        {
            return false;
        }
        else if (sideOfTheBlock == 1)
        {
            return false;
        }
        else if (sideOfTheBlock == 2)
        {
            z--;
        }
        else if (sideOfTheBlock == 3)
        {
            z++;
        }
        else if (sideOfTheBlock == 4)
        {
            x--;
        }
        else if (sideOfTheBlock == 5)
        {
            x++;
        }

        world.setBlock(x, y, z, Calendar.getBlockCalendar().blockID, sideOfTheBlock, 2);
        stack.stackSize--;
        return true;
    }
}
