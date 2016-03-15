package com.theopeneffect.flexml;

import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * A FlexibleImage with a button mouseover shimmer animation
 * 
 * @author Maxwell Sanchez
 *
 */
public class FlexibleImageButton extends FlexibleImage 
{
	// State to determine whether a shimmer animation should be rendered
	private double mouseoverState = -1;
	private boolean mouseInside = false;
	
	/**
	 * Construct a FlexibleImage given the provided image and identifier
	 * 
	 * @param image BufferedImage to display
	 * @param identifier Identifier used for GUI positioning
	 */
	public FlexibleImageButton(BufferedImage image, String identifier) 
	{
		super(image, identifier);
	}

	/**
	 * Construct a FlexibleImage given the provided image and identifier
	 * 
	 * @param URL URL for image to display
	 * @param identifier Identifier used for GUI positioning
	 */
	public FlexibleImageButton(URL imageFile, String identifier) 
	{
		super(imageFile, identifier);
	}

	@Override
	public void mouseOver(int x, int y) 
	{
		// Set animation state appropriately
		if (mouseoverState < 0 && !mouseInside)
			mouseoverState = 0;
		mouseInside = true;
	}
	
	@Override
	public void mouseAbsent()
	{
		mouseInside = false;
	}
	
	/**
	 * When the button is no longer on the screen, stop the shimmer animation
	 * so the animation doesn't continue when the button is displayed again.
	 */
	public void offScreen()
	{
		mouseoverState = -1;
	}

}
