package com.theopeneffect.flexml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Handles storage and retrieval of layout element properties.
 * 
 * @author Maxwell Sanchez
 *
 */
public class LayoutDescription 
{
	// InputStream to pull placement information from
	private InputStream layoutDescriptionStream;
	
	// HashMap to associate identifiers with GraphicProperties objects
	private HashMap<String, GraphicProperties> propertyMap;
	
	/**
	 * Construct a LayoutDescription which describes a layout based on the 
	 * provided layout description file URL.
	 * 
	 * @param layoutDescriptionURL The location of the file describing this layout
	 */
	public LayoutDescription(InputStream layoutDescriptionStream) 
	{
		try
		{
			// Get a FileInputStream from the provided URL
			this.layoutDescriptionStream = layoutDescriptionStream;
			
			// Create the associative map
			propertyMap = new HashMap<String, GraphicProperties>();
			
			// Read in information from the description input stream
			Scanner scanDescriptionFile = new Scanner(this.layoutDescriptionStream);
			while (scanDescriptionFile.hasNextLine())
			{
				// Default values for display elements
				String id = "";
				double posX = 0;
				double posY = 0;
				double width = 0;
				double height = 0;
				boolean enterable = false;
				int zScore = 0;
				
				// Each line should correspond to a display property
				String line = scanDescriptionFile.nextLine();
				System.out.println(line);
				
				// Double-hash is a comment
				if (line.indexOf("##") > -1)
				{
					line = line.substring(0,  line.indexOf("##"));
				}
				
				// Lines must contain < and > to be valid descriptors
				if (line.contains("<") && line.contains(">"))
				{
					// Remove the chevrons
					line = line.substring(line.indexOf("<") + 1, line.indexOf(">"));
					
					// Remove all spaces
					line = line.replaceAll(" ", "");
					
					// Position and size values separated by semicolons
					String[] parts = line.split(";");
					
					// Interpret each position or size value
					for (int i = 0; i < parts.length; i++)
					{
						// Split the name from the value
						String[] sectionsOfPart = parts[i].split(":");
						
						// Match any case value name
						sectionsOfPart[0] = sectionsOfPart[0].toLowerCase();
						
						// Associated name and value
						if (sectionsOfPart.length > 1)
						{
							if (sectionsOfPart[0].startsWith("id"))
							{
								id = sectionsOfPart[1].replaceAll("\"", "");
							}
							else if (sectionsOfPart[0].startsWith("posX".toLowerCase()))
							{
								if (isDouble(sectionsOfPart[1].replaceAll("%", "")))
								{
									posX = Double.parseDouble(sectionsOfPart[1].replaceAll("%", ""))/100;
								}
							}
							else if (sectionsOfPart[0].startsWith("posY".toLowerCase()))
							{
								if (isDouble(sectionsOfPart[1].replaceAll("%", "")))
								{
									posY = Double.parseDouble(sectionsOfPart[1].replaceAll("%", ""))/100;
								}
							}
							else if (sectionsOfPart[0].startsWith("width"))
							{
								if (isDouble(sectionsOfPart[1].replaceAll("%", "")))
								{
									width = Double.parseDouble(sectionsOfPart[1].replaceAll("%", ""))/100;
								}
							}
							else if (sectionsOfPart[0].startsWith("height"))
							{
								if (isDouble(sectionsOfPart[1].replaceAll("%", "")))
								{
									height = Double.parseDouble(sectionsOfPart[1].replaceAll("%", ""))/100;
								}
							}
							else if (sectionsOfPart[0].startsWith("zScore".toLowerCase()))
							{
								if (isInteger(sectionsOfPart[1]))
								{
									zScore = Integer.parseInt(sectionsOfPart[1]);
								}
							}
							else if (sectionsOfPart[0].startsWith("enterable".toLowerCase()))
							{
								if (isBoolean(sectionsOfPart[1]))
								{
									enterable = Boolean.parseBoolean(sectionsOfPart[1]);
								}
							}
						}
					}
				}
				// Add any id which isn't left blank
				if (!id.equals(""))
				{
					propertyMap.put(id, new GraphicProperties(posX, posY, width, height, zScore, enterable));
				}
			}
			// Close the description file resource
			scanDescriptionFile.close();
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public LayoutDescription(File file) throws FileNotFoundException
	{
		this(new FileInputStream(file));
	}
	
	/**
	 * Return the zScore of the provided renderable if it exists in this layout,
	 * otherwise return -1;
	 * 
	 * @param renderable Renderable to lookup zScore for
	 * @return zScore of provided renderable in this layout
	 */
	public int getZScoreForRenderable(Renderable renderable)
	{
		GraphicProperties properties = getPropertiesForID(renderable.getIdentifier());
		if (properties != null)
		{
			return properties.getZScore();
		}
		else
		{
			return -1;
		}
	}
	
	/**
	 * Get the GraphicProperties object describing a given identifier
	 * 
	 * @param identifier The identifier to lookup
	 * @return The GraphicProperties object associated with the provided identifier
	 */
	public GraphicProperties getPropertiesForID(String identifier)
	{
		return propertyMap.get(identifier);
	}
	
	/**
	 * Tests whether a String can be parsed to an integer
	 * 
	 * @param toTest String to test
	 * @return Whether toTest can be parsed to an integer
	 */
	public static boolean isInteger(String toTest)
	{
		try
		{
			Integer.parseInt(toTest);
			return true; // Parsing successful
		} catch (Exception e) { return false; }
	}

	/**
	 * Tests whether a String can be parsed to a double
	 * 
	 * @param toTest String to test
	 * @return Whether toTest can be parsed to an double
	 */
	public static boolean isDouble(String toTest)
	{
		try
		{
			Double.parseDouble(toTest);
			return true; // Parsing successful
		} catch (Exception e) { return false; }
	}
	
	/**
	 * Tests whether a String can be parsed to a boolean
	 * 
	 * @param toTest String to test
	 * @return Whether toTest can be parsed to a boolean
	 */
	public static boolean isBoolean(String toTest)
	{
		try
		{
			Boolean.parseBoolean(toTest);
			return true;
		} catch (Exception e) { return false; }
	}
}
