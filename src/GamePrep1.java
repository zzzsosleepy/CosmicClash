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
	private int playerProjectileSpeed = 15;
	private List<Block> blockArray = new ArrayList<Block>();
	private Menu titleMenu;
	private List<MenuOption> menuOptions = new ArrayList<MenuOption>();
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
		super("Cosmic Clash");
		setSize(GameProperties.SCREEN_WIDTH, GameProperties.SCREEN_HEIGHT);
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
		
		//Create menu options and assign unselected and selected sprites to each option, then add each to menuOptions list
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

		//Alternative arraylist foreach method
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
		playerShip = new Ship(5,1,"Ship01.png", playerProjectileSpeed);
		PlayerLabel = new JLabel();
		PlayerLabel.setVisible(false);
		PlayerImage = new ImageIcon(getClass().getResource(playerShip.getFilename()));
		PlayerLabel.setIcon(PlayerImage);
		PlayerLabel.setSize(playerShip.getWidth(), playerShip.getHeight());
		
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
		windowBG.setX(0);
		windowBG.setY(0);
		windowGUI.setX(0);
		windowGUI.setY(0);
		clouds.setX(0);
		clouds.setY(0);
		gameTitle.setX((GameProperties.SCREEN_WIDTH / 2) - (gameTitle.getWidth() / 2) - 8);
		gameTitle.setY(96);
		playerShip.setX(48);
		playerShip.setY(186);
		
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
		myBlock = new Block();
		BlockLabel = new JLabel();
		BlockImage = new ImageIcon(getClass().getResource(myBlock.getFilename()));
		BlockLabel.setIcon(BlockImage);
		BlockLabel.setSize(myBlock.getWidth(), myBlock.getHeight());
		myBlock.setX(x * 32);
		myBlock.setY(y * 32);
		layeredPane.add(BlockLabel, Integer.valueOf(98));
		blockArray.add(myBlock);
		BlockLabel.setLocation(myBlock.getX(), myBlock.getY());
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
				dy -= GameProperties.CHARACTER_STEP;
				if (dy + playerShip.getHeight() < 0) {
					dy = GameProperties.SCREEN_HEIGHT;
				}
			//DOWN MOVEMENT
			} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				dy += GameProperties.CHARACTER_STEP;
				if (dy > GameProperties.SCREEN_HEIGHT) {
					dy = -1 * playerShip.getHeight();
				}
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
		if (stateManager.getGameState() == 0) {
			TitleLabel.setVisible(false);
			for (MenuOption option : menuOptions) {
				option.optionLabel.setVisible(false);
			}
			PlayerLabel.setVisible(true);
		} else {
			TitleLabel.setVisible(true);
			for (MenuOption option : menuOptions) {
				option.optionLabel.setVisible(true);
			}
			PlayerLabel.setVisible(false);
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