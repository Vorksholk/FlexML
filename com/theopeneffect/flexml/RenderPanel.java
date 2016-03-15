package com.theopeneffect.flexml;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

/**
 * Top-level semi-flexible component to handle drawing user interfaces adhering
 * to a layout defined by a LayoutDescription.
 * 
 * @author Maxwell Sanchez
 * 
 */
public class RenderPanel extends Component implements MouseListener, MouseMotionListener, KeyListener
{
	private static final long serialVersionUID = 0xFEED;
	
	// Default width and height in a 16x9 ratio
	private static final int DEFAULT_WIDTH = 850;
	private static final int DEFAULT_HEIGHT = 478;
	
	// The highest zScore reached during the last render
	private int highestZScoreLastRender = 0;
	
	// All of the objects that can be rendered
	private ArrayList<Renderable> renderables;
	
	// The current layout description
	private LayoutDescription layoutDescription;
	
	// The selected Renderable, if it exists
	private Renderable selected = null;
	
	/**
	 * Create a RenderPanel with the provided LayoutDescription, width, and height
	 * 
	 * @param layoutDescription Describes the initial layout to use
	 * @param width The desired width in pixels
	 * @param height The desired height in pixels
	 */
	public RenderPanel(LayoutDescription layoutDescription, int width, int height)
	{
		renderables = new ArrayList<Renderable>();
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusTraversalKeysEnabled(false);
		this.layoutDescription = layoutDescription;
		this.setPreferredSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));
	}
	
	/**
	 * Create a RenderPanel with the provided LayoutDescription and default dimensions
	 * @param layoutDescription
	 */
	public RenderPanel(LayoutDescription layoutDescription) 
	{
		this(layoutDescription, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	/**
	 * Adds a Renderable object to the RenderPanel
	 * 
	 * @param renderableGraphicObject Renderable to add
	 */
	public void add(Renderable renderableGraphicObject)
	{
		renderables.add(renderableGraphicObject);
	}
	
	/**
	 * Sets the LayoutDescription to the provided LayoutDescription
	 * 
	 * @param newLayout New LayoutDescription
	 */
	public void setLayout(LayoutDescription newLayout)
	{
		this.layoutDescription = newLayout;
		selected = null;
	}
	
	/**
	 * Delegate painting job to all renderables with position and size described
	 * by the current layout description.
	 */
	public void paint(Graphics g)
	{
		super.paint(g);
		
		ArrayList<Renderable> renderables =  this.renderables; // Limit possibility of race condition
		if (g instanceof Graphics2D)
		{
			// Get width and height of RenderPanel
			int width = this.getWidth();
			int height = this.getHeight();
			
			// Cast provided Graphics to Graphics2D
			Graphics2D graphics = (Graphics2D)g;
			
			// Set the graphic rendering quality
			setQualityOnGraphics2D(graphics);
			
			// Start at zIndex of 0
			int currentZIndex = 0;
			
			// Count number of items that should be rendered
			int renderedItems = 0;
			for (int i = 0; i < renderables.size(); i++)
			{
				if (layoutDescription.getPropertiesForID(renderables.get(i).getIdentifier()) == null)
				{
					renderedItems++;
				}
			}
			// Loop until all relevant renderables have been painted
			while (renderedItems < renderables.size())
			{
				// Loop through all renderables, and see if they should be rendered now
				for (int i = 0; i < renderables.size(); i++)
				{
					Renderable itemToRender = renderables.get(i);
					if (itemToRender != null)
					{
						// Get GraphicProperties for the relevant renderable
						GraphicProperties itemToRenderProperties = layoutDescription.getPropertiesForID(itemToRender.getIdentifier());
						
						// Check if GraphicProperties exist
						if (itemToRenderProperties != null)
						{
							// Item should be rendered now, because zScore of object matches current zScore
							if (itemToRenderProperties.getZScore() == currentZIndex)
							{
								// Calculate location and size based on RenderPanel size
								int insertXLocation = (int)(width * itemToRenderProperties.getPosX());
								int insertYLocation = (int)(height * itemToRenderProperties.getPosY());
								int drawWidth = (int)(width * itemToRenderProperties.getWidth());
								int drawHeight = (int)(height * itemToRenderProperties.getHeight()); 
								itemToRender.paint(graphics, insertXLocation, insertYLocation, drawWidth, drawHeight);
								renderedItems++;
							}
						}
						else
						{
							// Alert item that it is no longer on the screen
							itemToRender.offScreen();
						}
					}
					else
					{
						renderables.remove(i);
						i--;
					}
				}
				currentZIndex++;
			}
			highestZScoreLastRender = currentZIndex;
		}
	}
	
	/**
	 * Set the quality on a Graphics2D object based on the class rendering quality state
	 * 
	 * @param graphics Graphics2D object to set quality on
	 */
	private void setQualityOnGraphics2D(Graphics2D graphics)
	{
		if (quality == OPTION_QUALITY_LOW)
		{
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
			graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
			graphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
			graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
			graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
		}
		else if (quality == OPTION_QUALITY_MED)
		{
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			graphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
			graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		}
		else if (quality == OPTION_QUALITY_HIGH)
		{
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			graphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
			graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		}
	}
	
	// Possible options for rendering quality
	public static final int OPTION_QUALITY_LOW = 1;
	public static final int OPTION_QUALITY_MED = 2;
	public static final int OPTION_QUALITY_HIGH = 3;
	
	// Rendering quality is, by default, medium
	private int quality = OPTION_QUALITY_MED;
	
	/**
	 * Set the rendering quality to the provided rendering quality
	 * 
	 * @param quality New rendering quality
	 */
	public void setQuality(int quality)
	{
		this.quality = quality;
	}
	
	/**
	 * Pass on click initiation events to the appropriate renderable child
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		int clickX = e.getX();
		int clickY = e.getY();
		int width = this.getWidth();
		int height = this.getHeight();
		
		// Begin at the highest-zScore-index and work down, looking for a Renderable in the click zone
		searchLoop: for (int i = highestZScoreLastRender; i >= 0; i--)
		{
			// Search through all renderables
			for (int j = 0; j < renderables.size(); j++)
			{
				Renderable possibleClickedItem = renderables.get(j);
				if (possibleClickedItem != null)
				{
					// Check if the Renderable is drawn where the click occurs
					GraphicProperties itemToRenderProperties = layoutDescription.getPropertiesForID(possibleClickedItem.getIdentifier());
					if (itemToRenderProperties != null)
					{
						if (itemToRenderProperties.getZScore() == i)
						{
							int insertXLocation = (int)(width * itemToRenderProperties.getPosX());
							int insertYLocation = (int)(height * itemToRenderProperties.getPosY());
							int drawWidth = (int)(width * itemToRenderProperties.getWidth());
							int drawHeight = (int)(height * itemToRenderProperties.getHeight());
							if (clickX >= insertXLocation && clickX <= (insertXLocation + drawWidth) &&
							    clickY >= insertYLocation && clickY <= (insertYLocation + drawHeight))
							{
								possibleClickedItem.mouseClick(clickX - insertXLocation, clickY - insertYLocation);
								selected = possibleClickedItem;
								break searchLoop; // Only one object can be clicked
							}
						}
					}
				}
			}
		} 
	}
	
	@Override
	public void mouseEntered(MouseEvent e) 
	{
		// Do Nothing
	}

	@Override
	public void mouseExited(MouseEvent e) 
	{
		// Do Nothing
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		// Do Nothing
	}

	@Override
	public void mouseDragged(MouseEvent e) 
	{
		// Do Nothing
	}

	/**
	 * Pass on mouse movement events to the appropriate renderable child
	 */
	@Override
	public void mouseMoved(MouseEvent e) 
	{
		int mouseX = e.getX();
		int mouseY = e.getY();
		int width = this.getWidth();
		int height = this.getHeight();
		Renderable objectMousedOver = null;
		
		// Begin at the highest-zScore-index and work down, looking for a Renderable in the click zone
		searchLoop: for (int i = highestZScoreLastRender; i >= 0; i--)
		{
			// Search through all renderables
			for (int j = 0; j < renderables.size(); j++)
			{
				Renderable possibleMousedOverItem = renderables.get(j);
				if (possibleMousedOverItem != null)
				{
					// Check if the Renderable is drawn where the mouse is currently located
					GraphicProperties itemToRenderProperties = layoutDescription.getPropertiesForID(possibleMousedOverItem.getIdentifier());
					if (itemToRenderProperties != null)
					{
						if (itemToRenderProperties.getZScore() == i)
						{
							int insertXLocation = (int)(width * itemToRenderProperties.getPosX());
							int insertYLocation = (int)(height * itemToRenderProperties.getPosY());
							int drawWidth = (int)(width * itemToRenderProperties.getWidth());
							int drawHeight = (int)(height * itemToRenderProperties.getHeight());
							if (mouseX >= insertXLocation && mouseX <= (insertXLocation + drawWidth) &&
							    mouseY >= insertYLocation && mouseY <= (insertYLocation + drawHeight))
							{
								possibleMousedOverItem.mouseOver(mouseX - insertXLocation, mouseY - insertYLocation);
								objectMousedOver = possibleMousedOverItem;
								break searchLoop; // Only one object can be moused over
							}
						}
					}
				}
			}
		}
		
		// Inform all other renderables that the mouse isn't within them
		for (int i = 0; i < renderables.size(); i++)
		{
			if (renderables.get(i) != objectMousedOver)
			{
				renderables.get(i).mouseAbsent();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent mouseEvent) 
	{
		// Program responds to the initial press, rather than the click event
		// Caused by pressing the mouse button and releasing.
	}

	@Override
	public void keyPressed(KeyEvent keyEvent) 
	{
		if (keyEvent.getKeyCode() == KeyEvent.VK_TAB)
		{
			if (selected != null)
			{
				int level = layoutDescription.getZScoreForRenderable(selected);
				for (Renderable renderable : renderables)
				{
					if (layoutDescription.getZScoreForRenderable(renderable) == level &&
							renderable != selected)
					{
						selected = renderable;
						return;
					}
				}
			}
		}
		else if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER)
		{
			for (Renderable renderable : renderables)
			{
				if (layoutDescription.getPropertiesForID(renderable.getIdentifier()).getEnterable())
				{
					
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent keyEvent) 
	{
		if (selected != null)
		{
			selected.keyReleased(keyEvent);
		}
	}

	@Override
	public void keyTyped(KeyEvent keyEvent) 
	{
		if (selected != null)
		{
			selected.keyTyped(keyEvent);
		}
	}
}
