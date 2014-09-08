package com.jgw.framework;

import java.awt.Graphics2D;
import java.awt.Point;

/**
 * Actual Game
 */

public class Game {
	public Game() {
		Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
		
		Thread threadForInitGame = new Thread() {
			@Override
			public void run() {
				// Sets variables and objects for game
				Initialize();
				// Load game files (images, sounds, etc.)
				LoadContent();
				
				Framework.gameState = Framework.GameState.PLAYING;
			}
		};
		threadForInitGame.start();
	}
	
	// Sets variables and objects for game
	private void Initialize() {
		// TODO Auto-generated method stub

	}
	
	// Load game files (images, sounds, etc.)
	private void LoadContent() {
		// TODO Auto-generated method stub

	}
	
	// Restart (resets some variables)
	public void RestartGame() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * <p>Update game logic
	 * 
	 * @param gameTime		gameTime of the game
	 * @param mousePosition	current mouse position
	 */
	public void updateGame(long gameTime, Point mousePosition) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * <p>Draw game on screen
	 * 
	 * @param g2d			Graphics2D
	 * @param mousePosition	current mouse position
	 */
	public void Draw(Graphics2D g2d, Point mousePosition) {
		// TODO Auto-generated method stub

	}
}
