package com.gendeathrow.javaenforcer;

import java.net.URI;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class JavaChecker 
{
	
	public static void run() throws InterruptedException
	{
		Logger logger = JavaEnforcer.logger;
		
		JavaEnforcer.JAVA_VERSION = getJavaVersion();

		if(JavaEnforcer.JAVA_VERSION < JavaEnforcer.JAVA_ENFORCER) 
		{
			String msg = "This Modpack requires Java Version "+ JavaEnforcer.JAVA_ENFORCER +". You have version "+ JavaEnforcer.JAVA_VERSION +". Go to "+ JavaEnforcer.http;
		
			String msgpop = "<html><center><p> This Modpack requires <font Color=red>Java Version "+ JavaEnforcer.JAVA_ENFORCER +"</font>. <br> You have version "+ JavaEnforcer.JAVA_VERSION +".<br> "+ JavaEnforcer.customMSG +"  <br> <br>  <br> <br> Java Enforcer will take you to the download page <Br><font Color=Blue><color Go to "+ JavaEnforcer.http +"</font>";
			
			Utils.popUpError(msg, msgpop);
			
			logger.log(Level.ERROR, msg);
		
            gotoHttp();
        
            logger.log(Level.ERROR, msg);
		
            throw new RuntimeException(msg);
		}
		else
		{
			logger.log(Level.INFO, "You are using the correct Java Version Congrats!");
		}
		
	}
	
    private static double getJavaVersion()
    {
     	 String version = System.getProperty("java.version");
     	    
     	 JavaEnforcer.logger.log(Level.INFO, "Grabbing Java Version..."+ version);
    		
    	 int pos = version.indexOf('.');
    	 pos = version.indexOf('.', pos+1);
    	 return Double.parseDouble (version.substring (0, pos));
    }
    
    private static void gotoHttp()
    {
    
        try
        {
        	Class oclass = Class.forName("java.awt.Desktop");
        	Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
        	oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {new URI(JavaEnforcer.http)});
        }	
        catch (Throwable throwable)
        {
        	 JavaEnforcer.logger.error("Couldn\'t open link", throwable);
        }
    }
}
