package com.monkj.mods.golemexpansion.golem.medium;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAILookAtVillager;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.monkj.mods.golemexpansion.golem.EntityCustomGolem;
import com.monkj.mods.golemexpansion.golem.GolemStats;
import com.monkj.mods.golemexpansion.lib.Reference;
import com.monkj.mods.golemexpansion.util.EntityGolemFireball;

public class EntityNetherrackGolem extends EntityCustomGolem implements IRangedAttackMob {
    public EntityNetherrackGolem(World world) {
        super(world);
        this.tasks.taskEntries.clear();
        this.tasks.addTask(1, new EntityAIArrowAttack(this, this.getAIMoveSpeed(), 60, 64));
        this.tasks.addTask(2, new EntityAIMoveThroughVillage(this, .9d, true));
        this.tasks.addTask(3, new EntityAILookAtVillager(this));
        this.tasks.addTask(4, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
    }
    
    public static final GolemStats stats                 = new GolemStats();
    static {
        stats.maxHealth = 60;
        stats.attackDamageMean = 10f;
        stats.attackDamageStdDev = 1f;
        stats.name = "Netherrack Golem";
        stats.texture = Reference.mobTexture("netherrack_golem");
        stats.droppedItems(new ItemStack(Block.netherrack, 4));
    }
    
    private int fireballCharges       = 64;
    private int fireballRechargeTimer = 60;
    private boolean burning;
    
    @Override
    public boolean attackEntityAsMob(Entity e) {
        if (this.fireballCharges<=0) {
            return super.attackEntityAsMob(e);
        }
        else return false;
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.fireballRechargeTimer > 0) {
            this.fireballRechargeTimer--;
        } else if (this.fireballCharges < 64) {
            // Normal distro, I like these!
            this.fireballCharges += MathHelper.clamp_int((int) Math.round(this.rand.nextGaussian() * 2 + 3), 1, 5);
            this.fireballRechargeTimer = this.rand.nextInt(200)-this.rand.nextInt(200) + 400;
        }
        
        //this.setPlayerCreated(false);
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("fireballCharges", this.fireballCharges);
        nbt.setInteger("fireballRechargeTimer", this.fireballRechargeTimer);
    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.fireballCharges = nbt.getInteger("fireballCharges");
        this.fireballRechargeTimer = nbt.getInteger("fireballRechargeTimer");
    }
    
    protected void dealFireDamage(int i){}

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distSq) {
        double x = target.posX - this.posX;
        double y = target.posY - this.posY;
        double z = target.posZ - this.posZ;
        for (int i = 0; i < 3; i++) {
            EntityGolemFireball egf = new EntityGolemFireball(this.worldObj,
                    this.posX + this.rand.nextGaussian() * .1D,
                    this.posY + 2.5D + this.rand.nextGaussian() * .1D,
                    this.posZ + this.rand.nextGaussian() * .1D,
                    x + this.rand.nextGaussian() * distSq * 5D,
                    y + this.rand.nextGaussian() * distSq * 5D - 2D,
                    z + this.rand.nextGaussian() * distSq * 5D);
            egf.shootingEntity=this;
            this.worldObj.spawnEntityInWorld(egf);
        }
    }
    
    @Override
    public boolean attackEntityFrom(DamageSource src, float dmg){
        // Make this golem immune to any kind of fire
        // Also, he reignites on burn tick, so he'll burn forever omghax!
        if(src==DamageSource.inFire || src==DamageSource.onFire || src==DamageSource.lava){
            this.setFire(10); // Will burn for 1 day straight
            return false;
        }else{
            return super.attackEntityFrom(src, dmg);
        }
    }
}
