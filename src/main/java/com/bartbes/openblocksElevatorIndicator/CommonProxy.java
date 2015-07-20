package com.bartbes.openblocksElevatorIndicator;

import cpw.mods.fml.common.FMLCommonHandler;

import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
	public static class ClientProxy extends CommonProxy
	{
		@Override
		void registerTickHandler(Object handler)
		{
			FMLCommonHandler.instance().bus().register(handler);
		}

		@Override
		void registerOverlay(Object handler)
		{
			MinecraftForge.EVENT_BUS.register(handler);
		}
	}

	void registerTickHandler(Object handler)
	{
	}

	void registerOverlay(Object handler)
	{
	}
}
