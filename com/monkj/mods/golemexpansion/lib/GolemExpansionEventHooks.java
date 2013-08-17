package com.monkj.mods.golemexpansion.lib;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import com.monkj.mods.golemexpansion.GolemExpansion;
import com.monkj.mods.golemexpansion.golem.EntityCustomGolem;
import com.monkj.mods.golemexpansion.golem.GolemRegistry;
import com.monkj.mods.golemexpansion.potion.DamageSourceShatter;
import com.monkj.mods.golemexpansion.potion.PotionFreeze;

public class GolemExpansionEventHooks {
    @ForgeSubscribe
    public void onLvingUpdate(LivingUpdateEvent event){
        if(event.entityLiving.isPotionActive(PotionFreeze.instance)){
            if(event.entityLiving.getActivePotionEffect(PotionFreeze.instance).getDuration() <= 0){
                event.entityLiving.removePotionEffect(PotionFreeze.instance.id);
                return;
            }
            event.entityLiving.motionX=0;
            event.entityLiving.motionY=0;
            event.entityLiving.motionZ=0;
        }   
    }
    
    @ForgeSubscribe
    public void onLivingAttack(LivingAttackEvent event){
        if(event.entityLiving.isPotionActive(PotionFreeze.instance) && !(event.source instanceof DamageSourceShatter)){
            event.entityLiving.attackEntityFrom(DamageSourceShatter.instance(), Math.max((float) (3 * event.ammount), 6));
            event.entityLiving.removePotionEffect(PotionFreeze.instance.id);
        } else if(event.source.getSourceOfDamage()!=null
                && event.source.getSourceOfDamage() instanceof EntityLivingBase
                && ((EntityLivingBase)event.source.getSourceOfDamage()).isPotionActive(PotionFreeze.instance)){
            event.setCanceled(true);
        }
    }
    
    @ForgeSubscribe
    public void onBreakSpeed(PlayerEvent.BreakSpeed event){
        if(event.entityLiving.isPotionActive(PotionFreeze.instance)){
            event.setCanceled(true);
        }
    }
    
    @ForgeSubscribe
    public void onEnderTeleport(EnderTeleportEvent event){
        if(event.entityLiving.isPotionActive(PotionFreeze.instance)){
            event.setCanceled(true);
        }
    }
    
    @ForgeSubscribe
    public void onArrowNock(ArrowNockEvent event){
        if(event.entityLiving.isPotionActive(PotionFreeze.instance)){
            event.setCanceled(true);
        }
    }
    
    /**
     * Listens to an {@link EntityJoinWorldEvent} and responds depending on the
     * type of entity that spawned. This code effectively disables the vanilla
     * iron golem by replacing it with one of our golems if it spawns naturally,
     * i.e. in a village, or dropping the blocks used to build it if it was
     * built manually. Configurable.
     * 
     * @param e the {@link EntityJoinWorldEvent}
     */
    @ForgeSubscribe
    public void onEntityJoinWorld(EntityJoinWorldEvent e) {
        // This code effectively disables the vanilla iron golem by replacing it
        // with one of our golems if it spawns naturally, i.e. in a village, or
        // dropping the blocks used to build it if it was built manually.
        // Configurable.
        if (e.entity instanceof EntityIronGolem && !(e.entity instanceof EntityCustomGolem)) {
            EntityIronGolem theGolem = (EntityIronGolem) e.entity;
            if (!theGolem.isPlayerCreated()) {
                if (GolemExpansion.config.get("Vanilla", "replaceVillageSpawns", true)
                        .getBoolean(true)) {
                    int x, y, z;
                    x = MathHelper.floor_double(theGolem.posX);
                    y = MathHelper.floor_double(theGolem.posY);
                    z = MathHelper.floor_double(theGolem.posZ);
                    e.world.removeEntity(theGolem);
                    GolemRegistry.spawnRandomGolem(e.world, x, y, z);
                }
            } else if (GolemExpansion.config.get("Vanilla", "deleteConstructedGolems", false)
                    .getBoolean(false)) {
                theGolem.entityDropItem(new ItemStack(Block.blockIron, 3), 0F);
                theGolem.entityDropItem(new ItemStack(Block.pumpkin), 0F);
                theGolem.setDead();
            }
        }
    }

    private static GolemExpansionEventHooks instance;
    
    public static GolemExpansionEventHooks instance() {
        if(instance==null)
            instance=new GolemExpansionEventHooks();
        return instance;
    }
}
