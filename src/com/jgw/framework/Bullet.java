package com.jgw.framework;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class Bullet {
	// For creating new bullets
	public final static long timeBetweenNewBullets = Framework.secInNanosec / 10;
	public static long timeOfLastCreatedBullet = 0;
	
	// Bullet's damage power
	public static int damagePower = 20;
	
	// Bullet position.  Must be type double because the corresponding speeds will not be whole numbers
	public double xCoord, yCoord;
	
	// Bullet speed and direction
	public static int bulletSpeed = 20;
	private double movingXSpeed, movingYSpeed;
	
	// Bullet image.  Image is loaded and set in LoadContent() method in Game.java
	public static BufferedImage bulletImg;
	
	/**
	 * Create new bullet
	 * 
	 * @param xCoord			x-coord when bullet was fired
	 * @param yCoord			y-coord when bullet was fired
	 * @param mousePosition		Position of mouse at time when bullet was shot
	 */
	public Bullet(int xCoord, int yCoord, Point mousePosition) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		
		setDirectionAndSpeed(mousePosition);
	}
	
	
	/**
	 * Calc speed on x- and y-coord
	 * 
	 * @param mousePosition
	 */
	private void setDirectionAndSpeed(Point mousePosition) {
		// Unit direction vector of bullet
		double directionVx = mousePosition.x - this.xCoord;
		double directionVy = mousePosition.y - this.yCoord;
		double lengthOfVector = Math.sqrt((directionVx * directionVx) + (directionVy * directionVy));
		directionVx = directionVx / lengthOfVector; // Unit vector
		directionVy = directionVy / lengthOfVector; // Unit vector
		
		// Set speed
		this.movingXSpeed = bulletSpeed * directionVx;
		this.movingYSpeed = bulletSpeed * directionVy;
	}
	
	
	/**
	 * Checks if bullet has left screen
	 * 
	 * @return true if bullet is off screen, false otherwise
	 */
	public boolean hasLeftScreen() {
		if (xCoord > 0 && xCoord < Framework.frameWidth && yCoord > 0 && yCoord < Framework.frameHeight) {
			return false;
		} else {
			return true;
		}
	}
	
	
	/**
	 * Move bullet
	 */
	public void Update() {
		xCoord += movingXSpeed;
		yCoord += movingYSpeed;
	}
	
	
	/**
	 * Draws bullet on screen
	 * 
	 * @param g2d	Graphics2D
	 */
	public void Draw(Graphics2D g2d) {
		g2d.drawImage(bulletImg, (int) xCoord, (int) yCoord, null);
	}
}
