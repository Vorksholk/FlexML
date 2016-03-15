package com.theopeneffect.flexml;

/**
 * A container class to hold state information pertaining to the
 * layout positioning of a graphic object.
 * 
 * @author Maxwell Sanchez
 *
 */
public class GraphicProperties 
{
	// Layer to reder at (higher zScore = closer to front of display)
	private int zScore;
	
	// Positioning and size variables
	private double posX;
	private double posY;
	private double width;
	private double height;
	private boolean enterable;
	
	/**
	 * Construct a GraphicProperties object with the provided position and size information.
	 * 
	 * All positioning and size are represented as doubles from 0.0 to 1.0, which correspond
	 * to percentages relative to the size of the RenderPanel.
	 * 
	 * @param posX X position of the object
	 * @param posY Y position of the object
	 * @param width Width of the object
	 * @param height Height of the object
	 * @param zScore Layer to render at
	 */
	public GraphicProperties(double posX, double posY, double width, double height, int zScore, boolean enterable)
	{
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		this.zScore = zScore;
		this.enterable = enterable;
	}
	
	/**
	 * Get the object's zScore
	 * 
	 * @return The object's zScore
	 */
	public int getZScore() 
	{
		return zScore;
	}
	
	/**
	 * Get the object's X position
	 * 
	 * @return The object's X position
	 */
	public double getPosX() 
	{
		return posX;
	}

	/**
	 * Get the object's Y position
	 * 
	 * @return The object's Y position
	 */
	public double getPosY() 
	{
		return posY;
	}
	
	/**
	 * Get the object's width 
	 * 
	 * @return The object's width
	 */
	public double getWidth() 
	{
		return width;
	}

	/**
	 * Get the object's height
	 * 
	 * @return The object's height
	 */
	public double getHeight() 
	{
		return height;
	}
	
	/**
	 * Get whether the object is enterable
	 * 
	 * @return Whether the object is enterable
	 */
	public boolean getEnterable()
	{
		return enterable;
	}
}
