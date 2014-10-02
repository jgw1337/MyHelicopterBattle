package com.jgw.framework;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * Actual Game
 */

public class Game {
	// Random number
	private Random rand;

	// Use this to set mouse position
	private Robot robot;

	// Player
	private HeroHelicopter player;

	// Enemies
	private ArrayList<EnemyHelicopter> enemyHelicopterList = new ArrayList<EnemyHelicopter>();

	// Explosions
	private ArrayList<Animation> explosionList;
	private BufferedImage explosionAnimImg;

	// Bullets
	private ArrayList<Bullet> bulletList;

	// Rockets
	private ArrayList<Rocket> rocketList;
	private ArrayList<HomingRocket> homingRocketList;
	// Rocket smoke
	private ArrayList<RocketSmoke> rocketSmokeList;

	// Sky
	private BufferedImage skyColorImg;

	// Clouds
	private BufferedImage cloudLayer1Img, cloudLayer2Img;

	// Mountains and ground
	private BufferedImage mountainsImg, groundImg;

	// Objects of moving images
	private Background cloudLayer1Moving, cloudLayer2Moving, mountainsMoving,
			groundMoving;

	// Mouse cursor
	private BufferedImage mouseCursorImg;

	// Font
	private Font font;

	// Statistics (enemies killed and ranaway)
	private int enemiesRanaway, enemiesKilled;

	// Temporary String (placeholder)
	private String tmpStr;

	public Game(final KeyEvent heliStyle) {
		Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;

		Thread threadForInitGame = new Thread() {
			@Override
			public void run() {
				// Sets variables and objects for game
				Initialize(heliStyle);
				// Load game files (images, sounds, etc.)
				LoadContent();

				Framework.gameState = Framework.GameState.PLAYING;
			}
		};
		threadForInitGame.start();
	}

	// Sets variables and objects for game
	private void Initialize(KeyEvent heliStyle) {
		rand = new Random();

		try {
			robot = new Robot();
		} catch (AWTException ex) {
			Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
		}

		player = new HeroHelicopter(Framework.frameWidth / 4,
				Framework.frameHeight / 4, heliStyle);

		enemyHelicopterList = new ArrayList<EnemyHelicopter>();

		explosionList = new ArrayList<Animation>();

		bulletList = new ArrayList<Bullet>();

		rocketList = new ArrayList<Rocket>();
		homingRocketList = new ArrayList<HomingRocket>();
		rocketSmokeList = new ArrayList<RocketSmoke>();

		// Moving images
		cloudLayer1Moving = new Background();
		cloudLayer2Moving = new Background();
		mountainsMoving = new Background();
		groundMoving = new Background();

		font = new Font("monospaced", Font.BOLD, 36);

		enemiesKilled = 0;
		enemiesRanaway = 0;
	}

