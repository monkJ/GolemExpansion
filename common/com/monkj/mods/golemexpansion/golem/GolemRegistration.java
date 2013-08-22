package com.monkj.mods.golemexpansion.golem;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import com.monkj.mods.golemexpansion.util.BlockWithMeta;
import com.monkj.mods.golemexpansion.util.TransactionDeleteBlocks;

/**
 * A single entry in the golem registry. Stores the golem's in-world
 * construction schematic and is able to create and spawn a golem.
 * 
 * @author monkJ
 * 
 */
public class GolemRegistration {
    
    /**
     * The blocks that the golem is built out of. null means that the block at
     * that position does not matter.
     */
    BlockWithMeta upperBody, lowerBody, shoulders, arms, legs;
    protected Class<? extends EntityCustomGolem> golemClass;
    public final boolean smart;
    
    /**
     * Full constructor: all other constructors delegate to this one. Also
     * precomputes smartness assertion via reflection.
     * 
     * @param golemClass The class of the golem this registration is for
     * @param upperBody The block used as the golem's uppper body
     * @param lowerBody The block used as the golem's lower body
     * @param shoulders The block used as the golem's shoulders
     * @param arms The block used as the golem's arms
     * @param legs The block used as the golem's legs
     */
    public GolemRegistration(Class<? extends EntityCustomGolem> golemClass,
            BlockWithMeta upperBody, BlockWithMeta lowerBody,
            BlockWithMeta shoulders, BlockWithMeta arms, BlockWithMeta legs) {
        this.golemClass = golemClass;
        this.upperBody = upperBody != null ? upperBody
                : new BlockWithMeta(null);
        this.lowerBody = lowerBody != null ? lowerBody
                : new BlockWithMeta(null);;
        this.shoulders = shoulders != null ? shoulders
                : new BlockWithMeta(null);;
        this.arms = arms != null ? arms : new BlockWithMeta(null);;
        this.legs = legs != null ? legs : new BlockWithMeta(null);;
        boolean temp = false;
        try {
            temp = (Boolean) golemClass.getMethod("isSmart").invoke(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            smart = temp;
        }
    }
    
    /**
     * Checks if a a valid multiblock for this golem is constructed with its
     * head at coords (x,y,z).
     * 
     * @param world The {@link World} to check in
     * @param x The x coord to check at
     * @param y The y coord to check at
     * @param z The z coord to check at
     * @param clearShape Whether or not to remove the affected blocks on
     *        successful match
     * @return Whether or not a golem is correctly built at the specified
     *         location
     */
    public boolean checkAt(World world, int x, int y, int z, boolean clearShape) {
        int headID = world.getBlockId(x, y, z);
        TransactionDeleteBlocks remove = clearShape ? new TransactionDeleteBlocks()
                : null;
        if (headID == Block.pumpkinLantern.blockID || !smart
                && headID == Block.pumpkin.blockID) {
            if (upperBody.isAt(world, x, y - 1, z, remove)
                    && lowerBody.isAt(world, x, y - 2, z, remove)) {
                // Check +-x first
                if (shoulders.isAt(world, x - 1, y, z, remove)
                        && shoulders.isAt(world, x + 1, y, z, remove)
                        && arms.isAt(world, x - 1, y - 1, z, remove)
                        && arms.isAt(world, x + 1, y - 1, z, remove)
                        && legs.isAt(world, x - 1, y - 1, z, remove)
                        && legs.isAt(world, x + 1, y - 1, z, remove)) {
                    if (clearShape) {
                        world.setBlockToAir(x, y, z);
                        remove.commit();
                    }
                    return true;
                } else if (clearShape) {
                    remove.abort();
                }
                remove.addAction(world, x, y - 1, z);
                remove.addAction(world, x, y - 2, z);
                // Check +-z now
                if (shoulders.isAt(world, x, y, z - 1, remove)
                        && shoulders.isAt(world, x, y, z + 1, remove)
                        && arms.isAt(world, x, y - 1, z - 1, remove)
                        && arms.isAt(world, x, y - 1, z + 1, remove)
                        && legs.isAt(world, x, y - 1, z - 1, remove)
                        && legs.isAt(world, x, y - 1, z + 1, remove)) {
                    if (clearShape) {
                        world.setBlockToAir(x, y, z);
                        remove.commit();
                    }
                    return true;
                } else if (clearShape) {
                    remove.abort();
                }
            }
        }
        return false;
    }
    
    /**
     * Spawns a golem if this registration's type at the specified location via
     * reflection.
     * 
     * @param world The world to spawn the golem in
     * @param x The x coord to spawn at
     * @param y The y coord to spawn at
     * @param z The z coord to spawn at
     * @return
     */
    public EntityCustomGolem spawn(World world, int x, int y, int z) {
        EntityCustomGolem theGolem = null;
        try {
            theGolem = golemClass.getConstructor(World.class)
                    .newInstance(world);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (theGolem != null) {
            theGolem.setLocationAndAngles(x + .5d, y - 2, z + .5d, 0, 0);
            world.spawnEntityInWorld(theGolem);
        }
        return theGolem;
    }
}
