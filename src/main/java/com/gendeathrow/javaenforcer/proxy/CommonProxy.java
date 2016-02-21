package com.gendeathrow.javaenforcer.proxy;

import com.gendeathrow.javaenforcer.UpdateChecker;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy
{
	public boolean isClient()
	{
		return false;
	}
	
	public boolean isOpenToLAN()
	{
		return false;
	}
	
	public void registerEventHandlers()
	{
		FMLCommonHandler.instance().bus().register(new UpdateChecker());
	}
	
	public void preInit(FMLPreInitializationEvent event)
	{
		
	}
}
	
