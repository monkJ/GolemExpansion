package com.monkj.mods.golemexpansion.golem.simple;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.monkj.mods.golemexpansion.golem.GolemStats;
import com.monkj.mods.golemexpansion.lib.Reference;

public class EntityStoneGolem extends EntitySimpleGolem {
    public static final GolemStats stats = new GolemStats();
    static {
        stats.maxHealth = 110;
        stats.attackDamageMean = 12f;
        stats.attackDamageStdDev = 1f;
        stats.name = "Stone Golem";
        stats.texture = Reference.mobTexture("stone_golem");
        stats.droppedItems(new ItemStack(Block.cobblestone, 2));
    }
    
    public EntityStoneGolem(World world) {
        super(world);
    }
    
}
