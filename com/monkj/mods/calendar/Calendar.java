/**
 * File: Calendar.java
 * created: 24.04.2013 - 15:52:02
 * by: RoboMat
 */

package com.monkj.mods.calendar;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.Configuration;

import com.monkj.mods.calendar.blocks.BlockCalendar;
//import com.monkj.mods.calendar.commands.CommandClock;
//import com.monkj.mods.calendar.commands.CommandDays;
//import com.monkj.mods.calendar.commands.CommandYears;
import com.monkj.mods.calendar.common.CalendarCommonProxy;
import com.monkj.mods.calendar.items.ItemCalendar;
import com.monkj.mods.calendar.tileentity.TileEntityCalendar;
import com.monkj.mods.calendar.utility.DateHandler;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = Calendar.MOD_ID, name = Calendar.MOD_NAME, version = Calendar.MOD_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class Calendar
{
	/* ---------------------- General Mod Information ------------------------- */
	final public static String MOD_ID = "CalendarGui_001";
	final public static String MOD_NAME = "CalendarGui";
	final public static String MOD_VERSION = "1.0.3";
	final public static String MOD_AUTHOR = "Originally made by Robomat, now updated and maintained by monkJ";
	final public static String MOD_DESCRIPTION = "Adds a calendar that allows you to keep track of the real date of your minecraft world.";
	final public static String MOD_URL = "http://www.minecraftforum.net/topic/1633665-";
	
	@Instance(MOD_ID)
	public static Calendar instance;
	
	@SidedProxy(clientSide = "com.monkj.mods.calendar.client.CalendarClientProxy", serverSide = "com.monkj.mods.calendar.common.CalendarCommonProxy")
	public static CalendarCommonProxy proxy;
	
	/* ---------------------- Standard values --------------------------------- */
	private String[] dayNames = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
	private String[] dayNamesFull = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
	private String[] monthNames = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	private String[] monthNamesFull = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
	private String[] eraNames = { "Clouds Fall", "Creepers Retreat", "Seventh Ender", "Drunk Zombie", "Deep Sun" };
	
	private int[] monthLengths = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	private int eraLength = 2;
	private int yearOffset = 1;
	
	/* ---------------------- Blocks && Items --------------------------------- */
	private final static String itemCalendarNameInternal = "itemCalendar";
	private final static String itemCalendarNameExternal = "Calendar";
	private static int itemCalendarId = 365;
	private static Item itemCalendar;
	
	private final static String blockCalendarNameInternal = "blockCalendar";
	private final static String blockCalendarBlockNameExternal = "Calendar";
	private static int blockCalendarId = 2500;
	private static Block blockCalendar;
	
	/* ---------------------- Date Calculator --------------------------------- */
	private static DateHandler calculator;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {		
		event.getModMetadata().modId = MOD_ID;
		event.getModMetadata().name = MOD_NAME;
		event.getModMetadata().version = MOD_VERSION;
		event.getModMetadata().authorList.add(MOD_AUTHOR);
		event.getModMetadata().description = MOD_DESCRIPTION;
		event.getModMetadata().url = MOD_URL;
		
		proxy.registerRenderers();
		
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		itemCalendarId = config.get("Ids", "itemCalendarId", itemCalendarId).getInt();
		blockCalendarId = config.get("Ids", "blockCalendarId", blockCalendarId).getInt();
		dayNames = config.get("Date Names", "Days", dayNames).getStringList();
		dayNamesFull = config.get("Date Names", "Days Full", dayNamesFull).getStringList();
		monthNames = config.get("Date Names", "Months", monthNames).getStringList();
		monthNamesFull = config.get("Date Names", "Month Full", monthNamesFull).getStringList();
		eraNames = config.get("Date Names", "Eras", eraNames).getStringList();
		monthLengths = config.get("Date Lengths", "Months", monthLengths).getIntList();
		eraLength = config.get("Date Lengths", "Eras", eraLength).getInt();
		yearOffset = config.get("Misc", "Year Offset", yearOffset).getInt();
		config.save();
		
		calculator = new DateHandler(dayNames, dayNamesFull, monthNames, monthNamesFull, eraNames, monthLengths, eraLength, yearOffset);
		
		/* KEYBINDING --------------------------------------------------------- */
		proxy.registerKeys();
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) {
		
		/* ITEMS -------------------------------------------------------------- */
		itemCalendar = new ItemCalendar(itemCalendarId, itemCalendarNameInternal);
		GameRegistry.registerItem(itemCalendar, itemCalendarNameInternal);
		GameRegistry.addRecipe(new ItemStack(itemCalendar, 1), new Object[] { "###", "#%#", "###", '#', Item.paper, '%', Item.pocketSundial });
		LanguageRegistry.addName(itemCalendar, itemCalendarNameExternal);
		LanguageRegistry.instance().addNameForObject(itemCalendar, "de_DE", "Kalender");
		LanguageRegistry.instance().addNameForObject(itemCalendar, "sv_SE", "Kalender");
		/* BLOCKS ------------------------------------------------------------- */
		blockCalendar = new BlockCalendar(blockCalendarId, blockCalendarNameInternal);
		GameRegistry.registerBlock(blockCalendar, blockCalendarNameInternal);
		GameRegistry.registerTileEntity(TileEntityCalendar.class, blockCalendarNameInternal);
		LanguageRegistry.addName(blockCalendar, blockCalendarBlockNameExternal);
		LanguageRegistry.instance().addNameForObject(blockCalendar, "de_DE", "Kalender");
		LanguageRegistry.instance().addNameForObject(blockCalendar, "sv_SE	", "Kalender");
		
		/* CHEST SPAWN HOOKS -------------------------------------------------- */
		ChestGenHooks.getInfo("pyramidDesertyChest").addItem(new WeightedRandomChestContent(new ItemStack(itemCalendar), 1, 1, 5));
		ChestGenHooks.getInfo("pyramidJungleChest").addItem(new WeightedRandomChestContent(new ItemStack(itemCalendar), 1, 1, 5));
		ChestGenHooks.getInfo("strongholdLibrary").addItem(new WeightedRandomChestContent(new ItemStack(itemCalendar), 1, 1, 25));
		ChestGenHooks.getInfo("dungeonChest").addItem(new WeightedRandomChestContent(new ItemStack(itemCalendar), 1, 1, 5));
	}
	
	@ServerStarting
	public void onServerStart(FMLServerStartingEvent event) {
		/* TICKS -------------------------------------------------------------- */
		proxy.registerTickHandlers();
		
		/* COMMANDS ----------------------------------------------------------- */
		//event.registerServerCommand(new CommandClock());
		//event.registerServerCommand(new CommandDays());
		//event.registerServerCommand(new CommandYears(calculator.getYearLength()));
	}
	
	/* ---------------------- Getters && Setters ------------------------------ */
	/**
	 * @return The date handler used to handle all ingame date calculations.
	 */
	public static DateHandler getCalculator() {
		return calculator;
	}
	
	/**
	 * @return the itemCalendar
	 */
	public static Item getItemCalendar() {
		return itemCalendar;
	}
	
	/**
	 * @return the blockCalendar
	 */
	public static Block getBlockCalendar() {
		return blockCalendar;
	}
}
