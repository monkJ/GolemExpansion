package com.monkj.mods.golemexpansion.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;

/**
 * Utility class that will accumulate transactions (here, deletion of blocks in
 * a world) to be performed later or discarded all at once.
 * 
 * @author monkJ
 * 
 */
public class TransactionDeleteBlocks {
    
    /**
     * Stores a single action to be taken, in this case the deletion of a single
     * block
     * 
     * @author monkJ
     * 
     * 
     */
    protected class DeleteAction {
        /**
         * {@link World} in which the block should be deleted
         */
        protected World world;
        /**
         * Coords of the block to be deleted
         */
        protected int   x, y, z;
        
        /**
         * Identity constructor
         */
        public DeleteAction(World world, int x, int y, int z) {
            this.world = world;
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
        /**
         * Perform the cation; here, delete the block.
         */
        public void run() {
            world.setBlockToAir(x, y, z);
        }
    }
    
    /**
     * A list of all the actions to be performed later
     */
    protected List<DeleteAction> actions = new ArrayList<DeleteAction>();
    
    /**
     * Adds an action (here, block deletion) to the internal list, to be
     * performed later.
     * 
     * @param world the world to delete from
     * @param x x coord to delete at
     * @param y y coord to delete at
     * @param z z coord to delete at
     */
    public void addAction(World world, int x, int y, int z) {
        actions.add(new DeleteAction(world, x, y, z));
    }
    
    /**
     * Perform the action all at once
     */
    public void commit() {
        for (DeleteAction a : actions) {
            if (a != null) {
                a.run();
            }
        }
    }
    
    /**
     * Abort: clears list of actions; this object can now be safely reused.
     */
    public void abort() {
        actions.clear();
    }
}
