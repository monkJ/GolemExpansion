package com.monkj.mods.golemexpansion.util;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import com.monkj.mods.golemexpansion.golem.GolemRegistry;

/**
 * Holds information about not only a block, but also its metadata (damage
 * value). Used so that metadata-sensitive checks can be made easily in the
 * {@link GolemRegistry}
 * 
 * @author monkJ
 * 
 */
public class BlockWithMeta {
    public Block b;
    public int   m;
    
    /**
     * A simple identity constructor
     * 
     * @param block
     * @param meta
     */
    public BlockWithMeta(Block block, int meta) {
        b = block;
        m = meta;
    }
    
    /**
     * Constructor that makes checks using this ignore metadata; Provided for
     * convenience.
     * 
     * @param b
     */
    public BlockWithMeta(Block b) {
        this(b, -1);
    }
    
    /**
     * Checks if the (block,metadate) tuple represented by this object is
     * located at the specified coords.
     * 
     * @param world The {@link World} to check in.
     * @param x x coord to check at
     * @param y y coord to check at
     * @param z z coord to check at
     * @param remover A {@link TransactionDeleteBlocks} to remove multiple
     *        blocks at once. May be set to null, it will then be ignored.
     * @return
     */
    public boolean isAt(World world, int x, int y, int z,
            TransactionDeleteBlocks remover) {
        boolean result = b == null || world.getBlockId(x, y, z) == b.blockID
                && (m == -1 || world.getBlockMetadata(x, y, z) == m);
        if (result && b != null && remover != null) {
            remover.addAction(world, x, y, z);
        }
        return result;
    }
}