package com.bartbes.openblocksElevatorIndicator;

import net.minecraft.block.Block;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@Mod(name = IndicatorMod.MODNAME,
		modid = IndicatorMod.MODID,
		version = IndicatorMod.VERSION,
		dependencies = "required-after:OpenBlocks")
public class IndicatorMod
{
	public static final String MODNAME = "OpenBlocks Elevator Indicator";
	public static final String MODID = "openblocksElevatorIndicator";
	public static final String VERSION = "1.0";

	@Mod.Instance(MODID)
	public static IndicatorMod instance;

	@SidedProxy(modId = MODID,
			clientSide = "com.bartbes.openblocksElevatorIndicator.Proxy$Client",
			serverSide = "com.bartbes.openblocksElevatorIndicator.Proxy$Common")
	public static Proxy proxy;

	private IndicatorOverlay overlay;
	Block elevatorBlock;

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		for (Object o : Block.blockRegistry)
		{
			Block b = (Block) o;
			String name = b.getUnlocalizedName();
			if (name.equals("tile.openblocks.elevator"))
			{
				elevatorBlock = b;
				break;
			}
		}

		if (elevatorBlock == null)
			System.err.println("Failed to find elevator!");

		overlay = new IndicatorOverlay();
		proxy.registerTickHandler(overlay);
		proxy.registerOverlay(overlay);
	}
}
