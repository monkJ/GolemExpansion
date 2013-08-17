package com.monkj.mods.golemexpansion.golem.simple;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.monkj.mods.golemexpansion.golem.GolemStats;
import com.monkj.mods.golemexpansion.lib.Reference;

public class EntitySandstoneGolem extends EntitySimpleGolem {
    
    public static final GolemStats stats = new GolemStats();
    static {
        stats.maxHealth = 60;
        stats.attackDamageMean = 10f;
        stats.attackDamageStdDev = 1f;
        stats.name = "Sandstone Golem";
        stats.texture = Reference.mobTexture("sandstone_golem");
        stats.droppedItems(new ItemStack(Block.sand, 4));
    }
    
    public EntitySandstoneGolem(World world) {
        super(world);
    }
    
}