	// Load game files (images, sounds, etc.)
	private void LoadContent() {
		try {
			// Environment
			URL skyColorImgUrl = this.getClass().getResource(
					"data/sky_color.jpg");
			skyColorImg = ImageIO.read(skyColorImgUrl);
			URL cloudLayer1ImgUrl = this.getClass().getResource(
					"data/cloud_layer_1.png");
			cloudLayer1Img = ImageIO.read(cloudLayer1ImgUrl);
			URL cloudLayer2ImgUrl = this.getClass().getResource(
					"data/cloud_layer_2.png");
			cloudLayer2Img = ImageIO.read(cloudLayer2ImgUrl);
			URL mountainsImgUrl = this.getClass().getResource(
					"data/mountains.png");
			mountainsImg = ImageIO.read(mountainsImgUrl);
			URL groundImgUrl = this.getClass().getResource("data/ground.png");
			groundImg = ImageIO.read(groundImgUrl);

			// Enemy and its propeller animation
			URL heliBodyImgUrl = this.getClass().getResource(
					"data/2_helicopter_body.png");
			EnemyHelicopter.heliBodyImg = ImageIO.read(heliBodyImgUrl);
			URL heliFrontPropellerAnimImgUrl = this.getClass().getResource(
					"data/2_front_propeller_anim.png");
			EnemyHelicopter.heliFrontPropellerAnimImg = ImageIO
					.read(heliFrontPropellerAnimImgUrl);
			URL heliRearPropellerAnimImgUrl = this.getClass().getResource(
					"data/2_rear_propeller_anim.png");
			EnemyHelicopter.heliRearPropellerAnimImg = ImageIO
					.read(heliRearPropellerAnimImgUrl);

			// Rocket and its smoke
			URL rocketImgUrl = this.getClass().getResource("data/rocket.png");
			Rocket.rocketImg = ImageIO.read(rocketImgUrl);
			URL rocketSmokeImgUrl = this.getClass().getResource(
					"data/rocket_smoke.png");
			RocketSmoke.smokeImg = ImageIO.read(rocketSmokeImgUrl);

			// Homing Rocket
			URL homingRocketImgUrl = this.getClass().getResource(
					"data/rocket.png");
			HomingRocket.rocketImg = ImageIO.read(homingRocketImgUrl);

			// Explosion animation
			URL explosionAnimImgUrl = this.getClass().getResource(
					"data/explosion_anim.png");
			explosionAnimImg = ImageIO.read(explosionAnimImgUrl);

			// Mouse cursor
			URL mouseCursorImgUrl = this.getClass().getResource(
					"data/mouse_cursor.png");
			mouseCursorImg = ImageIO.read(mouseCursorImgUrl);

			// Bullet
			URL bulletImgUrl = this.getClass().getResource("data/bullet.png");
			Bullet.bulletImg = ImageIO.read(bulletImgUrl);
		} catch (IOException ex) {
			Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
		}

		// Now that images are loaded, we can move them
		cloudLayer1Moving.Initialize(cloudLayer1Img, -6, 0);
		cloudLayer2Moving.Initialize(cloudLayer2Img, -2, 0);
		mountainsMoving.Initialize(mountainsImg, -1, Framework.frameHeight
				- groundImg.getHeight() - mountainsImg.getHeight() + 40);
		groundMoving.Initialize(groundImg, -1.2, Framework.frameHeight
				- groundImg.getHeight());
	}

	// Restart (resets some variables)
	public void RestartGame() {
		player.Reset(Framework.frameWidth / 4, Framework.frameHeight / 4);

		EnemyHelicopter.RestartEnemy();

		Bullet.timeOfLastCreatedBullet = 0;
		Rocket.timeOfLastCreatedRocket = 0;
		HomingRocket.timeOfLastCreatedRocket = 0;

		// Empty all lists
		enemyHelicopterList.clear();
		bulletList.clear();
		rocketList.clear();
		homingRocketList.clear();
		rocketSmokeList.clear();
		explosionList.clear();

		// Statistics
		enemiesKilled = 0;
		enemiesRanaway = 0;
	}

	/**
	 * <p>
	 * Update game logic
	 * 
	 * @param gameTime
	 *            gameTime of the game
	 * @param mousePosition
	 *            current mouse position
	 */
	public void updateGame(long gameTime, Point mousePosition) {
		/**
		 * Player
		 */
		// When player is destroyed and all explosions animation is complete,
		// change the game status
		if (!isPlayerAlive() && explosionList.isEmpty()) {
			Framework.gameState = Framework.GameState.GAMEOVER;
			return; // If player is destroyed, no need to do anything below
		}

		// When player exhausts rockets and bullets and all lists (bullets,
		// rockets, explosions) are empty, end the game
		if (player.numberOfBullets <= 0 && player.numberOfRockets <= 0
				&& player.numberOfHomingRockets <= 0 && bulletList.isEmpty()
				&& rocketList.isEmpty() && homingRocketList.isEmpty()
				&& rocketSmokeList.isEmpty() && explosionList.isEmpty()) {
			Framework.gameState = Framework.GameState.GAMEOVER;
			return; // If player is destroyed, no need to do anything below
		}

		// If player is alive, update
		if (isPlayerAlive()) {
			isPlayerShooting(gameTime, mousePosition);
			didPlayerFireRocket(gameTime);
			didPlayerFireHomingRocket(gameTime);
			player.setVelocity();
			player.Update();
		}

		/**
		 * Mouse
		 */
		if (player.style == "frank" || player.style == "marcus") {
			limitMousePosition(mousePosition);
		}

		/**
		 * Bullets
		 */
		updateBullets();

		/**
		 * Rockets
		 */
		// Also checks for collisions (hitting the enemy)
		updateRockets(gameTime);
		// Also checks for collisions (hitting the enemy)
		updateHomingRockets(gameTime);
		updateRocketSmoke(gameTime);

		/**
		 * Enemies
		 */
		createEnemyHelicopter(gameTime);
		updateEnemies();

		/**
		 * Explosions
		 */
		updateExplosions();
	}

