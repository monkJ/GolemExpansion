/**
 * File: KeyBinding.java
 * created: 16.05.2013 - 13:21:20
 * by: RoboMat
 */

package com.monkj.mods.calendar.client.keys;

import java.util.EnumSet;

import net.minecraft.client.settings.KeyBinding;

import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CalendarKeyHandler extends KeyHandler
{
	private static boolean isKeyPressed = false;
	
	public CalendarKeyHandler(KeyBinding[] keyBindings, boolean[] repeatings) {
		super(keyBindings, repeatings);
	}
	
	@Override
	public String getLabel() {
		return "CalendarGuiKey";
	}
	
	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}
	
	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {

	}
	
	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
		final Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (tickEnd && side == Side.CLIENT) {
			isKeyPressed = !isKeyPressed;
			System.out.println(isKeyPressed);
		}
	}
	
	/* ---------------------- Getters && Setters ------------------------------ */
	public static boolean isKeyPressed() {
		return isKeyPressed;
	}
}