package com.monkj.mods.golemexpansion.block;

import com.monkj.mods.golemexpansion.GolemExpansion;

public class ModBlocks {
    public static final int glowingRSAirID = GolemExpansion.config.getBlock("glowingRedstoneAir", 600).getInt();
    
    public static BlockGlowingRedstoneAir glowingRedstoneAir = new BlockGlowingRedstoneAir(glowingRSAirID);
    
    public static void init(){}
}
