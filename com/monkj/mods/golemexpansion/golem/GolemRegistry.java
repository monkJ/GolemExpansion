package com.monkj.mods.golemexpansion.golem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import com.monkj.mods.golemexpansion.GolemExpansion;
import com.monkj.mods.golemexpansion.golem.medium.EntityGlowstoneGolem;
import com.monkj.mods.golemexpansion.golem.medium.EntityIceGolem;
import com.monkj.mods.golemexpansion.golem.medium.EntityNetherrackGolem;
import com.monkj.mods.golemexpansion.golem.medium.EntityRedstoneGolem;
import com.monkj.mods.golemexpansion.golem.simple.EntityClayGolem;
import com.monkj.mods.golemexpansion.golem.simple.EntityDiamondGolem;
import com.monkj.mods.golemexpansion.golem.simple.EntityDirtGolem;
import com.monkj.mods.golemexpansion.golem.simple.EntityEmeraldGolem;
import com.monkj.mods.golemexpansion.golem.simple.EntityGlassGolem;
import com.monkj.mods.golemexpansion.golem.simple.EntityGoldGolem;
import com.monkj.mods.golemexpansion.golem.simple.EntityIronGolem;
import com.monkj.mods.golemexpansion.golem.simple.EntityLapisGolem;
import com.monkj.mods.golemexpansion.golem.simple.EntityObsidianGolem;
import com.monkj.mods.golemexpansion.golem.simple.EntitySandstoneGolem;
import com.monkj.mods.golemexpansion.golem.simple.EntityStoneGolem;
import com.monkj.mods.golemexpansion.golem.simple.EntityNetherbrickGolem;
import com.monkj.mods.golemexpansion.golem.simple.EntityQuartzGolem;
import com.monkj.mods.golemexpansion.util.BlockWithMeta;

import cpw.mods.fml.common.registry.EntityRegistry;

/**
 * A list of all golems with their respective construction multiblock recipe.
 * Also handles spawning golems when asked to.
 * 
 * @author Solonarv
 * 
 */
public class GolemRegistry {
    /**
     * A list of all the {@link GolemRegistration}s
     */
    protected static List<GolemRegistration> entries = new ArrayList<GolemRegistration>();
    /**
     * The GolemRegistry has its own RNG
     */
    protected static Random                  rand    = new Random();
    /**
     * The next ID available, for {@link EntityRegistry}.registerModEntity.
     */
    private static int                       nextID  = 1;
    
    /**
     * Registers a new golem with the golemRegistry. This is the main method for
     * doing so: all overloads ultimately delegate to this one. It allows the
     * most control of all.
     * 
     * @param golemClass The class of the golem to register.
     * @param upperBody The BlockWithMeta that is the golem's upper body
     * @param lowerBody The BlockWithMeta that is the golem's lower body
     * @param shoulders The BlockWithMeta that is the golem's shoulders
     * @param arms The BlockWithMeta that is the golem's arms
     * @param legs The BlockWithMeta that is the golem's legs
     */
    public static void registerGolem(
            Class<? extends EntityCustomGolem> golemClass,
            BlockWithMeta upperBody, BlockWithMeta lowerBody,
            BlockWithMeta shoulders, BlockWithMeta arms, BlockWithMeta legs) {
        entries.add(new GolemRegistration(golemClass, upperBody, lowerBody,
                shoulders, arms, legs));
        EntityRegistry.registerModEntity(golemClass, golemClass.getName(),
                nextID++, GolemExpansion.instance, 40, 1, true);
    }
    
    /**
     * Wrapper for registerGolem(golemClass, BlockWithMeta, ...) that lets one
     * use just plain old Block instances. Params are the same as wrapped
     * method.
     */
    public static void registerGolem(
            Class<? extends EntityCustomGolem> golemClass, Block upperBody,
            Block lowerBody, Block shoulders, Block arms, Block legs) {
        registerGolem(golemClass, new BlockWithMeta(upperBody),
                new BlockWithMeta(lowerBody), new BlockWithMeta(shoulders),
                new BlockWithMeta(arms), new BlockWithMeta(legs));
    }
    
    /**
     * Another wrapper for registerGolem(golemClass, BlockWithMeta, ...) that
     * lets one specify a shape from an enum and a material (a BlockWithMeta
     * that the golem is built out of); generally more readable and shorter that
     * the full invocation.
     * 
     * @param golemClass The class of the golem to register
     * @param mat A {@link BlockWithMeta} the golem is built out of
     * @param shape A {@link GolemShapes} the golem is built in
     */
    public static void registerGolem(
            Class<? extends EntityCustomGolem> golemClass, BlockWithMeta mat,
            GolemShapes shape) {
        switch (shape) {
            case DEFAULT:
                registerGolem(golemClass, mat, mat, null, mat, null);
                break;
            case FULL:
                registerGolem(golemClass, mat, mat, mat, mat, mat);
                break;
            case PILLAR:
                registerGolem(golemClass, mat, mat, null, null, null);
                break;
            default:
                return;
                
        }
    }
    
