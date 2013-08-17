package com.monkj.mods.golemexpansion.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.world.World;

public class EntityGolemFireball extends EntitySmallFireball {
    
    public EntityGolemFireball(World world){
        super(world);
    }
    
    public EntityGolemFireball(World par1World,
            EntityLivingBase par2EntityLivingBase, double par3, double par5,
            double par7) {
        super(par1World, par2EntityLivingBase, par3, par5, par7);
    }
    
    public EntityGolemFireball(World par1World, double par2, double par4,
            double par6, double par8, double par10, double par12) {
        super(par1World, par2, par4, par6, par8, par10, par12);
    }
    
    @Override
    public boolean isEntityInvulnerable() {
        return true;
    }
    
}
