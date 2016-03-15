package com.theopeneffect.flexml;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class FlexibleLabel extends FlexibleInputLabel
{
	public FlexibleLabel(String identifier, double scale, Color startColor, Color mouseOverColor)
	{
		super(identifier, scale, startColor, mouseOverColor);
	}
	
	@Override
	public void keyTyped(KeyEvent keyEvent) 
	{
		// Do Nothing!
	}
	
	@Override
	public void keyPressed(KeyEvent keyEvent) 
	{
	}

	@Override
	public void keyReleased(KeyEvent keyEvent)
	{
		// Do Nothing!
	}

}
