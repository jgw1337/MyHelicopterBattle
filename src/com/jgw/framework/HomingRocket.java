package com.jgw.framework;

import java.awt.Color;
import java.awt.Font;
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
	private double movingXSpeed;

	// Speed and direction of homing system
	private double priorYSpeed, currentYSpeed, maxYSpeed;

	// Life time of current rocket smoke
	public long currentSmokeLifeTime;

	// Image. Loaded and set in LoadContent() method in Game.java
	public static BufferedImage rocketImg;

	/**
	 * Set vars and objs
	 */
	public void Initialize(int xCoord, int yCoord) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;

		this.movingXSpeed = 15;
		this.priorYSpeed = 0;
		this.currentYSpeed = 0;
		this.maxYSpeed = 5;

		this.currentSmokeLifeTime = Framework.secInNanosec / 2;
	}

	/**
	 * Checks if rocket has left screen
	 */
	public boolean hasLeftScreen() {
		if (xCoord > 0 && xCoord < Framework.frameWidth) { // only need to check
															// the x-axis
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
		final int RANGE = 300;
		final int BUTTER_ZONE = 100;
		final int SWEET_SPOT = 25;
		final int KILL_ZONE = 5;

		// If rocket is high but within range
		if (this.yCoord > eh.getYCenter()
				&& this.yCoord <= eh.getYCenter() + RANGE) {
			if (this.yCoord > eh.getYCenter() + BUTTER_ZONE
					|| this.xCoord + this.rocketImg.getWidth() < eh
							.getXCenter() - eh.heliBodyImg.getWidth()) {
				currentYSpeed = currentYSpeed - 0.4;
			} else if (this.yCoord > eh.getYCenter() + SWEET_SPOT) {
				currentYSpeed = -2;
			} else if (this.yCoord > eh.getYCenter() + KILL_ZONE) {
				currentYSpeed = -0.5;
			} else {
				currentYSpeed = -0.1;
			}

			if (Math.abs(currentYSpeed) > maxYSpeed) {
				currentYSpeed = -maxYSpeed;
			}

			// else if rocket is low but within range
		} else if (this.yCoord < eh.getYCenter()
				&& this.yCoord >= eh.getYCenter() - RANGE) {
			if (this.yCoord < eh.getYCenter() - BUTTER_ZONE
					|| this.xCoord + this.rocketImg.getWidth() < eh
							.getXCenter() - eh.heliBodyImg.getWidth()) {
				currentYSpeed = currentYSpeed + 0.4;
			} else if (this.yCoord < eh.getYCenter() - SWEET_SPOT) {
				currentYSpeed = 2;
			} else if (this.yCoord < eh.getYCenter() - KILL_ZONE) {
				currentYSpeed = 0.5;
			} else {
				currentYSpeed = 0.1;
			}

			if (currentYSpeed > maxYSpeed) {
				currentYSpeed = maxYSpeed;
			}

		} else if (this.yCoord == eh.getYCenter()) {
			currentYSpeed = 0.1;
		}

		yCoord += currentYSpeed;
	}

	/**
	 * Draws rocket
	 */
	public void Draw(Graphics2D g2d) {
		g2d.drawImage(rocketImg, xCoord, yCoord, null);

		/**
		 * Debugging
		 * 
		 * Font font = new Font("monospaced", Font.BOLD, 36); String tmpStr =
		 * "x: " + xCoord + " :: y: " + yCoord; g2d.setFont(font);
		 * g2d.setColor(Color.BLACK); g2d.drawString(tmpStr,
		 * (Framework.frameWidth / 2) -
		 * (g2d.getFontMetrics().stringWidth(tmpStr) / 2) + 2,
		 * Framework.frameHeight / 2); g2d.setColor(Color.WHITE);
		 * g2d.drawString(tmpStr, (Framework.frameWidth / 2) -
		 * (g2d.getFontMetrics().stringWidth(tmpStr) / 2), Framework.frameHeight
		 * / 2 - 2);
		 */
	}
}
