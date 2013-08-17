package com.monkj.mods.golemexpansion.golem.simple;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.monkj.mods.golemexpansion.golem.GolemStats;
import com.monkj.mods.golemexpansion.lib.Reference;

public class EntityNetherbrickGolem extends EntitySimpleGolem {
    
    public static final GolemStats stats = new GolemStats();
    static {
        // Initialize stats
        stats.maxHealth = 75;
        stats.attackDamageMean = 12f;
        stats.attackDamageStdDev = 1f;
        stats.name = "Netherbrick Golem";
        stats.texture = Reference.mobTexture("netherbrick_golem");
        stats.droppedItems(new ItemStack(Block.netherBrick, 3));
    }
    
    public EntityNetherbrickGolem(World world) {
        super(world);
    }
    
}


