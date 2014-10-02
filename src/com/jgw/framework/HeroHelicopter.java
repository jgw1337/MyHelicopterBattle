package com.jgw.framework;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class HeroHelicopter {
	// Health
	private final int healthInit = 100;
	public int health;

	// Position
	public int xCoord, yCoord;

	// Speed and direction
	private double movingXSpeed;
	public double movingYSpeed;
	private double acceleratingXSpeed, acceleratingYSpeed;
	private double stoppingXSpeed, stoppingYSpeed;

	// Rockets
	private final int numberOfRocketsInit = 80;
	public int numberOfRockets;

	// Homing Rockets
	private final int numberOfHomingRocketsInit = 80;
	public int numberOfHomingRockets;

	// Bullets
	private final int numberOfBulletsInit = 1400;
	public int numberOfBullets;

	// Images
	public BufferedImage heliBodyImg;
	private BufferedImage heliFrontPropellerAnimImg, heliRearPropellerAnimImg;

	// Propeller animation
	private Animation heliFrontPropellerAnim, heliRearPropellerAnim;

	// Propeller offsets (relative to the helicopter)
	private int offsetXFrontPropeller, offsetYFrontPropeller;
	private int offsetXRearPropeller, offsetYRearPropeller;

	// Rocket launcher offsets (relative to helicopter)
	private int offsetXRocketLauncher, offsetYRocketLauncher;

	// Position of rocket launcher
	public int rocketLauncherXCoord, rocketLauncherYCoord;

	// Gun offsets (relative to helicopter)
	private int offsetXGun, offsetYGun;

	// Position of gun
	public int gunXCoord, gunYCoord;

	// Helicopter style
	private String heliStyleStr;
	public String style;

	/**
	 * Creates player
	 * 
	 * @param xCoord
	 *            Starting x-coord
	 * @param yCoord
	 *            Starting x-coord
	 */
	public HeroHelicopter(int xCoord, int yCoord, KeyEvent heliStyle) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;

		LoadContent(heliStyle);
		Initialize();
	}

	/**
	 * Set vars and objs
	 */
	private void Initialize() {
/*
		this.health = healthInit;
*/
		
/*
		this.numberOfRockets = numberOfRocketsInit;
		this.numberOfHomingRockets = numberOfHomingRocketsInit;
		this.numberOfBullets = numberOfBulletsInit;
*/

		this.movingXSpeed = 0;
		this.movingYSpeed = 0;
/*
		this.acceleratingXSpeed = 0.2;
		this.acceleratingYSpeed = 0.2;
		this.stoppingXSpeed = 0.1;
		this.stoppingYSpeed = 0.1;
*/

/*
		this.offsetXFrontPropeller = 70;
		this.offsetYFrontPropeller = -23;
		this.offsetXRearPropeller = -6;
		this.offsetYRearPropeller = -21;
*/
		
		this.offsetXRocketLauncher = 138;
		this.offsetYRocketLauncher = 40;
		this.rocketLauncherXCoord = this.xCoord + this.offsetXRocketLauncher;
		this.rocketLauncherYCoord = this.yCoord + this.offsetYRocketLauncher;

		this.offsetXGun = heliBodyImg.getWidth() - 40;
		this.offsetYGun = heliBodyImg.getHeight();
		this.gunXCoord = this.xCoord + this.offsetXGun;
		this.gunYCoord = this.yCoord + this.offsetYGun;
	}

	/**
	 * Load files
	 */
	private void LoadContent(KeyEvent heliStyle) {
		try {
			if (heliStyle.getKeyCode() == KeyEvent.VK_E) {
				style = "esther";
				heliStyleStr = "data/1_helicopter_body_esther.png";
				this.health = healthInit;
				this.numberOfRockets = 0;
				this.numberOfHomingRockets = 100;
				this.numberOfBullets = 1000;
				this.offsetXFrontPropeller = 38;
				this.offsetYFrontPropeller = -17;
				this.offsetXRearPropeller = 3;
				this.offsetYRearPropeller = -21;
				this.acceleratingXSpeed = 0.4;
				this.acceleratingYSpeed = 0.4;
				this.stoppingXSpeed = 0.4;
				this.stoppingYSpeed = 0.4;
			} else if (heliStyle.getKeyCode() == KeyEvent.VK_F) {
				style = "frank";
				heliStyleStr = "data/1_helicopter_body_frank.png";
				this.health = 50;
				this.numberOfRockets = 5;
				this.numberOfHomingRockets = 5;
				this.numberOfBullets = 2000;
				this.offsetXFrontPropeller = 70;
				this.offsetYFrontPropeller = -23;
				this.offsetXRearPropeller = -10;
				this.offsetYRearPropeller = -21;
				this.acceleratingXSpeed = 0.2;
				this.acceleratingYSpeed = 0.2;
				this.stoppingXSpeed = 0.1;
				this.stoppingYSpeed = 0.1;
			} else if (heliStyle.getKeyCode() == KeyEvent.VK_G) {
				style = "glenn";
				heliStyleStr = "data/1_helicopter_body_glenn.png";
				this.health = 50;
				this.numberOfRockets = 10;
				this.numberOfHomingRockets = 25;
				this.numberOfBullets = 100;
				this.offsetXFrontPropeller = 70;
				this.offsetYFrontPropeller = -23;
				this.offsetXRearPropeller = -6;
				this.offsetYRearPropeller = -21;
				this.acceleratingXSpeed = 0.2;
				this.acceleratingYSpeed = 0.2;
				this.stoppingXSpeed = 0.1;
				this.stoppingYSpeed = 0.1;
			} else if (heliStyle.getKeyCode() == KeyEvent.VK_J) {
				style = "john";
				heliStyleStr = "data/1_helicopter_body_john.png";
				this.health = healthInit;
				this.numberOfRockets = 15;
				this.numberOfHomingRockets = 15;
				this.numberOfBullets = 500;
				this.offsetXFrontPropeller = 65;
				this.offsetYFrontPropeller = -23;
				this.offsetXRearPropeller = -6;
				this.offsetYRearPropeller = -11;
				this.acceleratingXSpeed = 0.2;
				this.acceleratingYSpeed = 0.2;
				this.stoppingXSpeed = 0.1;
				this.stoppingYSpeed = 0.1;
			} else if (heliStyle.getKeyCode() == KeyEvent.VK_M) {
				style = "marcus";
				heliStyleStr = "data/1_helicopter_body_marcus.png";
				this.health = 150;
				this.numberOfRockets = 10;
				this.numberOfHomingRockets = 5;
				this.numberOfBullets = 1000;
				this.offsetXFrontPropeller = 70;
				this.offsetYFrontPropeller = -23;
				this.offsetXRearPropeller = -6;
				this.offsetYRearPropeller = -21;
				this.acceleratingXSpeed = 0.2;
				this.acceleratingYSpeed = 0.2;
				this.stoppingXSpeed = 0.1;
				this.stoppingYSpeed = 0.1;
			} else if (heliStyle.getKeyCode() == KeyEvent.VK_W) {
				style = "wade";
				heliStyleStr = "data/1_helicopter_body_wade.png";
				this.health = 250;
				this.numberOfRockets = 25;
				this.numberOfHomingRockets = 10;
				this.numberOfBullets = 100;
				this.offsetXFrontPropeller = 70;
				this.offsetYFrontPropeller = -23;
				this.offsetXRearPropeller = -4;
				this.offsetYRearPropeller = 34;
				this.acceleratingXSpeed = 0.2;
				this.acceleratingYSpeed = 0.2;
				this.stoppingXSpeed = 0.1;
				this.stoppingYSpeed = 0.1;
			} else {
				style = "default";
				heliStyleStr = "data/1_helicopter_body.png";
				this.health = healthInit;
				this.numberOfRockets = numberOfRocketsInit;
				this.numberOfHomingRockets = numberOfHomingRocketsInit;
				this.numberOfBullets = numberOfBulletsInit;
				this.offsetXFrontPropeller = 70;
				this.offsetYFrontPropeller = -23;
				this.offsetXRearPropeller = -6;
				this.offsetYRearPropeller = -21;
				this.acceleratingXSpeed = 0.2;
				this.acceleratingYSpeed = 0.2;
				this.stoppingXSpeed = 0.1;
				this.stoppingYSpeed = 0.1;
			}
			URL heliBodyImgUrl = this.getClass().getResource(heliStyleStr);
			heliBodyImg = ImageIO.read(heliBodyImgUrl);

			URL heliFrontPropellerAnimImgUrl = this.getClass().getResource(
					"data/1_front_propeller_anim.png");
			heliFrontPropellerAnimImg = ImageIO
					.read(heliFrontPropellerAnimImgUrl);

			URL heliRearPropellerAnimImgUrl = this.getClass().getResource(
					"data/1_rear_propeller_anim_blur.png");
			heliRearPropellerAnimImg = ImageIO
					.read(heliRearPropellerAnimImgUrl);
		} catch (IOException ex) {
			Logger.getLogger(HeroHelicopter.class.getName()).log(Level.SEVERE,
					null, ex);
		}

		// Now that propeller animation images are loaded, initialize the
		// animation object
		heliFrontPropellerAnim = new Animation(heliFrontPropellerAnimImg, 204,
				34, 3, 20, true, xCoord + offsetXFrontPropeller, yCoord
						+ offsetYFrontPropeller, 0);
		heliRearPropellerAnim = new Animation(heliRearPropellerAnimImg, 54, 54,
				4, 20, true, xCoord + offsetXRearPropeller, yCoord
						+ offsetYRearPropeller, 0);
	}

	/**
	 * Resets player
	 * 
	 * @param xCoord
	 *            Starting x-coord
	 * @param yCoord
	 *            Starting y-coord
	 */
	public void Reset(int xCoord, int yCoord) {
		this.health = healthInit;

		this.numberOfRockets = numberOfRocketsInit;
		this.numberOfHomingRockets = numberOfHomingRocketsInit;
		this.numberOfBullets = numberOfBulletsInit;

		this.xCoord = xCoord;
		this.yCoord = yCoord;

		this.movingXSpeed = 0;
		this.movingYSpeed = 0;

		this.rocketLauncherXCoord = this.xCoord + this.offsetXRocketLauncher;
		this.rocketLauncherYCoord = this.yCoord + this.offsetYRocketLauncher;

		this.gunXCoord = this.xCoord + this.offsetXGun;
		this.gunYCoord = this.yCoord + this.offsetYGun;
	}

	/**
	 * Checks if player is shooting; also checks if player can shoot (reload aka
	 * time between bullets and remaining ammo)
	 * 
	 * @param gameTime
	 *            Current elapsed time (in nanosecs)
	 * @return true if shooting, false otherwise
	 */
	public boolean isShooting(long gameTime) {
		// Checks is left mouse is down and if it is time for a new bullet
		if (Canvas.mouseButtonState(MouseEvent.BUTTON1)
				&& ((gameTime - Bullet.timeOfLastCreatedBullet) >= Bullet.timeBetweenNewBullets && this.numberOfBullets > 0)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if player is launching a rocket; also checks if player can fire
	 * rocket (reload aka time between rockets and remaining rockets)
	 * 
	 * @param gameTime
	 *            Current elapsed time (in nanosecs)
	 * @return true if shooting, false otherwise
	 */
	public boolean isFiringRocket(long gameTime) {
		if (Canvas.mouseButtonState(MouseEvent.BUTTON3)
				&& ((gameTime - Rocket.timeOfLastCreatedRocket) >= Rocket.timeBetweenNewRockets && this.numberOfRockets > 0)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if player is launching a rocket; also checks if player can fire
	 * rocket (reload aka time between rockets and remaining rockets)
	 * 
	 * @param gameTime
	 *            Current elapsed time (in nanosecs)
	 * @return true if shooting, false otherwise
	 */
	public boolean isFiringHomingRocket(long gameTime) {
		if (Canvas.mouseButtonState(MouseEvent.BUTTON2)
				&& ((gameTime - HomingRocket.timeOfLastCreatedRocket) >= HomingRocket.timeBetweenNewRockets && this.numberOfHomingRockets > 0)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sets its speed and direction
	 */
	public void setVelocity() {
		// Moving on x-axis
		if (Canvas.keyboardKeyState(KeyEvent.VK_D)
				|| Canvas.keyboardKeyState(KeyEvent.VK_RIGHT)) {
			movingXSpeed += acceleratingXSpeed;
		} else if (Canvas.keyboardKeyState(KeyEvent.VK_A)
				|| Canvas.keyboardKeyState(KeyEvent.VK_LEFT)) {
			movingXSpeed -= acceleratingXSpeed;
		} else { // Stopping
			if (movingXSpeed < 0) {
				movingXSpeed += stoppingXSpeed;
			} else if (movingXSpeed > 0) {
				movingXSpeed -= stoppingXSpeed;
			}
		}

		// Kludge to prevent helicopter from moving offscreen along x-axis
		if (this.xCoord <= 0) {
			movingXSpeed = 0;
			this.xCoord = 1;
		} else if (this.xCoord + this.heliBodyImg.getWidth() >= Framework.frameWidth) {
			movingXSpeed = 0;
			this.xCoord = Framework.frameWidth - this.heliBodyImg.getWidth()
					- 1;
		}

		// Moving on y-axis
		if (Canvas.keyboardKeyState(KeyEvent.VK_W)
				|| Canvas.keyboardKeyState(KeyEvent.VK_UP)) {
			movingYSpeed -= acceleratingYSpeed;
		} else if (Canvas.keyboardKeyState(KeyEvent.VK_S)
				|| Canvas.keyboardKeyState(KeyEvent.VK_DOWN)) {
			movingYSpeed += acceleratingYSpeed;
		} else { // Stopping
			if (movingYSpeed < 0) {
				movingYSpeed += stoppingYSpeed;
			} else if (movingYSpeed > 0) {
				movingYSpeed -= stoppingYSpeed;
			}
		}

		// Kludge to prevent helicopter from moving offscreen along y-axis
		if (this.yCoord <= 0) {
			movingYSpeed = 0;
			this.yCoord = 1;
		} else if (this.yCoord + this.heliBodyImg.getHeight() >= Framework.frameHeight) {
			movingYSpeed = 0;
			this.yCoord = Framework.frameHeight - this.heliBodyImg.getHeight()
					- 1;
		}

	}

	/**
	 * Updates position
	 */
	public void Update() {
		// Move
		xCoord += movingXSpeed;
		yCoord += movingYSpeed;
		heliFrontPropellerAnim.changeCoordinates(
				xCoord + offsetXFrontPropeller, yCoord + offsetYFrontPropeller);
		heliRearPropellerAnim.changeCoordinates(xCoord + offsetXRearPropeller,
				yCoord + offsetYRearPropeller);

		// Change Rocket position accordingly
		this.rocketLauncherXCoord = this.xCoord + this.offsetXRocketLauncher;
		this.rocketLauncherYCoord = this.yCoord + this.offsetYRocketLauncher;

		// Change Gun position accordingly
		this.gunXCoord = this.xCoord + this.offsetXGun;
		this.gunYCoord = this.yCoord + this.offsetYGun;
	}

	/**
	 * Draws image
	 * 
	 * @param g2d
	 *            Graphics2D
	 */
	public void Draw(Graphics2D g2d) {
		heliFrontPropellerAnim.Draw(g2d);
		heliRearPropellerAnim.Draw(g2d);
		g2d.drawImage(heliBodyImg, xCoord, yCoord, null);
	}
	
	
}
