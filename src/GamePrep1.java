import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class GamePrep1 extends JFrame implements ActionListener, KeyListener{
	private static final long serialVersionUID = -5095419774985539830L;

	//Storage classes
	private Ship playerShip;
	private WindowBorderGUI windowGUI;
	private Title gameTitle;
	private BG windowBG;
	private int playerProjectileSpeed = 25;
	private List<Ship> enemies = new ArrayList<Ship>();
	private List<Block> blockArray = new ArrayList<Block>();
	private List<MenuOption> menuOptions = new ArrayList<MenuOption>();
	private Menu titleMenu;
	private Clouds clouds;
	
	//State manager
	private GameStateManager stateManager;
	
	//LayeredPane to allow for sprite depth
	private JLayeredPane layeredPane;
	
	//Labels to show the graphics
	private JLabel PlayerLabel, WindowGUILabel, WindowBGLabel, TitleLabel, CloudLabel;
	private ImageIcon PlayerImage, WindowGUIImage, WindowBGImage, TitleImage, CloudImage;
	
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
		
		//LayeredPane setup
		layeredPane = new JLayeredPane();
		layeredPane.setBounds(0,0, GameProperties.SCREEN_WIDTH, GameProperties.SCREEN_HEIGHT);
		
		//Title setup
		gameTitle = new Title();
		TitleLabel = new JLabel();
		TitleImage = new ImageIcon(getClass().getResource(gameTitle.getFilename()));
		TitleLabel.setIcon(TitleImage);
		TitleLabel.setSize(gameTitle.getWidth(), gameTitle.getHeight());
		
		//Create menu options and assign unselected and selected sprite to each option, then add each to menuOptions list
		MenuOption playOption = new MenuOption("PlayButton.png", "PlayButton_Selected.png", "PlayButton.png");
		MenuOption exitOption = new MenuOption("ExitButton.png", "ExitButton_Selected.png", "ExitButton.png");
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
		
		//Player setup
		playerShip = new Ship(5,1,"Ship01.png", "Ship01_Hurt.png", playerProjectileSpeed, true);
		PlayerLabel = new JLabel();
		playerShip.setShipLabel(PlayerLabel);
		PlayerLabel.setVisible(false);
		PlayerImage = new ImageIcon(getClass().getResource(playerShip.getFilename()));
		PlayerLabel.setIcon(PlayerImage);
		PlayerLabel.setSize(playerShip.getWidth(), playerShip.getHeight());
		
		//Enemy creation using 2D array
		for(int x = 0; x < 13; x++) {
			for(int y = 0; y < 13; y++) {
				if (x > 7 && y > 3 && x < 11 && y < 9) {
					CreateEnemy(x,y);
				}
			}
		}
		
		//Block creation using 2D array
		for(int x = 0; x < 13; x++) {
			for(int y = 0; y < 13; y++) {
				if (x == 0 || y == 1 || x == 12 || y == 11) {
					CreateBlock(x,y);
				}
			}
		}
		
		content = getContentPane();
		content.setBackground(Color.gray);
		setLayout(null);
		
		
		//Set storage class vectors
		windowBG.SetVectors(0,0);
		windowGUI.SetVectors(0,0);
		clouds.SetVectors(0,0);
		gameTitle.SetVectors((GameProperties.SCREEN_WIDTH / 2) - (gameTitle.getWidth() / 2) - 8, 96);
		playerShip.SetVectors(48, 186);
		
		//Add the layered pane to the JFrame
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
		
		//LayeredPane is used to implement z-ordering for labels
		//LayeredPane depth must be an Integer, changing LayeredPane dynamically must be an int!!
		//Add LayerPane to the JFrame and add labels to the LayeredPane
		layeredPane.add(WindowGUILabel, Integer.valueOf(99));
		layeredPane.add(PlayerLabel, Integer.valueOf(5));
		layeredPane.add(WindowBGLabel, Integer.valueOf(-1));
		layeredPane.add(TitleLabel, Integer.valueOf(99));
		layeredPane.add(CloudLabel, Integer.valueOf(98));
		
		//Update the label positions to match the stored values
		PlayerLabel.setLocation(playerShip.getX(), playerShip.getY());
		WindowGUILabel.setLocation(windowGUI.getX(), windowGUI.getY());
		CloudLabel.setLocation(clouds.getX(), clouds.getY());
		TitleLabel.setLocation(gameTitle.getX(), gameTitle.getY());
		WindowBGLabel.setLocation(windowBG.getX(), windowBG.getY());
		for (MenuOption option : menuOptions) {
			option.optionLabel.setLocation(option.getX(), option.getY());
		}
		
		content.addKeyListener(this);
		content.setFocusable(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
	public void CreateEnemy(int x, int y) {
		Ship enemy;
		JLabel EnemyLabel;
		ImageIcon EnemyImage;
		//Instantiate new enemy
		enemy = new Ship(1, 1, "EnemyShip01.png", "EnemyShip01_Hurt.png", 1, false);
		//Create a new label for the enemy
		EnemyLabel = new JLabel();
		enemy.setShipLabel(EnemyLabel);
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

	//Player Controller
	@Override
	public void keyPressed(KeyEvent e) {
		//If state is game play
		if (stateManager.getGameState() == 1) {
			int dx = playerShip.getX();
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
			//If the escape key is pressed during game-play, return to title screen
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				ToggleTitle();
				stateManager.setGameState(0);
			}
			
			//Update player label location
			playerShip.setX(dx);
			playerShip.setY(dy);		
			PlayerLabel.setLocation(playerShip.getX(), playerShip.getY());
			
			//FIRE KEY
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				Projectile createdBullet = playerShip.Fire();
				layeredPane.add(createdBullet.ProjectileLabel, Integer.valueOf(3));
			}
			
		//If state is title
		} else if (stateManager.getGameState() == 0) {
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
					ToggleTitle();
					stateManager.setGameState(1);
					break;
				//EXIT
				case 1:
					System.exit(0);
					break;
				}
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

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}
}