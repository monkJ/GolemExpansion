package com.monkj.mods.golemexpansion.localization;

import com.monkj.mods.golemexpansion.item.ModItems;
import com.monkj.mods.golemexpansion.potion.PotionFreeze;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class Localization {
    
    public static void registerNames(){
        LanguageRegistry.instance().addStringLocalization(PotionFreeze.instance.getName(), "Freeze");
        
        // Temporary -- will be changed once I have more items
        LanguageRegistry.addName(ModItems.golemExpansionUniversal, "§4Tome of Ressurection");
    }
    
}
