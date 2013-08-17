package com.monkj.mods.golemexpansion.golem.simple;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.monkj.mods.golemexpansion.golem.GolemStats;
import com.monkj.mods.golemexpansion.lib.Reference;

public class EntityDirtGolem extends EntitySimpleGolem {
    
    public static final GolemStats stats = new GolemStats();
    static {
        // Initialize stats
        stats.maxHealth = 15;
        stats.attackDamageMean = 6f;
        stats.attackDamageStdDev = 1.2f;
        stats.name = "Dirt Golem";
        stats.texture = Reference.mobTexture("dirt_golem");
        stats.droppedItems(new ItemStack(Block.dirt, 3));
    }
    
    public EntityDirtGolem(World world) {
        super(world);
    }
    
}
