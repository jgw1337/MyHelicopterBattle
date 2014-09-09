package com.jgw.framework;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Rocket smoke
 * Use: create object of this class, then initialize with Initialize() method
 */
public class RocketSmoke {
	// Coords
	private int xCoord, yCoord;
	
	// How long is smoke visible?
	public long smokeLifeTime;
	
	// For calculating how long smoke exists
	public long timeOfCreation;
	
	// Smoke image.  Image is loaded and set in Game class in LoadContent() method
	public static BufferedImage smokeImg;
	
	// Smoke slowly disappears and holds how much visible smoke
	public float imageTransparency;
	
	
	/**
	 * Initialize
	 * 
	 * @param xCoord		x-coord
	 * @param yCoord		y-coord
	 * @param gameTime		Current elapsed game time (in nanosecs)
	 * @param smokeLifeTime	How long smoke exists on screen
	 * @param image			Rocket smoke image
	 */
	public void Initialize(int xCoord, int yCoord, long gameTime, long smokeLifeTime) {
		this.timeOfCreation = gameTime;
		
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		
		this.smokeLifeTime = smokeLifeTime;
		
		this.imageTransparency = 1.0f;
	}
	
	
	/**
	 * Sets new transparency of smoke
	 * The older the smoke, the more transparent it is
	 * 
	 * @param gameTime		Current elapsed game time (in nanosecs)
	 */
	public void updateTransparency(long gameTime) {
		long currentLifeTime = gameTime - timeOfCreation;
		
		int currentLTInPercentages = (int) (currentLifeTime + 100 / smokeLifeTime);
		currentLTInPercentages = 100 - currentLTInPercentages;
		float rSmokeTransparency = 1.0f * (currentLTInPercentages * 0.01f);
		
		if (rSmokeTransparency > 0) {
			imageTransparency = rSmokeTransparency;
		}
	}
	
	
	/**
	 * Check if smoke is old enough to remove
	 * 
	 * @param gameTime		Current elapsed game time (in nanosecs)
	 * @return				True is smoke can be removed, false otherwise
	 */
	public boolean didSmokeDisappear(long gameTime) {
		long currentLifeTime = gameTime - timeOfCreation;
		
		if (currentLifeTime >= smokeLifeTime) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * Draws rocket smoke
	 * 
	 * @param g2d	Graphics2D
	 */
	public void Draw(Graphics2D g2d) {
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, imageTransparency));
		
		// While smoke is disappearing (imageTransparency), it is also expanding
		float imageMultiplier = 2 - imageTransparency; // Multiply smoke image with imageMultiplier so it slowly becomes bigger
		int newImgWidth = (int) (smokeImg.getWidth() * imageMultiplier);
		int newImgHeight = (int) (smokeImg.getHeight() * imageMultiplier);
		int newImgYCoord = (int) (smokeImg.getHeight()/2 * (1-imageTransparency)); // Set new y-coord so it stays in center behind rocket
		g2d.drawImage(smokeImg, xCoord, yCoord - newImgYCoord, newImgWidth, newImgHeight, null);
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		
	}
}
