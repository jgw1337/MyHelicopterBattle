package com.jgw.framework;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Framework that controls game (Game.java) that created, updated, and drew it
 * on screen
 */

public class Framework extends Canvas {
	public static int frameWidth, frameHeight;

	// 1 second = 1,000,000,000 nanseconds
	public static final long secInNanosec = 1000000000L;

	// 1 millisecond = 1,000,000 nanoseconds
	public static final long millisecInNanosec = 1000000L;

	// Frames per second
	private final int GAME_FPS = 60;

	// Pause between updates (in nanosecs)
	private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;

	// Game States
	public static enum GameState {
		STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED
	}

	// Current Game State
	public static GameState gameState;

	// Elapsed game time (in nanosecs)
	private long gameTime;
	private long lastTime;

	// Actual game
	private Game game;

	public Framework() {
		super();

		gameState = GameState.VISUALIZING;

		// Start game in new thread
		Thread gameThread = new Thread() {
			@Override
			public void run() {
				GameLoop();
			}
		};
		gameThread.start();
	}

	/**
	 * Sets variables and objects
	 * <p>
	 * This method sets variables and objects for this class. Variables and
	 * objects for actual game set in Game.java
	 */
	private void Initialize() {
		// TODO Auto-generated method stub

	}

	/**
	 * Load files (images, sounds, etc.)
	 * <p>
	 * This method loads files for this class. Files for actual game set in
	 * Game.java
	 */
	private void LoadContent() {
		// TODO Auto-generated method stub

	}

	/**
	 * In specific intervals of game time (GAME_UPDATE_PERIOD), the game/logic
	 * is updated and then drawn on screen
	 */
	private void GameLoop() {
		// These two vars are used in VISUALIZING game state. We used them to
		// wait some time so we can get the correct frame/window resolution
		long visualizingTime = 0, lastVisualizingTime = System.nanoTime();

		// Vars used for calculating time that defines how long we should put
		// thread to seep to meet GAME_FPS
		long beginTime, timeTaken, timeLeft;

		while (true) {
			beginTime = System.nanoTime();

			switch (gameState) {
			case PLAYING:
				gameTime += System.nanoTime() - lastTime;
				game.updateGame(gameTime, mousePosition());
				lastTime = System.nanoTime();
				break;
			case GAMEOVER:

				break;
			case MAIN_MENU:

				break;
			case OPTIONS:

				break;
			case GAME_CONTENT_LOADING:

				break;
			case STARTING:
				// Sets variables and objects
				Initialize();
				// Load files (images, sounds, etc.)
				LoadContent();
				// Then, set game state to Main Menu
				gameState = GameState.MAIN_MENU;
				break;
			case VISUALIZING:
				// Kludge for Ubuntu OS, this.getWidth() needs to wait a sec
				if (this.getWidth() > 1 && visualizingTime > secInNanosec) {
					frameWidth = this.getWidth();
					frameHeight = this.getHeight();
					// Then, set game state to Start
					gameState = GameState.STARTING;
				} else {
					visualizingTime += System.nanoTime() - lastVisualizingTime;
					lastVisualizingTime = System.nanoTime();
				}
				break;
			}
			
			// Repaint screen
			repaint();
			
			// Calc the time that defines how long to put thread to sleep to meet GAME_FPS
			timeTaken = System.nanoTime() - beginTime;
			timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / millisecInNanosec;  // in nanosecs

			// If time is less than 10 ms, the put thread to sleep for another 10 ms so another thread can work
			if (timeLeft < 10) {
				timeLeft = 10;  // set a min value
			}
			
			try {
				// Provides the necessary delay and yields control so another thread can work
				Thread.sleep(timeLeft);
			} catch (InterruptedException e) {
				// TODO: handle exception
			}
		}
	}
	
	
	/**
	 * Draw game on screen.  It is called in the repaint() and GameLoop() methods
	 */
	@Override
	public void Draw(Graphics2D g2d) {
		switch (gameState) {
		case PLAYING:
			game.Draw(g2d, mousePosition());
			break;
		case GAMEOVER:
			
			break;
		case MAIN_MENU:
			
			break;
		case OPTIONS:
			
			break;
		case GAME_CONTENT_LOADING:
			
			break;
		}
	}
	
	
	/**
	 * Starts new game
	 */
	private void newGame() {
		// Set gameTime to zero and lastTime to current time for later calcs
		gameTime = 0;
		lastTime = System.nanoTime();
		
		game = new Game();
	}
	
	
	/**
	 * Restart game
	 * <p>Resets gameTime and calls RestartGame() method of game object so some vars are reset
	 */
	private void restartGame() {
		// Set gameTime to zero and lastTime to current time for later calcs
		gameTime = 0;
		lastTime = System.nanoTime();
		
		game.RestartGame();
		
		// Change game state so game can start
		gameState = GameState.PLAYING;
	}
	
	
	/**
	 * Returns position of mouse in frame/window.
	 * If mouse position is null, then return 0,0 coords
	 * 
	 * @return	Point of mouse coords
	 */
	private Point mousePosition() {
		try {
			Point mp = this.getMousePosition();
			
			if (mp != null) {
				return this.getMousePosition();
			} else {
				return new Point(0, 0);
			}
		} catch (Exception e) {
			return new Point(0, 0);
		}
	}
	
	/**
	 * Keyboard key released method
	 * 
	 * @param e	KeyEvent
	 */
	@Override
	public void keyReleasedFramework(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * Mouse clicked method
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
