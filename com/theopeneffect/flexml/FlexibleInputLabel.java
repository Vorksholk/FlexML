package com.theopeneffect.flexml;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class FlexibleInputLabel implements Renderable 
{
	
	private Font font = new Font("Arial", Font.PLAIN, 1);
	private Color startColor;
	private Color mouseOverColor;
	private Color currentColor;
	private String contents = "";
	
	private boolean isPassword = false;
	
	private double scale;
	
	private String identifier;
	private boolean isVisible = true;
	
	public FlexibleInputLabel(String identifier, double scale, Color startColor, Color mouseOverColor)
	{
		this.identifier = identifier;
		this.scale = scale;
		this.startColor = startColor;
		this.mouseOverColor = mouseOverColor;
		this.currentColor = this.startColor;
	}
	
	public FlexibleInputLabel(String identifier, double scale)
	{
		this(identifier, scale, Color.WHITE, Color.ORANGE);
	}
	
	public void setIsPassword(boolean isPassword)
	{
		this.isPassword = isPassword;
	}

	@Override
	public void paint(Graphics2D g, int startX, int startY, int width, int height) 
	{
		if (isVisible)
		{
			font = new Font(font.getFontName(), font.getStyle(), (int) (scale * height));
			g.setColor(currentColor);
			g.setFont(font);
			if (isPassword)
			{
				String representation = "";
				for (int i = 0; i < contents.length(); i++)
				{
					representation += "*";
				}
				g.drawString(representation, startX, startY + font.getSize());
			}
			else
			{
				g.drawString(contents, startX, startY + font.getSize());
			}
		}
	}

	@Override
	public String getIdentifier() 
	{	
		return identifier;
	}

	@Override
	public boolean getVisible() 
	{
		return isVisible;
	}

	@Override
	public void setVisible(boolean visibility)
	{
		this.isVisible = visibility;
	}

	@Override
	public void mouseOver(int x, int y) 
	{
		currentColor = mouseOverColor;
	}

	@Override
	public void mouseAbsent() 
	{
		currentColor = startColor;
	}

	@Override
	public void mouseClick(int x, int y) 
	{
	}

	@Override
	public void offScreen() 
	{
	}
	
	public String getContents()
	{
		return this.contents;
	}
	
	public void setContents(String newContents)
	{
		this.contents = newContents;
	}

	public boolean isCharacter(KeyEvent keyEvent)
	{
		char key = keyEvent.getKeyChar();
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789{}[]-_=+`~!@#$%^&*()\\|'\";:/?.>,<";
		if (characters.indexOf(key + "") > -1)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public void keyTyped(KeyEvent keyEvent) 
	{
		if (isCharacter(keyEvent))
			contents += keyEvent.getKeyChar();
	}
	
	@Override
	public void keyPressed(KeyEvent keyEvent) 
	{
	}

	@Override
	public void keyReleased(KeyEvent keyEvent)
	{
		if (keyEvent.getKeyCode() == KeyEvent.VK_DELETE || keyEvent.getKeyCode() == KeyEvent.VK_BACK_SPACE)
		{
			if (contents.length() > 0)
			{
				contents = contents.substring(0, contents.length() - 1);
			}
		}
	}

}
