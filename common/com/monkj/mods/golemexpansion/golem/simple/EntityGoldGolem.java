package com.monkj.mods.golemexpansion.golem.simple;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.monkj.mods.golemexpansion.golem.GolemStats;
import com.monkj.mods.golemexpansion.lib.Reference;

public class EntityGoldGolem extends EntitySimpleGolem {
    public static final GolemStats stats = new GolemStats();
    static {
        stats.maxHealth = 70;
        stats.attackDamageMean = 16;
        stats.attackDamageStdDev = 1;
        stats.name = "Gold Golem";
        stats.texture = Reference.mobTexture("gold_golem");
        stats.droppedItems(new ItemStack(Item.ingotGold, 2));
    }
    
    public EntityGoldGolem(World world) {
        super(world);
    }
}
