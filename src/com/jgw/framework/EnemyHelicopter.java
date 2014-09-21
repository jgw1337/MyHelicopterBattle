package com.jgw.framework;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class EnemyHelicopter {
	// For creating new enemies
	private static final long timeBetweenNewEnemiesInit = Framework.secInNanosec * 3;
	public static long timeBetweenNewEnemies = timeBetweenNewEnemiesInit;
	public static long timeOfLastCreatedEnemy = 0;
	
	// Helicopter health
	public int health;
	
	// Helicopter position
	public int xCoord, yCoord;
	
	// Helicopter's center coords
	public int xCenter, yCenter;
	
	// Moving speed and direction
	private static final double movingXSpeedInit = -4;
	private static double movingXSpeed = movingXSpeedInit;
	
	// Helicopter images.  Images are loaded and set in LoadContent() method in Game.java
	public static BufferedImage heliBodyImg, heliFrontPropellerAnimImg, heliRearPropellerAnimImg;
	
	// Animation of helicopter propeller
	private Animation heliFrontPropellerAnim, heliRearPropellerAnim;
	
	// Propeller position offset from main image
	private static int offsetXFrontPropeller = 4;
	private static int offsetYFrontPropeller = -7;
	private static int offsetXRearPropeller = 205;
	private static int offsetYRearPropeller = 6;
	
	
	/**
	 * Enemy helicopter
	 * 
	 * @param xCoord						Starting x-coord
	 * @param yCoord						Starting y-coord
	 * @param heliBodyImg					Helicopter image
	 * @param heliFrontPropellerAnimImg		Image of front propeller 
	 * @param heliRearPropellerAnimImg		Image of rear propeller 
	 */
	public void Initialize(int xCoord, int yCoord) {
		health = 100;
		
		// Enemy position
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		
		// Enemy center
		this.xCenter = xCoord + heliBodyImg.getWidth()/2;
		this.yCenter = yCoord + heliBodyImg.getHeight()/2;
		
		// Animation
		heliFrontPropellerAnim = new Animation(heliFrontPropellerAnimImg, 158, 16, 3, 20, true, xCoord + offsetXFrontPropeller, yCoord + offsetYFrontPropeller, 0);
		heliRearPropellerAnim = new Animation(heliRearPropellerAnimImg, 47, 47, 10, 10, true, xCoord + offsetXRearPropeller, yCoord + offsetYRearPropeller, 0);
		
		// Speed and direction
		EnemyHelicopter.movingXSpeed = -4;
	}
	
	
	/**
	 * Sets speed and time between enemies to initial properties 
	 */
	public static void RestartEnemy() {
		EnemyHelicopter.timeBetweenNewEnemies = timeBetweenNewEnemies;
		EnemyHelicopter.timeOfLastCreatedEnemy = 0;
		EnemyHelicopter.movingXSpeed = movingXSpeed;
	}
	
	
	/**
	 * Increase enemy speed and time between new enemies
	 */
	public static void speedUp() {
		if (EnemyHelicopter.timeBetweenNewEnemies > Framework.secInNanosec) {
			EnemyHelicopter.timeBetweenNewEnemies -= Framework.secInNanosec / 100;
		}
		
		EnemyHelicopter.movingXSpeed -= 0.25;
	}
	
	
	/**
	 * Has the enemy left the screen?
	 * 
	 * @return true if enemy has left screen, false otherwise
	 */
	public boolean hasLeftScreen() {
		if (xCoord < 0 - heliBodyImg.getWidth()) { // When entire helicopter image if offscreen
			return true;
		} else {
			return false;
		}
	}
	
	
	/*
	 * Updates helicopter
	 */
	public void Update() {
		// Move enemy on x-coord
		xCoord += movingXSpeed;
		
		// Move animated propellers along with helicopter
		heliFrontPropellerAnim.changeCoordinates(xCoord + offsetXFrontPropeller, yCoord + offsetYFrontPropeller);
		heliRearPropellerAnim.changeCoordinates(xCoord + offsetXRearPropeller, yCoord + offsetYRearPropeller);
	}
	
	
	/**
	 * Draws helicopter
	 * 
	 * @param g2d	Graphics2D
	 */
	public void Draw(Graphics2D g2d) {
		heliFrontPropellerAnim.Draw(g2d);
		g2d.drawImage(heliBodyImg, xCoord, yCoord, null);
		heliRearPropellerAnim.Draw(g2d);
	}


	public int getXCenter() {
		return xCenter;
	}


	public int getYCenter() {
		return yCenter;
	}
}
