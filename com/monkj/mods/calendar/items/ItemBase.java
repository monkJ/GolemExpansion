/**
 * File: ItemBase.java
 * created: 17.04.2013 - 11:48:28
 * by: RoboMat
 */

package com.monkj.mods.calendar.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import com.monkj.mods.calendar.Calendar;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBase extends Item
{
    /**
     * @param id - Sets the item id.
     * @param itemName - Sets the internal name to "item.itemName";
     */
    public ItemBase(int id, String name)
    {
        super(id);
        setUnlocalizedName(name);
    }

    /**
     * Returns the unlocalized name of the texture with the leading "item."
     * omitted. Use this to load textures.
     *
     * @return The internal name of the item set via setUnlocalizedName(...).
     */
    private String getInternalName()
    {
        return getUnlocalizedName().substring(5);
    }
    
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister) {
		this.itemIcon = par1IconRegister.registerIcon("calendar:itemCalendar");
	}
}
