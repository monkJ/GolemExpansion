package com.monkj.mods.golemexpansion.proxy;

/**
 * A proxy to handle client-specific stuff. Client-only methods will be blank
 * here and overridden in {@link ClientProxy}. This is currently empty.
 * 
 * @author monkJ
 * 
 */
public class CommonProxy {
    public void registerRenderer() {
    }

    public void tellPlayer(String string) {}
}