	/**
	 * <p>
	 * Draw game on screen
	 * 
	 * @param g2d
	 *            Graphics2D
	 * @param mousePosition
	 *            current mouse position
	 */
	public void Draw(Graphics2D g2d, Point mousePosition, long gameTime) {
		// Sky
		g2d.drawImage(skyColorImg, 0, 0, Framework.frameWidth,
				Framework.frameHeight, null);

		// Moving images behind helicopter
		mountainsMoving.Draw(g2d);
		groundMoving.Draw(g2d);
		cloudLayer2Moving.Draw(g2d);

		// Player
		if (isPlayerAlive()) {
			player.Draw(g2d);
		}

		// Enemies
		for (int i = 0; i < enemyHelicopterList.size(); i++) {
			enemyHelicopterList.get(i).Draw(g2d);
		}

		// Bullets
		for (int i = 0; i < bulletList.size(); i++) {
			bulletList.get(i).Draw(g2d);
		}

		// Rockets
		for (int i = 0; i < rocketList.size(); i++) {
			rocketList.get(i).Draw(g2d);
		}

		// Homing Rockets
		for (int i = 0; i < homingRocketList.size(); i++) {
			homingRocketList.get(i).Draw(g2d);
		}

		// Rocket smoke
		for (int i = 0; i < rocketSmokeList.size(); i++) {
			rocketSmokeList.get(i).Draw(g2d);
		}

		// Explosions
		for (int i = 0; i < explosionList.size(); i++) {
			explosionList.get(i).Draw(g2d);
		}

		// Stats
		g2d.setFont(font);

		tmpStr = formatTime(gameTime);
		g2d.setColor(Color.DARK_GRAY);
		g2d.drawString(tmpStr, (Framework.frameWidth / 2)
				- (g2d.getFontMetrics().stringWidth(tmpStr) / 2) + 2, 41);
		g2d.setColor(Color.WHITE);
		g2d.drawString(tmpStr, (Framework.frameWidth / 2)
				- (g2d.getFontMetrics().stringWidth(tmpStr) / 2), 41 - 2);

		tmpStr = "HEALTH: " + player.health;
		g2d.setColor(Color.DARK_GRAY);
		g2d.drawString(tmpStr, 10 + 2, 40);
		g2d.setColor(Color.GREEN);
		g2d.drawString(tmpStr, 10, 40 - 2);

		tmpStr = "DESTROYED: " + enemiesKilled;
		g2d.setColor(Color.DARK_GRAY);
		g2d.drawString(tmpStr, 10 + 2, 80);
		g2d.setColor(Color.WHITE);
		g2d.drawString(tmpStr, 10, 80 - 2);

		tmpStr = "RANAWAY: " + enemiesRanaway;
		g2d.setColor(Color.DARK_GRAY);
		g2d.drawString(tmpStr, 10 + 2, 120);
		g2d.setColor(Color.WHITE);
		g2d.drawString(tmpStr, 10, 120 - 2);

		tmpStr = "ROCKETS: " + player.numberOfRockets;
		g2d.setColor(Color.DARK_GRAY);
		g2d.drawString(tmpStr, 10 + 2, 160);
		g2d.setColor(Color.WHITE);
		g2d.drawString(tmpStr, 10, 160 - 2);

		tmpStr = "HOMING ROCKETS: " + player.numberOfHomingRockets;
		g2d.setColor(Color.DARK_GRAY);
		g2d.drawString(tmpStr, 10 + 2, 200);
		g2d.setColor(Color.WHITE);
		g2d.drawString(tmpStr, 10, 200 - 2);

		tmpStr = "AMMO: " + player.numberOfBullets;
		g2d.setColor(Color.DARK_GRAY);
		g2d.drawString(tmpStr, 10 + 2, 240);
		g2d.setColor(Color.WHITE);
		g2d.drawString(tmpStr, 10, 240 - 2);

		// Moving images in front of helicopter
		cloudLayer1Moving.Draw(g2d);

		// Mouse cursor
		if (isPlayerAlive()) {
			drawRotatedMouseCursor(g2d, mousePosition);
		}
	}

