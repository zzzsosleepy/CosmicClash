//||=================================================================||
//||-----------------------------------------------------------------||
//||---------------------COSMIC CLASH--------------------------------||
//||------------------BY: JEFFREY CHIPMAN----------------------------||
//||-----------------------------------------------------------------||
//||----------------PROG2200 - GAME PROJECT 1------------------------||
//||-----------------------------------------------------------------||
//||=================================================================||


import java.awt.Color;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class GamePrep1 extends JFrame implements KeyListener, Runnable{
	private static final long serialVersionUID = -5095419774985539830L;

	//GUI & System Elements
	private WindowBorderGUI windowGUI;
	private Title gameTitle;
	private BG windowBG;
	private List<MenuOption> menuOptions = new ArrayList<MenuOption>();
	private Menu titleMenu;
	private Clouds clouds;
	private ClearScreen clearScreen;
	private MenuOption playOption;
	private MenuOption exitOption;
	private Thread mainThread;
	private EnemyShotManager enemyShotManager;
	
	//Enemy Settings
	private Boolean movingUp = true;
	
	//Game Objects
	private List<Ship> enemies = new ArrayList<Ship>();
	private List<Block> blockArray = new ArrayList<Block>();
	
	//Player settings
	private Ship playerShip;
	private int playerProjectileSpeed = 25;
	
	//State manager
	private GameStateManager stateManager;
	
	//LayeredPane to allow for sprite depth
	private JLayeredPane layeredPane;
	
	//Labels to show the graphics
	private JLabel PlayerLabel, WindowGUILabel, WindowBGLabel, TitleLabel, CloudLabel, ClearLabel;
	private ImageIcon PlayerImage, WindowGUIImage, WindowBGImage, TitleImage, CloudImage, ClearImage;
	
	//Container to hold graphics
	private Container content;
	
	public GamePrep1() {
		//Set window title
		super("Cosmic Clash");
		
		//Set window size
		setSize(GameProperties.SCREEN_WIDTH, GameProperties.SCREEN_HEIGHT);
		
		//Disable window resizing to ensure the window is always the same size
		setResizable(false);
		
		//State setup
		//Set state is 0 upon creation, which is title screen
		stateManager = new GameStateManager(0);
		
		//Create a layeredpane and set it's bounds to the screen height and width
		layeredPane = new JLayeredPane();
		layeredPane.setBounds(0,0, GameProperties.SCREEN_WIDTH, GameProperties.SCREEN_HEIGHT);
		
		//Setup title screen menu
		TitleMenuSetup();
		
		//Set up all graphical labels
		LabelSetup();
		
		//Create enemies using a 2D array
		InstantiateEnemies(2, 1, "EnemyShip01.png", "EnemyShip01_Hurt.png", playerProjectileSpeed * 3);
		
		enemyShotManager = new EnemyShotManager(enemies, layeredPane, stateManager);
		enemyShotManager.Activate();
		//Create block barrier using 2D array
		InstantiateBlocks();
		
		//Assign the container content to the content pane and set it's background to gray, and layout to null
		content = getContentPane();
		content.setBackground(Color.gray);
		setLayout(null);
		
		//Set storage class vectors
		windowBG.SetVectors(0,0);
		windowGUI.SetVectors(0,0);
		clouds.SetVectors(0,0);
		clearScreen.SetVectors(32, 128);
		gameTitle.SetVectors((GameProperties.SCREEN_WIDTH / 2) - (gameTitle.getWidth() / 2) - 8, 96);
		playerShip.SetVectors(48, 186);
		
		//Add the player and GUI elements to the layeredpane
		LayeredPaneSetup();
		
		//Update the label positions to match the stored values
		PlayerLabel.setLocation(playerShip.getX(), playerShip.getY());
		WindowGUILabel.setLocation(windowGUI.getX(), windowGUI.getY());
		CloudLabel.setLocation(clouds.getX(), clouds.getY());
		ClearLabel.setLocation(clearScreen.getX(), clearScreen.getY());
		TitleLabel.setLocation(gameTitle.getX(), gameTitle.getY());
		WindowBGLabel.setLocation(windowBG.getX(), windowBG.getY());
		for (MenuOption option : menuOptions) {
			option.optionLabel.setLocation(option.getX(), option.getY());
		}
		
		//Add a keylistener to the content container and allow it to be focusable
		content.addKeyListener(this);
		content.setFocusable(true);
		
		//JFrame default close
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	//Create title labels and create the title menu
	private void TitleMenuSetup() {
		//Title setup
		gameTitle = new Title();
		TitleLabel = new JLabel();
		TitleImage = new ImageIcon(getClass().getResource(gameTitle.getFilename()));
		TitleLabel.setIcon(TitleImage);
		TitleLabel.setSize(gameTitle.getWidth(), gameTitle.getHeight());
		
		//Create menu options and assign unselected and selected sprite to each option, then add each to menuOptions list
		playOption = new MenuOption("PlayButton.png", "PlayButton_Selected.png", "PlayButton.png");
		exitOption = new MenuOption("ExitButton.png", "ExitButton_Selected.png", "ExitButton.png");
		menuOptions.add(playOption);
		menuOptions.add(exitOption);
		
		//For each option in the menuOptions list, setup labels, images, and positions
		for (MenuOption option : menuOptions) {
			option.optionLabel = new JLabel();
			option.optionIcon = new ImageIcon(getClass().getResource(option.getCurrentSprite()));
			option.optionLabel.setIcon(option.optionIcon);
			option.optionLabel.setSize(option.getWidth(), option.getHeight());
		}
		//Alternative ArrayList for each method
		//menuOptions.forEach(option -> option.optionLabel = new JLabel());
		
		//Create new menu populated with each option in the menuOptions list, and then update the menu's display
		titleMenu = new Menu(0, menuOptions);
		titleMenu.UpdateOptionLabel();
	}
	
	//Create all labels and label images
	private void LabelSetup() {
		//BG setup
		windowBG = new BG();
		WindowBGLabel = new JLabel();
		WindowBGImage = new ImageIcon(getClass().getResource(windowBG.getFilename()));
		WindowBGLabel.setIcon(WindowBGImage);
		WindowBGLabel.setSize(windowBG.getWidth(), windowBG.getHeight());
		
		//Cloud setup
		clouds = new Clouds();
		CloudLabel = new JLabel();
		CloudImage = new ImageIcon(getClass().getResource(clouds.getFilename()));
		CloudLabel.setIcon(CloudImage);
		CloudLabel.setSize(clouds.getWidth(), clouds.getHeight());
		
		//GUI setup
		windowGUI = new WindowBorderGUI();
		WindowGUILabel = new JLabel();
		WindowGUIImage = new ImageIcon(getClass().getResource(windowGUI.getFilename()));
		WindowGUILabel.setIcon(WindowGUIImage);
		WindowGUILabel.setSize(windowGUI.getWidth(), windowGUI.getHeight());
		
		//Clear screen setup
		clearScreen = new ClearScreen();
		ClearLabel = new JLabel();
		ClearImage = new ImageIcon(getClass().getResource(clearScreen.getFilename()));
		ClearLabel.setIcon(ClearImage);
		ClearLabel.setSize(clearScreen.getWidth(), clearScreen.getHeight());
		ClearLabel.setVisible(false);
		
		
		//Player setup
		PlayerLabel = new JLabel();
		playerShip = new Ship(5,1,"Ship01.png", "Ship01_Hurt.png", playerProjectileSpeed, true, "Projectile01.png", PlayerLabel);
		playerShip.enemyList = enemies;
		PlayerLabel.setVisible(false);
		PlayerImage = new ImageIcon(getClass().getResource(playerShip.getFilename()));
		PlayerLabel.setIcon(PlayerImage);
		PlayerLabel.setSize(playerShip.getWidth(), playerShip.getHeight());
	}
	
	//Enemy creation using 2D array
	private void InstantiateEnemies(int health, int damage, String defaultSprite, String hurtSprite, int projectileSpeed) {
		for(int x = 0; x < 13; x++) {
			for(int y = 0; y < 13; y++) {
				if (x > 8 && y > 3 && x < 12 && y < 9) {
					CreateEnemy(x,y, health, damage, defaultSprite, hurtSprite, projectileSpeed);
				}
			}
		}
	}
	
	//Block creation using 2D array
	private void InstantiateBlocks() {
		for(int x = 0; x < 13; x++) {
			for(int y = 0; y < 13; y++) {
				if (x == 0 || y == 1 || x == 12 || y == 11) {
					CreateBlock(x,y);
				}
			}
		}
	}
	
	//LayeredPane is used to implement z-ordering for labels (depth must be an integer, changing dynamically must be an int)
	private void AddToLayeredPane(JLabel label, int zDepth) {
		layeredPane.add(label, Integer.valueOf(zDepth));
	}
	
	//Add the layeredpane to the JFrame, position menu options, and add elements to layeredpane
	private void LayeredPaneSetup() {
		//Add the layeredpane to the JFrame
		add(layeredPane);
		
		//Position the play and exit options on the title screen and add each to the layered pane
		playOption = menuOptions.get(0);
		exitOption = menuOptions.get(1);
		playOption.setX((GameProperties.SCREEN_WIDTH / 2) - (gameTitle.getWidth() / 2) + 8);	
		playOption.setY((GameProperties.SCREEN_HEIGHT / 2) + 32);
		layeredPane.add(playOption.optionLabel, Integer.valueOf(99));
		exitOption.setX((GameProperties.SCREEN_WIDTH / 2) + (exitOption.getWidth() / 2) + 8);
		exitOption.setY((GameProperties.SCREEN_HEIGHT / 2) + 32);
		layeredPane.add(exitOption.optionLabel, Integer.valueOf(99));
		
		//Add GUI elements to the layeredpane with a specified z-depth
		AddToLayeredPane(WindowGUILabel, 99);
		AddToLayeredPane(WindowBGLabel, -1);
		AddToLayeredPane(TitleLabel, 99);
		AddToLayeredPane(CloudLabel, 98);
		AddToLayeredPane(ClearLabel, 99);
		
		//Add the player label to the layered pane
		AddToLayeredPane(PlayerLabel, 5);
	}
	
	//Creates a block at specific x, y coordinates and adds them to the block array
	public void CreateBlock(int x, int y) {
		Block myBlock;
		JLabel BlockLabel;
		ImageIcon BlockImage;
		//Instantiate new block
		myBlock = new Block();
		//Create a new label for the block
		BlockLabel = new JLabel();
		//Attach an image to the block's label
		BlockImage = new ImageIcon(getClass().getResource(myBlock.getFilename()));
		BlockLabel.setIcon(BlockImage);
		//Set the size of the block
		BlockLabel.setSize(myBlock.getWidth(), myBlock.getHeight());
		//Set the vectors of the block
		myBlock.SetVectors(x * 32, y * 32);
		//Add the block's label to the layered pane for z-sorting
		layeredPane.add(BlockLabel, Integer.valueOf(98));
		//Add the created block to the array of blocks
		blockArray.add(myBlock);
		//Update the label's location
		BlockLabel.setLocation(myBlock.getX(), myBlock.getY());
	}
	
	//Creates an enemy ship at specific x, y coordinates and adds them to the enemies array
	public void CreateEnemy(int x, int y, int health, int damage, String defaultSprite, String hurtSprite, int projectileSpeed) {
		Ship enemy;
		JLabel EnemyLabel;
		ImageIcon EnemyImage;
		//Instantiate new enemy
		//Create a new label for the enemy
		EnemyLabel = new JLabel();
		enemy = new Ship(health, damage, defaultSprite, hurtSprite, projectileSpeed, false, "Projectile02.png", EnemyLabel);
		enemy.playerShip = playerShip;
		//Hide the enemy by default
		EnemyLabel.setVisible(false);
		//Attach an image to the enemy's label
		EnemyImage = new ImageIcon(getClass().getResource(enemy.getFilename()));
		EnemyLabel.setIcon(EnemyImage);
		//Set the size of the enemy
		EnemyLabel.setSize(enemy.getWidth(), enemy.getHeight());
		//Set the vectors of the enemy
		enemy.SetVectors(x * 32, y * 32);
		//Add the enemy's label to the layered pane for z-sorting
		layeredPane.add(EnemyLabel, Integer.valueOf(98));
		//Add the created enemy to the array of enemies
		enemies.add(enemy);
		//Update the label's location
		EnemyLabel.setLocation(enemy.getX(), enemy.getY());
	}
	
	//Main method
	public static void main(String[] args) {
		GamePrep1 myGame = new GamePrep1();
		myGame.setVisible(true);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	//Control player movement based on key input and update the ship's location
	public void PlayerMovement(KeyEvent e) {
		//If state is game play
		if (stateManager.getGameState() == 1) {
			int dy = playerShip.getY();
			
			//Up down movement
			//UP MOVEMENT
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				//If the player is below 74, allow movement up
				//This is to prevent the player from moving offscreen
				if (dy >= 74) {					
					dy -= GameProperties.CHARACTER_STEP;
					if (dy + playerShip.getHeight() < 0) {
						dy = GameProperties.SCREEN_HEIGHT;
					}
				}
			//DOWN MOVEMENT
			} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				//If the player is above 314, allow movement down
				//This is to prevent the player from moving offscreen
				if (dy <= 314) {
					dy += GameProperties.CHARACTER_STEP;
					if (dy > GameProperties.SCREEN_HEIGHT) {
						dy = -1 * playerShip.getHeight();
					}					
				}
			}
			
			//Update player label location
			playerShip.Move(dy);
		}
		//FIRE KEY
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if(playerShip.canShoot) {
				Projectile createdBullet = playerShip.Fire();
				//Increment the shot count everytime the player shoots
				GameProperties.SHOT_COUNT++;
				//Add the projectile to the layered pane
				layeredPane.add(createdBullet.ProjectileLabel, Integer.valueOf(3));
			}
		}
	}
	
	//Control title movement based on key input
	public void TitleMovement(KeyEvent e) {
		//If state is title
			if (stateManager.getGameState() == 0) {
				//RIGHT MENU MOVEMENT
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					titleMenu.SelectNext();
				//LEFT MENU MOVEMENT
				} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					titleMenu.SelectPrevious();
				}
				
				//MENU SELECT
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					switch (titleMenu.selectedIndex) {
					//PLAY
					case 0:
						//Toggle the title off
						ToggleTitle();
						//Force an initial cooldown on the player's fire key
						playerShip.ProjectileCooldown();
						//Set the game state to gameplay
						stateManager.setGameState(1);
						//Activate the enemy shooting "AI"
						enemyShotManager.Activate();
						//Start the main thread
						mainThread = new Thread(this, "Main Game Thread");
						mainThread.start();
						break;
					//EXIT
					case 1:
						System.exit(0);
						break;
					}
				}
			}
			//If the escape key is pressed during game-play, return to title screen
			if (stateManager.getGameState() == 1) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					ToggleTitle();
					stateManager.setGameState(0);
				}				
			}
	}
	
	//Handle input on the Clear Screen
	public void ClearScreenMovement(KeyEvent e) {
		//If state is level-end
		if (stateManager.getGameState() == 2) {
			//Continue
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				HideClearScreen();
				stateManager.setGameState(1);
				switch(GameProperties.CURRENT_LEVEL) {
					case 1:
						GameProperties.CURRENT_LEVEL = 2;
						break;
					case 2:
						GameProperties.CURRENT_LEVEL = 3;
						break;
					case 3:
						GameProperties.CURRENT_LEVEL = 1;
						break;
					default:
						GameProperties.CURRENT_LEVEL = 1;
						break;
				}
				//Change the level to the new current level
				ChangeLevel(GameProperties.CURRENT_LEVEL);
			}
		}
	}
	
	//Toggles the display of the title labels and player label depending on the current game state
	public void ToggleTitle() {
		//If the game state is 0, hide the title and show the player + enemies
		//If the game state is 1, hide the player, enemies, and show the title screen
		if (stateManager.getGameState() == 0) {
			TitleLabel.setVisible(false);
			for (MenuOption option : menuOptions) {
				option.optionLabel.setVisible(false);
			}
			PlayerLabel.setVisible(true);
			for(Ship enemy : enemies) {
				enemy.ShipLabel.setVisible(true);
			}
		} else if (stateManager.getGameState() == 1){
			TitleLabel.setVisible(true);
			for (MenuOption option : menuOptions) {
				option.optionLabel.setVisible(true);
			}
			PlayerLabel.setVisible(false);
			for(Ship enemy : enemies) {
				enemy.ShipLabel.setVisible(false);
			}
		}
	}
	
	//Move the enemies up or down individually
	public void MoveEnemies() {
		//If the first enemy in the array can't move up anymore, begin moving down
		if ((enemies.get(0).getY() - GameProperties.CHARACTER_STEP) <= 48) {
			movingUp = false;
		}
		//If the last enemy in the array can't move down anymore, begin moving up
		else if ((enemies.get(enemies.size() - 1).getY() + GameProperties.CHARACTER_STEP) >= 330){
			movingUp = true;
		}
	    Iterator<Ship> itr = enemies.iterator();
	    while (itr.hasNext()) {
	      Ship ship = itr.next();
	      if (!ship.ShipLabel.isVisible()) {
	        itr.remove();
	      }
	    }
		//For each enemy in the enemies array...
		for(Ship enemy : enemies) {
			if (enemy != null) {
				//Store their Y position
				int moveY = enemy.getY();
				if (movingUp) {
					//Prepare to move up one step
					moveY -= GameProperties.CHARACTER_STEP;
				} else {
					//Prepare to move down one step
					moveY += GameProperties.CHARACTER_STEP;
				}
				//Initiate movement
				enemy.Move(moveY);				
			}
		}
	}
	
	//Changes the current level, BG image
	public void ChangeLevel(int levelNumber) {
		//Update the background image, instantiate enemies, and make them visible
		switch(levelNumber) {
		case 1:
			windowBG.setFilename("BG01.png");
			InstantiateEnemies(2, 1, "EnemyShip01.png", "EnemyShip01_Hurt.png", playerProjectileSpeed * 3);
			for(Ship enemy : enemies) {
				enemy.ShipLabel.setVisible(true);
			}
			break;
		case 2:
			windowBG.setFilename("BG02.png");
			InstantiateEnemies(3, 1, "EnemyShip02.png", "EnemyShip01_Hurt.png", playerProjectileSpeed * 2);
			for(Ship enemy : enemies) {
				enemy.ShipLabel.setVisible(true);
			}
			break;
		case 3:
			windowBG.setFilename("BG03.png");
			InstantiateEnemies(4, 1, "EnemyShip03.png", "EnemyShip01_Hurt.png", playerProjectileSpeed);
			for(Ship enemy : enemies) {
				enemy.ShipLabel.setVisible(true);
			}
			break;
		default:
			windowBG.setFilename("BG01.png");
			InstantiateEnemies(2, 1, "EnemyShip01.png", "EnemyShip01_Hurt.png", playerProjectileSpeed * 3);
			for(Ship enemy : enemies) {
				enemy.ShipLabel.setVisible(true);
			}
			break;
		}
		//Start the main thread
		mainThread = new Thread(this, "Main Game Thread");
		mainThread.start();
		//Activate the enemy shooting "AI"
		enemyShotManager.Activate();
		//Update the BG image
		WindowBGImage = new ImageIcon(getClass().getResource(windowBG.getFilename()));
		WindowBGLabel.setIcon(WindowBGImage);
	}
	
	//Hides the Clear Screen GUI
	public void HideClearScreen() {
		ClearLabel.setVisible(false);
		ChangeClearScreenSprite("ClearScreen.png"); 
	}
	
	//Modifies the Clear Screen graphic
	public void ChangeClearScreenSprite(String sprite) {
		clearScreen.setFilename(sprite);
		ClearImage = new ImageIcon(getClass().getResource(clearScreen.getFilename()));
		ClearLabel.setIcon(ClearImage);
	}

	//Input handling
	@Override
	public void keyPressed(KeyEvent e) {
			//Handle all player movement based on input
			PlayerMovement(e);	
			
			//Handle title movement based on input
			TitleMovement(e);
			
			//Handle clear screen movement
			ClearScreenMovement(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (stateManager.getGameState() == 1) {
			//Move the enemies while the game is in game-play mode
			if (enemies.size() > 0) {
				MoveEnemies();				
			} else {
				//If there are no more enemies, set the game state to level end
				stateManager.setGameState(2);
			}
			try {
				Thread.sleep(150);
				} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
			}
		while (stateManager.getGameState() == 2) {
			//Show the clear screen
			ClearLabel.setVisible(true);
			try {
				//Wait before displaying the continue message
				Thread.sleep(1500);
				//Update the clear screen image and display it
				ChangeClearScreenSprite("ClearScreen_Continue.png");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}