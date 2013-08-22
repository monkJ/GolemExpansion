package com.monkj.mods.golemexpansion.golem.simple;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.monkj.mods.golemexpansion.golem.GolemStats;
import com.monkj.mods.golemexpansion.lib.Reference;

public class EntityQuartzGolem extends EntitySimpleGolem {
    public static final GolemStats stats = new GolemStats();
    static {
        stats.maxHealth = 65;
        stats.attackDamageMean = 14;
        stats.attackDamageStdDev = 1;
        stats.name = "Quartz Golem";
        stats.texture = Reference.mobTexture("quartz_golem");
        stats.droppedItems(new ItemStack(Item.netherQuartz, 2));
    }
    
    public EntityQuartzGolem(World world) {
        super(world);
    }
}


