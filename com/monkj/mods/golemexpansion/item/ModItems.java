package com.monkj.mods.golemexpansion.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.monkj.mods.golemexpansion.GolemExpansion;
import com.monkj.mods.golemexpansion.lib.Reference;

import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Proxy of sorts that handles all the items I need to create.
 * 
 * @author monkJ
 * @license 
 * 
 */
public class ModItems {
    
    public static final int ItemGWId = GolemExpansion.config.getItem("GolemExpansionUniversal", 5000).getInt();
    
    /**
     * tomeOfRessurection
     */
    public static ItemGolemExpansionUniversal golemExpansionUniversal = (ItemGolemExpansionUniversal) new ItemGolemExpansionUniversal(ItemGWId).setMaxStackSize(1)
            .setCreativeTab(CreativeTabs.tabMisc).setUnlocalizedName("tomeOfRessurection").func_111206_d(Reference.texture("tomeOfRessurection").toString());
    /**
     * Register my items with the game and make them craftable
     */
    public static void registerItems() {
        GameRegistry.addRecipe(golemExpansionUniversal.stack(0),new Object[] {"grg", "rpr", "grg",
            'g', Item.glowstone,
            'r', Item.blazePowder,
            'p', Item.paper });
        GameRegistry.addRecipe(golemExpansionUniversal.stack(0),new Object[] {"grg", "rpr", "grg",
            'r', Item.glowstone,
            'g', Item.blazePowder,
            'p', Item.paper });
    }
}