    /**
     * A wrapper for registerGolem(Class golemClass, {@link BlockWithMeta} mat,
     * {@link GolemShapes} shape) that takes a {@link Block} as material
     * instead. Params are the same as wrapped method.
     */
    public static void registerGolem(
            Class<? extends EntityCustomGolem> golemClass, Block mat,
            GolemShapes shape) {
        registerGolem(golemClass, new BlockWithMeta(mat), shape);
    }
    
    /**
     * Finds the first {@link GolemRegistration} that matches at the specified
     * coords.
     * 
     * @param world The world to check in.
     * @param x x coord to check at
     * @param y y coord to check at
     * @param z z coord to check at
     * @return The first matching {@link GolemRegistration} if found, null
     *         otherwise.
     */
    public static GolemRegistration findMatch(World world, int x, int y, int z) {
        for (GolemRegistration gr : entries) {
            if (gr.checkAt(world, x, y, z, true)) { return gr; }
        }
        return null;
    }
    
    /**
     * Attempts to spawn the golem constructed at (x,y,z) in world.
     * 
     * @param world The world to check in.
     * @param x x coord to check at
     * @param y y coord to check at
     * @param z z coord to check at
     * @return The spawned golem if successful, null otherwise
     */
    public static EntityCustomGolem trySpawn(World world, int x, int y, int z) {
        GolemRegistration f = findMatch(world, x, y, z);
        if (f != null) { return f.spawn(world, x, y, z); }
        return null;
    }
    
    /**
     * Spawns a random non-advanced golem. Currently used to replace vanilla
     * Iron Golems spawned in villages.
     * 
     * @param world The world to spawn in
     * @param x x coord to spawn at
     * @param y y coord to spawn at
     * @param z z coord to spawn at
     */
    public static void spawnRandomGolem(World world, int x, int y, int z) {
        ArrayList<GolemRegistration> dumbGolems = new ArrayList<GolemRegistration>();
        for (GolemRegistration gr : entries) {
            if (!gr.smart) {
                dumbGolems.add(gr);
            }
        }
        int i = rand.nextInt(dumbGolems.size());
        dumbGolems.get(i).spawn(world, x, y, z);
    }
    
    /**
     * Registers all golems added by the standalone mod
     */
    public static final void registerGolems() {
        // Register all OUR golems with the golemRegistry
        GolemRegistry.registerGolem(EntityIronGolem.class, Block.blockIron, GolemShapes.DEFAULT);
        GolemRegistry.registerGolem(EntityDirtGolem.class, Block.dirt, GolemShapes.DEFAULT);
        GolemRegistry.registerGolem(EntitySandstoneGolem.class, Block.sandStone, GolemShapes.DEFAULT);
        GolemRegistry.registerGolem(EntityStoneGolem.class, Block.stone, GolemShapes.DEFAULT);
        GolemRegistry.registerGolem(EntityClayGolem.class, Block.blockClay, GolemShapes.PILLAR);
        GolemRegistry.registerGolem(EntityEmeraldGolem.class, Block.blockEmerald, GolemShapes.DEFAULT);
        GolemRegistry.registerGolem(EntityGoldGolem.class, Block.blockGold, GolemShapes.DEFAULT);
        GolemRegistry.registerGolem(EntityLapisGolem.class, Block.blockLapis, GolemShapes.DEFAULT);
        GolemRegistry.registerGolem(EntityGlassGolem.class, Block.glass, GolemShapes.DEFAULT);
        GolemRegistry.registerGolem(EntityObsidianGolem.class, Block.obsidian, GolemShapes.DEFAULT);
        GolemRegistry.registerGolem(EntityDiamondGolem.class, Block.blockDiamond, GolemShapes.DEFAULT);
        GolemRegistry.registerGolem(EntityIceGolem.class, Block.ice, GolemShapes.DEFAULT);
        GolemRegistry.registerGolem(EntityNetherrackGolem.class, Block.netherrack, Block.netherrack, Block.fire, Block.netherrack, null);
        GolemRegistry.registerGolem(EntityRedstoneGolem.class, Block.blockRedstone, GolemShapes.DEFAULT);
        GolemRegistry.registerGolem(EntityGlowstoneGolem.class, Block.glowStone, GolemShapes.DEFAULT);
        GolemRegistry.registerGolem(EntityNetherbrickGolem.class, Block.netherBrick, GolemShapes.DEFAULT);
        GolemRegistry.registerGolem(EntityQuartzGolem.class, Block.blockNetherQuartz, GolemShapes.DEFAULT);
    }
    
    public static List<Class<? extends EntityCustomGolem>> getGolemClasses() {
        List<Class<? extends EntityCustomGolem>> ret = new ArrayList<Class<? extends EntityCustomGolem>>();
        for (GolemRegistration reg : entries) {
            ret.add(reg.golemClass);
        }
        return ret;
    }
}
