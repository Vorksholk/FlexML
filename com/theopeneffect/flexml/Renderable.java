package com.theopeneffect.flexml;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 * Interface to define the bare minimum required for any graphic object to be
 * rendered within a RenderPanel. Not all implementations of Renderable need to
 * respond to all events, but all events will be called on any Renderable object
 * when the event occurs.
 * 
 * Not to be confused with the java.awt.image.renderable package.
 * 
 * All items which a RenderPanel is capable of rendering must adhere to
 * this Renderable contract.
 * 
 * @author Maxwell Sanchez
 *
 */
public interface Renderable
{	
	/**
	 * Render and paint the graphic object according to provided 
	 * 
	 * @param g Graphics2D to draw with
	 * @param startX X coordinate of top-left corner from which to draw.
	 * @param startY Y coordinate of top-left corner from which to draw.
	 * @param width The width to draw this component within
	 * @param height The height to draw this component within
	 */
	public void paint(Graphics2D g, int startX, int startY, int width, int height);
	
	/**
	 * Get the object identifier, used for matching graphic object to
	 * layout properties.
	 * 
	 * @return String graphic object identifier
	 */
	public String getIdentifier();
	
	/**
	 * Determine whether the graphic object is set visible
	 * 
	 * @return boolean Whether the object is visible
	 */
	public boolean getVisible();
	
	/**
	 * Modify the visibility of the graphic object
	 * 
	 * @param visibility Whether the graphic object should be visible or not
	 */
	public void setVisible(boolean visibility);
	
	/**
	 * Respond to mouseover at the provided x and y coordinates
	 * 
	 * @param x X coordinate of mouseover relative to top-left corner of Renderable
	 * @param y Y coordinate of mouseover relative to top-left corner of Renderable
	 */
	public void mouseOver(int x, int y);
	
	/**
	 * Respond to mouse not being within object
	 */
	public void mouseAbsent();
	
	/**
	 * Responds to a mouse click at the provided x and y coordinates
	 * 
	 * @param x X coordinate of click relative to top-left corner of Renderable
	 * @param y Y coordinate of click relative to top-left corner of Renderable
	 */
	public void mouseClick(int x, int y);
	
	/**
	 * Takes appropriate actions to clean up when object is no longer displayed.
	 * 
	 * Most useful for clearing animations so they don't resume when an object
	 * reappears.
	 */
	public void offScreen();
	
	/**
	 * Responds to a key type when the item is selected
	 * 
	 * @param keyEvent The KeyEvent entered into the current item
	 */
	public void keyTyped(KeyEvent keyEvent);
	
	/**
	 * Responds to a key pressed when the item is selected
	 * 
	 * @param keyEvent The KeyEvent entered into the current item
	 */
	public void keyPressed(KeyEvent keyEvent);
	
	/**
	 * Responds to a key released when the item is selected
	 * 
	 * @param keyEvent The KeyEvent entered into the current item
	 */
	public void keyReleased(KeyEvent keyEvent);
}
