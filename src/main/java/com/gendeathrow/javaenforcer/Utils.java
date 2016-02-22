package com.gendeathrow.javaenforcer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.Level;

public class Utils 
{

	public static void popUpError(String msg, String msgpop)
	{
		final JFrame parent = new JFrame();
        
        parent.setSize(400, 400);
        
        JOptionPane.showMessageDialog(parent, msgpop, "Incompatable mod error",  JOptionPane.ERROR_MESSAGE);

        JavaEnforcer.logger.log(Level.ERROR, msg);

	}
}
