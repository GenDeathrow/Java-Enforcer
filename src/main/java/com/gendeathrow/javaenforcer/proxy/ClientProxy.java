package com.gendeathrow.javaenforcer.proxy;

import net.minecraft.client.Minecraft;

import com.gendeathrow.javaenforcer.UpdateChecker;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{
	@Override
	public boolean isClient()
	{
		return true;
	}
	
	@Override
	public boolean isOpenToLAN()
	{
		if (Minecraft.getMinecraft().isIntegratedServerRunning())
		{
			return Minecraft.getMinecraft().getIntegratedServer().getPublic();
		} else
		{
			return false;
		}
	}
	
	
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
	}
}
