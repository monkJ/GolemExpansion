package com.monkj.mods.golemexpansion.golem.simple;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.monkj.mods.golemexpansion.golem.GolemStats;
import com.monkj.mods.golemexpansion.lib.Reference;

public class EntityClayGolem extends EntitySimpleGolem {
    public static final GolemStats stats = new GolemStats();
    static {
        stats.maxHealth = 110;
        stats.attackDamageMean = 8f;
        stats.attackDamageStdDev = 2.3f;
        stats.name = "Clay Golem";
        stats.texture = Reference.mobTexture("clay_golem");
        stats.droppedItems(new ItemStack(Item.clay, 5));
    }
    
    public EntityClayGolem(World world) {
        super(world);
    }
}
