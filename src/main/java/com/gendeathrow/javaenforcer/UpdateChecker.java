package com.gendeathrow.javaenforcer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;



public class UpdateChecker 
{
	boolean hasChecked = false;
	public static String version;
	public static String lastSeen;
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event)
	{
		hasChecked = true;
		displayUpdateCheck(event);
	}
	
	@SuppressWarnings("unused")
	private void displayUpdateCheck(PlayerLoggedInEvent event)
	{

		// File link: http://bit.ly/1OooLeO;
		// DO NOT CHANGE THIS!
		if(JavaEnforcer.VERSION == "GD_" + "JE" + "_VER")
		{
			event.player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "THIS COPY OF JAVA ENFORCER IS NOT FOR PUBLIC USE!"));
			return;
		}
		
		try
		{
			String page = getUrl("http://bit.ly/1OooLeO", true);
			String[] data = page.split("\\n");
			
			String[] rawVer = data[0].trim().split("\\.");
			version = rawVer[0] + "." + rawVer[1] + "." + rawVer[2];
			
			if(!JavaEnforcer.updateCheck)
			{
				return;
			}
			
			//Debug stuff that shouldn't be printed to the user's chat window!
			/*for(int i = 0; i < data.length; i++)
			{
				event.player.addChatMessage(new ChatComponentText(EnumChatFormatting.RESET + "" + data[i].trim()));
			}*/
			
			String http = data[0].trim();
			
			int verStat = compareVersions(JavaEnforcer.VERSION, version);
			
			if(verStat == -1)
			{
				event.player.addChatMessage(new ChatComponentTranslation("updatemsg.javaenforcer.available", version).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				event.player.addChatMessage(new ChatComponentTranslation("updatemsg.javaenforcer.download"));
				event.player.addChatMessage(new ChatComponentText("http://minecraft.curseforge.com/projects/java-enforcer/").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE).setUnderlined(true)));
//				for(int i = 3; i < data.length; i++)
//				{
//					if(i > 6)
//					{
//						event.player.addChatMessage(new ChatComponentText("" + (data.length - 7) + " more..."));
//						break;
//					} else
//					{
//						event.player.addChatMessage(new ChatComponentText(EnumChatFormatting.RESET + "" + data[i].trim()));
//					}
//				}
			} else if(verStat == 0)
			{
				event.player.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + StatCollector.translateToLocalFormatted("updatemsg.javaenforcer.uptodate", JavaEnforcer.VERSION)));
			} else if(verStat == 1)
			{
				event.player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("updatemsg.javaenforcer.debug", JavaEnforcer.VERSION)));
			} else if(verStat == -2)
			{
				event.player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("updatemsg.javaenforcer.error")));
			}
			
		} catch(IOException e)
		{
			if(JavaEnforcer.updateCheck)
			{
				JavaEnforcer.logger.log(Level.WARN, "Failed to get/read versions file!");
			}
		}
	}
	
	/**
	 * Grabs http webpage and returns data
	 * @param link
	 * @param doRedirect
	 * @return
	 * @throws IOException
	 */
	public static String getUrl(String link, boolean doRedirect) throws IOException
	{
		URL url = new URL(link);
		HttpURLConnection.setFollowRedirects(false);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setDoOutput(false);
		con.setReadTimeout(20000);
		con.setRequestProperty("Connection", "keep-alive");
		
		con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:16.0) Gecko/20100101 Firefox/16.0");
		((HttpURLConnection)con).setRequestMethod("GET");
		con.setConnectTimeout(5000);
		BufferedInputStream in = new BufferedInputStream(con.getInputStream());
		int responseCode = con.getResponseCode();
		if(responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_MOVED_PERM)
		{
			JavaEnforcer.logger.log(Level.WARN, "Update request returned response code: " + responseCode + " " + con.getResponseMessage());
		} else if(responseCode == HttpURLConnection.HTTP_MOVED_PERM)
		{
			if(doRedirect)
			{
				try
				{
					return getUrl(con.getHeaderField("location"), false);
				} catch(IOException e)
				{
					throw e;
				}
			} else
			{
				throw new IOException();
			}
		}
		StringBuffer buffer = new StringBuffer();
		int chars_read;
		//	int total = 0;
		while((chars_read = in.read()) != -1)
		{
			char g = (char)chars_read;
			buffer.append(g);
		}
		final String page = buffer.toString();
		
		return page;
	}

	
	/**
	 * Will compare Versions numbers and give difference
	 * @param oldVer
	 * @param newVer
	 * @return
	 */
	public static int compareVersions(String oldVer, String newVer)
	{
		if(oldVer == null || newVer == null || oldVer.isEmpty() || newVer.isEmpty())
		{
			return -2;
		}
		
		int result = 0;
		int[] oldNum;
		int[] newNum;
		String[] oldNumStr;
		String[] newNumStr;
		
		try
		{
			oldNumStr = oldVer.split("\\.");
			newNumStr = newVer.split("\\.");
			
			oldNum = new int[]{Integer.valueOf(oldNumStr[0]),Integer.valueOf(oldNumStr[1]),Integer.valueOf(oldNumStr[2])};
			newNum = new int[]{Integer.valueOf(newNumStr[0]),Integer.valueOf(newNumStr[1]),Integer.valueOf(newNumStr[2])};
		} catch(IndexOutOfBoundsException e)
		{
			JavaEnforcer.logger.log(Level.WARN, "An IndexOutOfBoundsException occured while checking version!", e);
			return -2;
		} catch(NumberFormatException e)
		{
			JavaEnforcer.logger.log(Level.WARN, "A NumberFormatException occured while checking version!\n", e);
			return -2;
		}
		
		for(int i = 0; i < 3; i++)
		{
			if(oldNum[i] < newNum[i])
			{
				return -1;
			} else if(oldNum[i] > newNum[i])
			{
				return 1;
			}
		}
		return result;
	}
}