	/**
	 * Draw Game Statistics when Game Over
	 * 
	 * @param g2d
	 *            Graphics2D
	 * @param gameTime
	 *            Elapsed game time
	 */
	public void DrawStatistics(Graphics2D g2d, long gameTime) {
		g2d.setFont(font);
		tmpStr = "Time: " + formatTime(gameTime);
		g2d.drawString(tmpStr, (Framework.frameWidth / 2)
				- (g2d.getFontMetrics().stringWidth(tmpStr) / 2),
				Framework.frameHeight / 3 + 100);
		tmpStr = "Rockets left: " + player.numberOfRockets;
		g2d.drawString(tmpStr, (Framework.frameWidth / 2)
				- (g2d.getFontMetrics().stringWidth(tmpStr) / 2),
				Framework.frameHeight / 3 + 140);
		tmpStr = "Ammo left: " + player.numberOfBullets;
		g2d.drawString(tmpStr, (Framework.frameWidth / 2)
				- (g2d.getFontMetrics().stringWidth(tmpStr) / 2),
				Framework.frameHeight / 3 + 180);
		tmpStr = "Enemies Killed: " + enemiesKilled;
		g2d.drawString(tmpStr, (Framework.frameWidth / 2)
				- (g2d.getFontMetrics().stringWidth(tmpStr) / 2),
				Framework.frameHeight / 3 + 220);
		tmpStr = "Enemies Fled: " + enemiesRanaway;
		g2d.drawString(tmpStr, (Framework.frameWidth / 2)
				- (g2d.getFontMetrics().stringWidth(tmpStr) / 2),
				Framework.frameHeight / 3 + 260);

		tmpStr = "STATISTICS: ";
		g2d.setColor(Color.BLACK);
		g2d.drawString(tmpStr, (Framework.frameWidth / 2)
				- (g2d.getFontMetrics().stringWidth(tmpStr) / 2) + 2,
				Framework.frameHeight / 3 + 60);
		g2d.setColor(Color.WHITE);
		g2d.drawString(tmpStr, (Framework.frameWidth / 2)
				- (g2d.getFontMetrics().stringWidth(tmpStr) / 2),
				Framework.frameHeight / 3 + 60 - 2);
	}

	/**
	 * Draws rotated mouse cursor Rotates cursor image based on player machine
	 * gun
	 * 
	 * @param g2d
	 *            Graphics2D
	 * @param mousePosition
	 *            Position of mouse
	 */
	private void drawRotatedMouseCursor(Graphics2D g2d, Point mousePosition) {
		double RIGHT_ANGLE_RADIANS = Math.PI / 2;

		// Position of gun
		int pivotX = player.gunXCoord;
		int pivotY = player.gunYCoord;

		int a = pivotX - mousePosition.x;
		int b = pivotY - mousePosition.y;
		double ab = (double) a / (double) b;
		double alfaAngleRadians = Math.atan(ab);

		if (mousePosition.y < pivotY) { // Above helicopter
			alfaAngleRadians = RIGHT_ANGLE_RADIANS - alfaAngleRadians
					- (RIGHT_ANGLE_RADIANS * 2);
		} else if (mousePosition.y > pivotY) { // Under helicopter
			alfaAngleRadians = RIGHT_ANGLE_RADIANS - alfaAngleRadians;
		} else {
			alfaAngleRadians = 0;
		}

		AffineTransform origXForm = g2d.getTransform();
		AffineTransform newXForm = (AffineTransform) (origXForm.clone());

		newXForm.rotate(alfaAngleRadians, mousePosition.x, mousePosition.y);
		g2d.setTransform(newXForm);

		// Subtract half of cursor image so it will be drawn in the center of
		// the mouse's y-coord
		g2d.drawImage(mouseCursorImg, mousePosition.x, mousePosition.y
				- mouseCursorImg.getHeight() / 2, null);

		g2d.setTransform(origXForm);
	}

	/**
	 * Format time to 00:00
	 * 
	 * @param time
	 *            Time (in nansecs)
	 * @return time in 0000 format
	 */
	private static String formatTime(long time) {
		// Time (in secs)
		int sec = (int) (time / Framework.millisecInNanosec / 1000);

		// Time (in mins and secs)
		int min = sec / 60;
		sec = sec - (min * 60);

		String minStr, secStr;

		minStr = Integer.toString(min);
		if (min <= 9) {
			minStr = "0" + minStr;
		}

		secStr = Integer.toString(sec);
		if (sec <= 9) {
			secStr = "0" + secStr;
		}

		return minStr + ":" + secStr;
	}

