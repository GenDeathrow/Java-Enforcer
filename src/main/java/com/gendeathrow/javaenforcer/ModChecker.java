package com.gendeathrow.javaenforcer;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class ModChecker 
{
// (modid/modName):(reason)	
	
	private static HashMap<String, String> incompatableMods = new HashMap<String, String>();
	
	private static ArrayList<String> ignore = new ArrayList<String>();
	
	
	public static void run()
	{
		
		JavaEnforcer.logger.log(Level.INFO, "Checking installed Mods...");
		
		if(FMLClientHandler.instance().hasOptifine() && !JavaEnforcer.isOptifineCompatable)
		{
			String msg = "This Modpack is not compatable with Optifine please uninstall this mod";
			Utils.popUpError(msg,"<html><center><p> This Modpack is not compatable with Optifine <br> <font Color=red>please uninstall this mod </font>");
            throw new RuntimeException(msg);
		}

		ignore.add("FML");
		ignore.add(JavaEnforcer.MODID);
		ignore.add("mcp");
		ignore.add("Forge");
		
		for(String modid : JavaEnforcer.incompMods)
		{
			String incompID = modid;
			String incompReason = "";
			
			if(modid.contains(":")) 
			{
				String[] split = modid.split("\\:", 2);
				incompID = split[0].trim();
				incompReason = split[1];
			}
			
			if(!ignore.contains(incompID))
			{
				incompatableMods.put(incompID, incompReason);
			}else if(ignore.contains(incompID)) JavaEnforcer.logger.log(Level.WARN, "Can not Disable "+ incompID);

		}

		for(ModContainer mod : Loader.instance().getActiveModList())
		{
			if(incompatableMods.containsKey(mod.getModId()) || incompatableMods.containsKey(mod.getName()))
			{
				String msg = "This Modpack is not compatable with "+mod.getName()+" please uninstall this mod";
				Utils.popUpError(msg,"<html><center><p> This Modpack is not compatable with "+mod.getName()+" <br> <font Color=red>please uninstall this mod </font> ");
	            throw new RuntimeException(msg);
			}
		}
		
	}
	

}
