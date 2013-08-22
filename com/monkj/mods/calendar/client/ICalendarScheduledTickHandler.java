/**
 * File: ServerTickHandler.java
 * created: 25.04.2013 - 14:18:17
 * by: RoboMat
 */

package com.monkj.mods.calendar.client;

import java.util.EnumSet;

import com.monkj.mods.calendar.Calendar;

import net.minecraft.client.Minecraft;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

public class ICalendarScheduledTickHandler implements IScheduledTickHandler
{
	private final Calendar calendar = Calendar.instance;
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {}
	
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		final Minecraft mc = Minecraft.getMinecraft();
		
		if (mc.theWorld != null) {
			final long time = mc.theWorld.getWorldTime();	
			calendar.getCalculator().updateFields(time);
		}
	}
	
	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}
	
	@Override
	public String getLabel() {
		return "calendarTickHandler";
	}
	
	@Override
	public int nextTickSpacing() {
		return (int) calendar.getCalculator().getTicksPerMin();
	}
}
