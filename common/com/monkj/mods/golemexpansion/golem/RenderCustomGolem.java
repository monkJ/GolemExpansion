package com.monkj.mods.golemexpansion.golem;

import net.minecraft.client.renderer.entity.RenderIronGolem;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderCustomGolem extends RenderIronGolem {
    
    protected ResourceLocation func_110775_a(Entity par1Entity) {
        return this.func_110898_a((EntityCustomGolem) par1Entity);
    }
    
    protected ResourceLocation func_110898_a(EntityCustomGolem theGolem) {
        return theGolem.stats().texture;
    }
}
