package com.monkj.mods.golemexpansion.golem;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * GolemExpansion
 * 
 * ABC of all our golems. Defines stats and implementation of gaussian-random
 * attack damage. Extends {@link EntityIronGolem} for convenience: most code
 * would be copypasted anyway; it also lets me use the same model without any
 * fuss.
 * 
 * @author monkJ
 * @license 
 */
public abstract class EntityCustomGolem extends EntityIronGolem {
    
    /**
     * Overridden by all non-abstract subclasses: Holds this golem's maximum
     * health, attack damage curve parameters, and drops.
     */
    private static GolemStats stats;
    /**
     * Cached reference to the stats from this golem's actual class (instead of
     * this ABC), needed because attributes are not virtual (unlike methods)
     */
    private GolemStats        actualStats = null;
    
    // Make private attackTimer from superclass visible
    protected int             attackTimer;
    
    public EntityCustomGolem(World world) {
        super(world);
        // func_94058_c(this.getStats().name);
    }
    
    /**
     * Utility function that makes sure actualStats is initialized, then returns
     * it. Uses reflection to do a virtual access instead of static access to
     * private static {@link GolemStats} stats. Prevents NPEs from occuring as
     * long as reflection does not fail.
     * 
     * @return
     */
    public GolemStats stats() {
        if (this.actualStats == null) {
            try {
                this.actualStats = (GolemStats) this.getClass()
                        .getField("stats").get(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.actualStats;
    }
    
    /**
     * Identical to super method except for amount of damage done: It's
     * gaussian-random, parametrized in stats.attackDamageMean and
     * stats.attackDamageStdDev
     */
    @Override
    public boolean attackEntityAsMob(Entity par1Entity) {
        attackTimer = 10;
        worldObj.setEntityState(this, (byte) 4);
        boolean flag = par1Entity.attackEntityFrom(
                DamageSource.causeMobDamage(this), this.getAttackStrength());
        
        if (flag) {
            par1Entity.motionY += 0.4000000059604645D;
        }
        
        playSound("mob.irongolem.throw", 1.0F, 1.0F);
        return flag;
    }
    
    /**
     * Generates the gaussian-random attack damage using Random.nextGaussian
     * 
     * @return randomized attack damage
     */
    public final float getAttackStrength() {
        return this.stats().attackDamageMean + ((float) (rand.nextGaussian()))
                * this.stats().attackDamageStdDev;
    };
    
    /**
     * @return whether or not this golem is smart
     */
    public static boolean isSmart() {
        return false;
    }
    
    /**
     * Safe access of the golem's name
     * 
     * @return the golem's name
     */
    public final String getName() {
        return this.stats().name;
    }
    
    /**
     * Updates the mob's attributes, probably gets called on init.
     */
    @Override
    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111267_a)
                .func_111128_a(this.stats().maxHealth);
    }
    
    /**
     * Drops what is specified in this golem's drop list.
     * super.dropFewItems(...) deliberately not called, as I don't want to drop
     * iron ingots and a rose for all the golems.
     */
    @Override
    public final void dropFewItems(boolean recentlyHit, int lootingLevel) {
        for (ItemStack is : this.stats().droppedItems) {
            entityDropItem(is.copy(), 0F);
        }
    }
    
}
