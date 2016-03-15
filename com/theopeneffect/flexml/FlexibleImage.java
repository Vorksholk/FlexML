package com.theopeneffect.flexml;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * A class to render an image at a provided location and provided dimensions.
 * 
 * Extended to add additional functionality for buttons and game tiles.
 * 
 * @author Maxwell Sanchez
 *
 */
public class FlexibleImage implements Renderable 
{
	private BufferedImage internalImage;
	
	private String identifier;
	
	private boolean isVisible = true;
	
	public FlexibleImage(BufferedImage image, String identifier)
	{
		internalImage = image;
		this.identifier = identifier;
	}
	
	public void setImage(BufferedImage newImage)
	{
		this.internalImage = newImage;
	}
	
	public FlexibleImage(URL imageFile, String identifier) 
	{
		try
		{
			internalImage = ImageIO.read(imageFile);
			this.identifier = identifier;
		} catch (Exception e) { e.printStackTrace(); }
	}

	@Override
	public void paint(Graphics2D g, int startX, int startY, int width, int height) 
	{
		if (isVisible)
		{
			g.drawImage(internalImage, startX, startY, width, height, null);
			afterPaint(g, startX, startY, width, height);
		}
	}
	
	@Override
	public boolean getVisible() 
	{
		return isVisible;
	}
	
	public void afterPaint(Graphics2D g, int startX, int startY, int width, int height)
	{
		// Do Nothing
	}

	@Override
	public void setVisible(boolean visibility) 
	{
		isVisible = visibility;
	}

	@Override
	public String getIdentifier() 
	{
		return identifier;
	}

	@Override
	public void mouseOver(int x, int y) 
	{
		// Do Nothing
	}
	
	public void setSelected(boolean selected)
	{
		// Do Nothing
	}

	@Override
	public void mouseClick(int x, int y) 
	{
		// Do Nothing
	}

	@Override
	public void mouseAbsent()
	{
		// Do Nothing
	}

	@Override
	public void offScreen()
	{
		// Do Nothing
	}

	@Override
	public void keyTyped(KeyEvent keyEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent keyEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent keyEvent) {
		// TODO Auto-generated method stub
		
	}
	
	
}
