package com.bartbes.openblocksElevatorIndicator;

import cpw.mods.fml.common.FMLCommonHandler;

import net.minecraftforge.common.MinecraftForge;

public abstract class Proxy
{
	public static class Common extends Proxy
	{
	}

	public static class Client extends Proxy.Common
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
