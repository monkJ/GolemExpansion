/**
 * File: CalendarClientProxy.java
 * created: 24.04.2013 - 15:57:20
 * by: RoboMat
 */

package com.monkj.mods.calendar.client;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import com.monkj.mods.calendar.client.keys.CalendarKeyHandler;
import com.monkj.mods.calendar.client.renderer.tileentity.TileEntityCalendarRenderer;
import com.monkj.mods.calendar.common.CalendarCommonProxy;
import com.monkj.mods.calendar.tileentity.TileEntityCalendar;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class CalendarClientProxy extends CalendarCommonProxy
{
	@Override
	public void registerRenderers() {
		final TileEntityCalendarRenderer renderer = new TileEntityCalendarRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCalendar.class, renderer);
	}
	
	@Override
	public void registerTickHandlers() {
		TickRegistry.registerTickHandler(new IGuiOverlayTickHandler(), Side.CLIENT);
		TickRegistry.registerScheduledTickHandler(new ICalendarScheduledTickHandler(), Side.CLIENT);
	}
	
	@Override
	public void registerKeys() {
		final KeyBinding toggleCalendar = new KeyBinding("ToggleCalendarGui", Keyboard.KEY_C);
		final KeyBinding[] keys = { toggleCalendar };
		final boolean[] keyRepeater = { false };
		final CalendarKeyHandler handler = new CalendarKeyHandler(keys, keyRepeater);
		KeyBindingRegistry.registerKeyBinding(handler);
	}
}
