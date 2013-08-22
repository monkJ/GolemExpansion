package com.monkj.mods.golemexpansion.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityIronGolem;

import com.monkj.mods.golemexpansion.golem.EntityCustomGolem;
import com.monkj.mods.golemexpansion.golem.RenderCustomGolem;

/**
 * Client-specific stuff goes here, overriding blank methods from
 * {@link CommonProxy}. Currently empty.
 * 
 * @author monkJ
 * 
 */
public class ClientProxy extends CommonProxy {
    private static RenderCustomGolem golemRenderer;
    
    @Override
    public void registerRenderer() {
        RenderManager.instance.entityRenderMap.remove(EntityIronGolem.class);
        golemRenderer = new RenderCustomGolem();
        RenderManager.instance.entityRenderMap.put(EntityCustomGolem.class,
                golemRenderer);
        golemRenderer.setRenderManager(RenderManager.instance);
    }
    
    @Override
    public void tellPlayer(String string) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(string);
    }
}