	/**
	 * 
	 * 
	 * METHODS FOR UPDATING GAME
	 * 
	 * 
	 * 
	 */

	/**
	 * Check is player is alive. If not, set GAME OVER
	 * 
	 * @return true is player is alive, false otherwise
	 */
	private boolean isPlayerAlive() {
		if (player.health <= 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Checks if player is shooting and creates bullets
	 */
	private void isPlayerShooting(long gameTime, Point mousePosition) {
		if (player.isShooting(gameTime)) {
			Bullet.timeOfLastCreatedBullet = gameTime;
			player.numberOfBullets--;

			Bullet b = new Bullet(player.gunXCoord, player.gunYCoord,
					mousePosition);
			bulletList.add(b);
		}
	}

	/**
	 * Checks if player is launching rockets and creates rockets
	 */
	private void didPlayerFireRocket(long gameTime) {
		if (player.isFiringRocket(gameTime)) {
			Rocket.timeOfLastCreatedRocket = gameTime;
			player.numberOfRockets--;

			Rocket r = new Rocket();
			r.Initialize(player.rocketLauncherXCoord,
					player.rocketLauncherYCoord);
			rocketList.add(r);
		}
	}

	/**
	 * Checks if player is launching rockets and creates rockets
	 */
	private void didPlayerFireHomingRocket(long gameTime) {
		if (player.isFiringHomingRocket(gameTime)) {
			HomingRocket.timeOfLastCreatedRocket = gameTime;
			player.numberOfHomingRockets--;

			HomingRocket hr = new HomingRocket();
			hr.Initialize(player.rocketLauncherXCoord,
					player.rocketLauncherYCoord);
			homingRocketList.add(hr);
		}
	}

	/**
	 * Creates new enemy if it's time
	 */
	private void createEnemyHelicopter(long gameTime) {
		if (gameTime - EnemyHelicopter.timeOfLastCreatedEnemy >= EnemyHelicopter.timeBetweenNewEnemies) {
			EnemyHelicopter eh = new EnemyHelicopter();
			int xCoord = Framework.frameWidth;
			int yCoord = rand.nextInt(Framework.frameHeight
					- EnemyHelicopter.heliBodyImg.getHeight());
			eh.Initialize(xCoord, yCoord);

			// Add created enemy to list of enemies
			enemyHelicopterList.add(eh);

			// Speed up enemy speed and creation
			EnemyHelicopter.speedUp();

			// Set new time for last created enemy
			EnemyHelicopter.timeOfLastCreatedEnemy = gameTime;
		}
	}

	/**
	 * Updates all enemies <li>Moves helicopter and checks if it's offscreen <li>
	 * Updates animations <li>Checks if enemy was killed <li>Checks if enemy
	 * collides with player
	 */
	private void updateEnemies() {
		for (int i = 0; i < enemyHelicopterList.size(); i++) {
			EnemyHelicopter eh = enemyHelicopterList.get(i);

			eh.Update();

			// Collided with player?
			Rectangle playerRectangle = new Rectangle(player.xCoord,
					player.yCoord, player.heliBodyImg.getWidth(),
					player.heliBodyImg.getHeight());
			Rectangle enemyRectangle = new Rectangle(eh.xCoord, eh.yCoord,
					EnemyHelicopter.heliBodyImg.getWidth(),
					EnemyHelicopter.heliBodyImg.getHeight());

			if (playerRectangle.intersects(enemyRectangle)) {
				player.health -= 50;

				// Remove enemy helicopter
				enemyHelicopterList.remove(i);

				// Player explosion
				for (int j = 0; j < 3; j++) {
					Animation expAnim = new Animation(explosionAnimImg, 134,
							134, 12, 45, false, player.xCoord + i * 60,
							player.yCoord - rand.nextInt(100), i * 200
									+ rand.nextInt(100));
					explosionList.add(expAnim);
				}

				// Enemy explosion
				for (int j = 0; j < 3; j++) {
					Animation expAnim = new Animation(explosionAnimImg, 134,
							134, 12, 45, false, eh.xCoord + i * 60, eh.yCoord
									- rand.nextInt(100), i * 200
									+ rand.nextInt(100));
					explosionList.add(expAnim);
				}

				// Since game over, we do not need to check other enemies
				// break;
			}

			// Enemy health
			if (eh.health <= 0) {
				// Enemy explosion
				// Substring 1/3 of explosion image height so explosion is drawn
				// closer to the center of the helicopter
				Animation expAnim = new Animation(explosionAnimImg, 134, 134,
						12, 45, false, eh.xCoord, eh.yCoord
								- explosionAnimImg.getHeight() / 3, 0);
				explosionList.add(expAnim);

				// Increment enemy killed counter
				enemiesKilled++;

				// Remove killed enemy from list
				enemyHelicopterList.remove(i);

				// This enemy was killed so skip to the next enemy
				continue;
			}

			// Enemy offscreen
			if (eh.hasLeftScreen()) {
				enemyHelicopterList.remove(i);
				enemiesRanaway++;
			}
		}
	}

	/**
	 * Update bullets <li>Moves bullets <li>Checks if bullets are offscreen <li>
	 * Checks if bullets hit the enemy
	 */
	private void updateBullets() {
		for (int i = 0; i < bulletList.size(); i++) {
			Bullet b = bulletList.get(i);

			// Move bullet
			b.Update();

			// Bullet offscreen?
			if (b.hasLeftScreen()) {
				bulletList.remove(i);
				// This bullet is offscreen so move onto the next bullet
				continue;
			}

			// Does the bullet hit?...
			Rectangle bulletRectangle = new Rectangle((int) b.xCoord,
					(int) b.yCoord, Bullet.bulletImg.getWidth(),
					Bullet.bulletImg.getHeight());
			// ...any on-screen enemy?
			for (int j = 0; j < enemyHelicopterList.size(); j++) {
				EnemyHelicopter eh = enemyHelicopterList.get(j);

				// Current enemy
				Rectangle enemeyRectangle = new Rectangle(eh.xCoord, eh.yCoord,
						EnemyHelicopter.heliBodyImg.getWidth(),
						EnemyHelicopter.heliBodyImg.getHeight());

				// Is current bullet over current enemy?
				if (bulletRectangle.intersects(enemeyRectangle)) {
					// Reduce enemy health
					eh.health -= Bullet.damagePower;

					// Bullet was also destroyed
					bulletList.remove(i);

					// This bullet is done so skip to the next bullet
					break;
				}
			}
		}
	}

	/**
	 * Update rockets <li>Moves rocket and adds trailing smoke <li>Checks if
	 * rockets are offscreen <li>Checks if rocket hit the enemy
	 * 
	 * @param gameTime
	 */
	private void updateRockets(long gameTime) {
		for (int i = 0; i < rocketList.size(); i++) {
			Rocket r = rocketList.get(i);

			// Move rocket
			r.Update();

			// Rocket offscreen?
			if (r.hasLeftScreen()) {
				rocketList.remove(i);
				// This rocket is offscreen so move onto the next rocket
				continue;
			}

			// Create rocket smoke
			RocketSmoke rs = new RocketSmoke();
			// Subtract size of rocket smoke image so smoke does not start in
			// the middle of the rocket image
			int xCoord = r.xCoord - RocketSmoke.smokeImg.getWidth();
			// Subtract 5 so smoke will be at the middle of the rocket on
			// y-axis. Randomly add number between 0 and 6 so smoke line isn't a
			// straight line
			int yCoord = r.yCoord - 5 + rand.nextInt(6);
			rs.Initialize(xCoord, yCoord, gameTime, r.currentSmokeLifeTime);
			rocketSmokeList.add(rs);

			// Because rocket is fast, we get empty space between smokes... so
			// we add more smoke.
			// The fast the rocket's speed, the larger the empty space
			// Draw this smoke a little bit ahead of the one we drew before
			int smokePositionX = 5 + rand.nextInt(8);
			rs = new RocketSmoke();
			// Add so smoke will not be on same x-coord as previous smoke.
			// First, add 5 because a random numer between 0 and 8 could be 0
			// (which would mean it's on the same x-coord as previous).
			xCoord = r.xCoord - RocketSmoke.smokeImg.getWidth()
					+ smokePositionX;
			// Subtract 5 so smoke will be at middle of rocket on y-axis
			yCoord = r.yCoord - 5 + rand.nextInt(6);
			rs.Initialize(xCoord, yCoord, gameTime, r.currentSmokeLifeTime);
			rocketSmokeList.add(rs);

			// Increate life time for next piece of rocket smoke
			r.currentSmokeLifeTime *= 1.02;

			// Rocket hits enemy?
			if (didRocketHitEnemy(r)) {
				// Rocket was destroyed too so remove it
				rocketList.remove(i);
			}
		}
	}

	/**
	 * Update rockets <li>Moves rocket and adds trailing smoke <li>Checks if
	 * rockets are offscreen <li>Checks if rocket hit the enemy
	 * 
	 * @param gameTime
	 */
	private void updateHomingRockets(long gameTime) {
		for (int i = 0; i < homingRocketList.size(); i++) {
			HomingRocket hr = homingRocketList.get(i);

			// Move homing rocket
			hr.Update();

			// Seek target
			if (enemyHelicopterList.size() > 0) {
				for (int j = 0; j < enemyHelicopterList.size(); j++) {
					EnemyHelicopter eh = enemyHelicopterList.get(j);

					// If rocket's nose is in front of the enemy helicopter's
					// tail, then...
					if (hr.xCoord + hr.rocketImg.getWidth() < eh.xCoord
							+ eh.heliBodyImg.getWidth()) {
						// ...seek the enemy helicopter
						hr.Follow(eh);
						break;
					}
				}
			}

			// Rocket offscreen?
			if (hr.hasLeftScreen()) {
				homingRocketList.remove(i);
				// This rocket is offscreen so move onto the next rocket
				continue;
			}

			// Create rocket smoke
			RocketSmoke rs = new RocketSmoke();
			// Subtract size of rocket smoke image so smoke does not start in
			// the middle of the rocket image
			int xCoord = hr.xCoord - RocketSmoke.smokeImg.getWidth();
			// Subtract 5 so smoke will be at the middle of the rocket on
			// y-axis. Randomly add number between 0 and 6 so smoke line isn't a
			// straight line
			int yCoord = hr.yCoord - 5 + rand.nextInt(6);
			rs.Initialize(xCoord, yCoord, gameTime, hr.currentSmokeLifeTime);
			rocketSmokeList.add(rs);

			// Because rocket is fast, we get empty space between smokes... so
			// we add more smoke.
			// The fast the rocket's speed, the larger the empty space
			// Draw this smoke a little bit ahead of the one we drew before
			int smokePositionX = 5 + rand.nextInt(8);
			rs = new RocketSmoke();
			// Add so smoke will not be on same x-coord as previous smoke.
			// First, add 5 because a random numer between 0 and 8 could be 0
			// (which would mean it's on the same x-coord as previous).
			xCoord = hr.xCoord - RocketSmoke.smokeImg.getWidth()
					+ smokePositionX;
			// Subtract 5 so smoke will be at middle of rocket on y-axis
			yCoord = hr.yCoord - 5 + rand.nextInt(6);
			rs.Initialize(xCoord, yCoord, gameTime, hr.currentSmokeLifeTime);
			rocketSmokeList.add(rs);

			// Increate life time for next piece of rocket smoke
			hr.currentSmokeLifeTime *= 1.02;

			// Rocket hits enemy?
			if (didHomingRocketHitEnemy(hr)) {
				// Rocket was destroyed too so remove it
				homingRocketList.remove(i);
			}
		}
	}

	/**
	 * Checks if the given rocket hit any enemy
	 * 
	 * @param rocket
	 *            Rocket to check
	 * @return true if any enemy is hit, false otherwise
	 */
	private boolean didRocketHitEnemy(Rocket rocket) {
		boolean didItHitEnemy = false;

		// Current rocket's rectangle
		// Use "2" vice "rocketImg.getWidth()" so the rocket is over the
		// helicopter to detect collision (since actual helicopter image is not
		// a rectangle
		Rectangle rocketRectangle = new Rectangle(rocket.xCoord, rocket.yCoord,
				2, Rocket.rocketImg.getHeight());

		// Loop through all enemies
		for (int i = 0; i < enemyHelicopterList.size(); i++) {
			EnemyHelicopter eh = enemyHelicopterList.get(i);

			// Current enemy rectangle
			Rectangle enemyRectangle = new Rectangle(eh.xCoord, eh.yCoord,
					EnemyHelicopter.heliBodyImg.getWidth(),
					EnemyHelicopter.heliBodyImg.getHeight());

			// Is current rocket over current enemy?
			if (rocketRectangle.intersects(enemyRectangle)) {
				didItHitEnemy = true;

				// Rocket hit enemy so reduce enemy's health
				eh.health -= Rocket.damagePower;

				// This rocket hit an enemy so skip to the next rocket
				break;
			}
		}

		return didItHitEnemy;
	}

	/**
	 * Checks if the given rocket hit any enemy
	 * 
	 * @param rocket
	 *            Rocket to check
	 * @return true if any enemy is hit, false otherwise
	 */
	private boolean didHomingRocketHitEnemy(HomingRocket homingRocket) {
		boolean didItHitEnemy = false;

		// Current rocket's rectangle
		// Use "2" vice "rocketImg.getWidth()" so the rocket is over the
		// helicopter to detect collision (since actual helicopter image is not
		// a rectangle
		Rectangle rocketRectangle = new Rectangle(homingRocket.xCoord,
				homingRocket.yCoord, 2, HomingRocket.rocketImg.getHeight());

		// Loop through all enemies
		for (int i = 0; i < enemyHelicopterList.size(); i++) {
			EnemyHelicopter eh = enemyHelicopterList.get(i);

			// Current enemy rectangle
			Rectangle enemyRectangle = new Rectangle(eh.xCoord, eh.yCoord,
					EnemyHelicopter.heliBodyImg.getWidth(),
					EnemyHelicopter.heliBodyImg.getHeight());

			// Is current rocket over current enemy?
			if (rocketRectangle.intersects(enemyRectangle)) {
				didItHitEnemy = true;

				// Rocket hit enemy so reduce enemy's health
				eh.health -= Rocket.damagePower;

				// This rocket hit an enemy so skip to the next rocket
				break;
			}
		}

		return didItHitEnemy;
	}

	/**
	 * Update rocket smoke <li>If smoke life time is over, delete it from list
	 * <li>Also, change transparency so smoke slowly disappears
	 * 
	 * @param gameTime
	 */
	private void updateRocketSmoke(long gameTime) {
		for (int i = 0; i < rocketSmokeList.size(); i++) {
			RocketSmoke rs = rocketSmokeList.get(i);

			// Is it time to remove smoke?
			if (rs.didSmokeDisappear(gameTime)) {
				rocketSmokeList.remove(i);
			}

			// Set new transparency of rocket smoke image
			rs.updateTransparency(gameTime);
		}
	}

	/**
	 * Update explosion animation and remove animation when done
	 */
	private void updateExplosions() {
		for (int i = 0; i < explosionList.size(); i++) {
			// If animation is over, remove it
			if (!explosionList.get(i).active) {
				explosionList.remove(i);
			}
		}
	}

	/**
	 * Limits distance of mouse from player
	 * 
	 * @param mousePostion
	 */
	private void limitMousePosition(Point mousePostion) {
		// Max distance from player on y-axis above
		int maxYCoordDistanceFromPlayerTop = 30;
		// Max distance from player on y-axis below
		int maxYCoordDistanceFromPlayerBottom = 120;

		// Mouse cursor will always be same distance from player on x-axis
		int mouseXCoord = player.gunXCoord + 250;

		// Limit distance of mouse on y-axis
		int mouseYCoord = mousePostion.y;
		if (mousePostion.y < player.gunYCoord) { // Above helicopter guns
			if (mousePostion.y < player.gunYCoord
					- maxYCoordDistanceFromPlayerTop) {
				mouseYCoord = player.gunYCoord - maxYCoordDistanceFromPlayerTop;
			}
		} else { // Under helicopter guns
			if (mousePostion.y > player.gunYCoord
					+ maxYCoordDistanceFromPlayerBottom) {
				mouseYCoord = player.gunYCoord
						+ maxYCoordDistanceFromPlayerBottom;
			}
		}

		// Move mouse on y-axis with helicopter... makes shooting easier
		mouseYCoord += player.movingYSpeed;

		// Move mouse
		robot.mouseMove(mouseXCoord, mouseYCoord);
	}
}
