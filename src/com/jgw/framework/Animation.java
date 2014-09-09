package com.jgw.framework;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Animation {
	// Animation image
	private BufferedImage animImg;

	// Animation dimension
	private int frameWidth, frameHeight;

	// Frames in animation
	private int numberOfFrames;

	// Time between frames (in millisecs)
	private long frameTime;

	// Time when the frame started showing. (Used to calculate the time for the
	// next frame)
	private long startingFrameTime;

	// Time when we show the next frame. (When current time greater than or
	// equal to timeForNextFrame, it's time to move to the next frame)
	private long timeForNextFrame;
	
	// Current frame number
	private int currentFrameNumber;

	// Should animation loop?
	private boolean loop;

	// Coords
	public int x, y;

	// Starting x-coord of the current frame
	private int startingXOfFrameInImg;

	// Ending x-coord of the current frame
	private int endingXOfFrameInImg;

	// State of animation. (Active or finished?... so we can delete when
	// finished)
	public boolean active;

	// How long to wait before starting the animation
	private long showDelay;

	// When was the animation created?
	private long timeOfAnimationCreation;

	/**
	 * Create Animation
	 * 
	 * @param animImg			Animation image
	 * @param frameWidth		Width of "animImg"
	 * @param frameHeight		Height of "animImg"
	 * @param numberOfFrames	Number of frames in animation
	 * @param frameTime			Amount of time each frame will be shown (in millisecs)
	 * @param loop				Should animation repeat?
	 * @param x					x-coord
	 * @param y					y-coord
	 * @param showDelay			How long to wait before starting the animation (in millisecs)
	 */
	public Animation(BufferedImage animImg, int frameWidth, int frameHeight, int numberOfFrames, long frameTime, boolean loop, int x, int y, long showDelay) {
		this.animImg = animImg;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.numberOfFrames = numberOfFrames;
		this.frameTime = frameTime;
		this.loop = loop;
		
		this.x = x;
		this.y = y;
		
		this.showDelay = showDelay;
		
		timeOfAnimationCreation = System.currentTimeMillis();
		
		startingXOfFrameInImg = 0;
		endingXOfFrameInImg = frameWidth;
		
		startingFrameTime = System.currentTimeMillis() + showDelay;
		timeForNextFrame = startingFrameTime + this.frameTime;
		currentFrameNumber = 0;
		active = true;
	}
	
	
	/**
	 * Moves coords of animation
	 * 
	 * @param x		x-coord
	 * @param y		y-coord
	 */
	public void changeCoordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
	/**
	 * Checks if it's time to show the next frame of the animation... and, checks if the animation is finished
	 */
	public void Update() {
		if (timeForNextFrame <= System.currentTimeMillis()) {
			// Next frame
			currentFrameNumber++;
			
			if (currentFrameNumber >= numberOfFrames) {
				currentFrameNumber = 0;
				
				// If animation is not a loop, then set the animation to inactive
				if (!loop) {
					active = false;
				}
			}
			
			// Starting and ending coords
			startingXOfFrameInImg = currentFrameNumber * frameWidth;
			endingXOfFrameInImg = startingXOfFrameInImg + frameWidth;
			
			// Set time for next frame
			startingFrameTime = System.currentTimeMillis();
			timeForNextFrame = startingFrameTime + frameTime;
		}
	}
	
	
	/**
	 * Draws current frame
	 * 
	 * @param g2d	Graphics2D
	 */
	public void Draw(Graphics2D g2d) {
		this.Update();
		
		// Checks if the showDelay is over
		if (this.timeOfAnimationCreation + this.showDelay <= System.currentTimeMillis()) {
			g2d.drawImage(animImg, x, y, x + frameWidth, y + frameHeight, startingXOfFrameInImg, 0, endingXOfFrameInImg, frameHeight, null);
		}
	}
}
