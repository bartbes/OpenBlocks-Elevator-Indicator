package com.bartbes.openblocksElevatorIndicator;

import net.minecraft.block.Block;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@Mod(name = IndicatorMod.MODNAME,
		modid = IndicatorMod.MODID,
		dependencies = "required-after:OpenBlocks")
public class IndicatorMod
{
	public static final String MODNAME = "OpenBlocks Elevator Indicator";
	public static final String MODID = "openblocksElevatorIndicator";

	@Mod.Instance(MODID)
	public static IndicatorMod instance;

	@SidedProxy(modId = MODID,
			clientSide = "com.bartbes.openblocksElevatorIndicator.CommonProxy$ClientProxy",
			serverSide = "com.bartbes.openblocksElevatorIndicator.CommonProxy")
	public static CommonProxy proxy;

	private IndicatorOverlay overlay;
	Block elevatorBlock;

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		elevatorBlock = GameRegistry.findBlock("OpenBlocks", "elevator");

		if (elevatorBlock == null)
			System.err.println("Can't find elevator!");

		overlay = new IndicatorOverlay();
		proxy.registerTickHandler(overlay);
		proxy.registerOverlay(overlay);
	}
}
