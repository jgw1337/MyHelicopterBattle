package com.jgw.framework;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Background {
	// Image
	private BufferedImage image;
	
	// Background speed
	private double speed;
	
	// Background position
	private double xPositions[];
	private int yPosition;
	
	
	/**
	 * Initialize object for moving image
	 * 
	 * @param image		Background image
	 * @param speed		How fast and in which direction?  Negative number = moving left; positive number = moving right
	 * @param yPosition	y-coord
	 */
	public void Initialize(BufferedImage image, double speed, int yPosition) {
		this.image = image;
		this.speed = speed;
		
		this.yPosition = yPosition;
		
		// Divide frame size with image size to get how many times we need to draw image to screen
		int numberOfPositions = (Framework.frameWidth / this.image.getWidth()) + 2; // Add 2 so we do not get blank spaces between images
		xPositions = new double[numberOfPositions];
		
		// Set x-coord
		for (int i = 0; i < xPositions.length; i++) {
			xPositions[i] = i * image.getWidth();
		}
	}
	
	
	/**
	 * Moves images
	 */
	public void Update() {
		for (int i = 0; i < xPositions.length; i++) {
			// Move image
			xPositions[i] += speed;
			
			// If image moving left
			if (speed < 0) {
				if (xPositions[i] <= -image.getWidth()) {
					xPositions[i] = image.getWidth() * (xPositions.length - 1);
				}
			} else {
				if (xPositions[i] >= image.getWidth() * (xPositions.length - 1)) {
					xPositions[i] = -image.getWidth();
				}

			}
			
		}
	}
	
	
	/**
	 * Draws image to screen
	 * 
	 * @param g2d	Graphics2D
	 */
	public void Draw(Graphics2D g2d) {
		this.Update();
		
		for (int i = 0; i < xPositions.length; i++) {
			g2d.drawImage(image, (int) xPositions[i], yPosition, null);
		}
	}
}
