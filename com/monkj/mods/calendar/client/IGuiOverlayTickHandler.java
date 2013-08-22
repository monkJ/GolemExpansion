/**
 * File: OverlayGui.java
 * created: 15.05.2013 - 16:27:30
 * by: RoboMat
 */

package com.monkj.mods.calendar.client;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;

import com.monkj.mods.calendar.Calendar;
import com.monkj.mods.calendar.client.keys.CalendarKeyHandler;
import com.monkj.mods.calendar.utility.DateHandler;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class IGuiOverlayTickHandler implements ITickHandler
{
	final private Calendar calendar = Calendar.instance;
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {}
	
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		final Minecraft mc = Minecraft.getMinecraft();
		final EntityClientPlayerMP player = mc.thePlayer;
		final DateHandler handler = calendar.getCalculator();
		
		String date = handler.returnStandardDate();
		String clock = handler.returnStandardClock();
		String era = handler.returnEras();
		
		if (mc.thePlayer == null || mc.currentScreen != null) { return; }
		
		if (player.inventory.hasItem(Calendar.instance.getItemCalendar().itemID) && CalendarKeyHandler.isKeyPressed()) {
			mc.fontRenderer.drawStringWithShadow(date, 4, 4, 16777215);
			mc.fontRenderer.drawStringWithShadow(clock, 4, 34, 16777215);
			mc.fontRenderer.drawStringWithShadow(era, 4, 14, 16777215);
		}
	}
	
	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.RENDER);
	}
	
	@Override
	public String getLabel() {
		return "guiTickHandler";
	}
}
