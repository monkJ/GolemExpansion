package com.monkj.mods.golemexpansion;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import net.minecraft.potion.Potion;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;

import com.monkj.mods.golemexpansion.block.TileEntityTicker;
import com.monkj.mods.golemexpansion.golem.GolemRegistry;
import com.monkj.mods.golemexpansion.item.ModItems;
import com.monkj.mods.golemexpansion.lib.GolemExpansionEventHooks;
import com.monkj.mods.golemexpansion.lib.Reference;
import com.monkj.mods.golemexpansion.localization.Localization;
import com.monkj.mods.golemexpansion.potion.PotionFreeze;
import com.monkj.mods.golemexpansion.proxy.CommonProxy;
import com.monkj.mods.golemexpansion.util.EntityGolemFireball;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * GolemExpansion
 * 
 * The main mod class
 * 
 * @author monkJ
 * @license 
 * 
 */
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class GolemExpansion {
    
    @Instance(Reference.MOD_ID)
    public static GolemExpansion    instance;
    
    /**
     * A proxy that client-/server-only stuff can be delegated to.
     */
    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.COMMON_PROXY)
    public static CommonProxy   proxy;
    
    public static Configuration config;
    
    /**
     * Initialize all my stuff: items, config, golems
     * 
     * @param event an {@link FMLPreInitializationEvent} containing some info
     */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        ModItems.registerItems();
        GolemRegistry.registerGolems();
        EntityRegistry.registerModEntity(EntityGolemFireball.class,
                EntityGolemFireball.class.getName(), 0, this, 40, 1, true);
        GameRegistry.registerTileEntity(TileEntityTicker.class, "TileEntityTicker");
        try {
            for(Field f: Potion.class.getDeclaredFields()){
                f.setAccessible(true);
                if(f.getName().equals("potionTypes") || f.getName().equals("field_76425_a")){
                    Field modifiers=Field.class.getDeclaredField("modifiers");
                    modifiers.setAccessible(true);
                    modifiers.setInt(f, f.getModifiers() & ~Modifier.FINAL);
                    Potion[] oldPotionTypes=(Potion[]) f.get(null);
                    Potion[] newPotionTypes=Arrays.copyOf(oldPotionTypes, 256);
                    f.set(null, newPotionTypes);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @EventHandler
    public void load(FMLInitializationEvent event) {
        proxy.registerRenderer();
        PotionFreeze.init();
        
        MinecraftForge.EVENT_BUS.register(GolemExpansionEventHooks.instance());
       Localization.registerNames();
    }
    
    /**
     * Interactions with other mods, eventually
     * 
     * @param event an {@link FMLPostInitializationEvent} containing some info
     */
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {}
}
