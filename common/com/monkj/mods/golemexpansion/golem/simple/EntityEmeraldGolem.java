package com.monkj.mods.golemexpansion.golem.simple;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.monkj.mods.golemexpansion.golem.GolemStats;
import com.monkj.mods.golemexpansion.lib.Reference;

public class EntityEmeraldGolem extends EntitySimpleGolem {
    public static final GolemStats stats = new GolemStats();
    static {
        stats.maxHealth = 100;
        stats.attackDamageMean = 16f;
        stats.attackDamageStdDev = 0f;
        stats.name = "Emerald Golem";
        stats.texture = Reference.mobTexture("emerald_golem");
        stats.droppedItems(new ItemStack(Item.emerald, 2));
    }
    
    public EntityEmeraldGolem(World world) {
        super(world);
    }
}
