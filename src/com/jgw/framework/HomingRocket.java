package com.jgw.framework;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class HomingRocket {
	// Rocket reload time (time that must pass before another rocket can fire)
	public static final long timeBetweenNewRockets = Framework.secInNanosec / 4;
	public static long timeOfLastCreatedRocket = 0;
	
	// Rocket damage
	public static int damagePower = 100;
	
	// Position
	public int xCoord, yCoord;
	
	// Speed and direction
	private double movingXSpeed, movingYSpeed;
	
	// Life time of current rocket smoke
	public long currentSmokeLifeTime;
	
	// Image.  Loaded and set in LoadContent() method in Game.java
	public static BufferedImage rocketImg;
	
	
	/**
	 * Set vars and objs
	 */
	public void Initialize(int xCoord, int yCoord) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		
		this.movingXSpeed = 23;
		this.movingYSpeed = 5;
		
		this.currentSmokeLifeTime = Framework.secInNanosec / 2;
	}
	
	
	/**
	 * Checks if rocket has left screen
	 */
	public boolean hasLeftScreen() {
		if (xCoord > 0 && xCoord < Framework.frameWidth) { // only need to check the x-axis
			return false;
		} else {
			return true;
		}
	}
	
	
	/**
	 * Moves rocket
	 */
	public void Update() {
		xCoord += movingXSpeed;
	}
	
	
	/**
	 * Moves rocket
	 */
	public void Follow(EnemyHelicopter eh) {
		if (this.yCoord > eh.getYCenter()) {
			yCoord -= movingYSpeed;
		} else {
			yCoord += movingYSpeed;
		}
	}
	
	
	/**
	 * Draws rocket
	 */
	public void Draw(Graphics2D g2d) {
		g2d.drawImage(rocketImg, xCoord, yCoord, null);
	}
}
