package com.gendeathrow.javaenforcer;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = JavaEnforcer.MODID, version = JavaEnforcer.VERSION, name = JavaEnforcer.NAME, dependencies = "before:*")
public class JavaEnforcer 
{

    	
    public static final String MODID = "java_enforcer";
    public static final String VERSION = "1.0";
    public static final String NAME = "Java Enforcer";
        
    public static  Logger logger;
    		 
    
    public static double JAVA_ENFORCER = 1.8;
    public static String customMSG = " ";
    
   	public static double JAVA_VERSION;
   	
   	public static String http = "https://www.java.com/download/";

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) 
	{
		
		if(event.getSide().isClient())
		{
		
			logger = event.getModLog();
    	
			logger.log(Level.INFO, "Loading Java Enforcer v"+ VERSION);
    	
			getConfigData();
			try 
			{
				JavaChecker.run();
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}

	}
	
	private void getConfigData()
	{
		Configuration config;
		File file = new File("config/java_enforcer.cfg");
	
		config = new Configuration(file);
	
		config.load();
			String val = config.getString("Enforce Java Version", Configuration.CATEGORY_GENERAL, Double.toString(JAVA_ENFORCER), "Throws an error if user doesn't have correct java version for mod pack. ie: if you set to 1.8 player must have java version 1.8+");
			customMSG = config.getString("Custom Message", Configuration.CATEGORY_GENERAL, customMSG, "Use simple html code to write a message, ex: \"<center><font color=red> sample message <br> next line </font></center>\" ");
		config.save();
	
		try
		{
			JAVA_ENFORCER = (double) Double.parseDouble(val);
		}
		catch(Throwable e)
		{
			logger.log(Level.ERROR, "Error reading Java version number. Please check your config file. Defalt version: "+ JAVA_ENFORCER );
		}
		
		logger.log(Level.INFO, "Enforcing java to version: "+ JAVA_ENFORCER);
	}
	
   

	}
