import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class MenuOption extends Sprite{
	
	//Default
	private String defaultSprite;
	//Selected
	private String selectedSprite;
	//Current
	private String currentSprite;
	
	protected JLabel optionLabel;
	protected ImageIcon optionIcon;
	
	public String getDefaultSprite() { return defaultSprite; }

	public void setDefaultSprite(String defaultSprite) { this.defaultSprite = defaultSprite; }

	public String getSelectedSprite() { return selectedSprite; }

	public void setSelectedSprite(String selectedSprite) { this.selectedSprite = selectedSprite; }

	public String getCurrentSprite() { return currentSprite; }

	public void setCurrentSprite(String currentSprite) { this.currentSprite = currentSprite; }

	public MenuOption(String defaultSprite, String selectedSprite, String currentSprite) {
		super(64, 32, currentSprite);
		this.defaultSprite = defaultSprite;
		this.selectedSprite = selectedSprite;
		this.currentSprite = currentSprite;
	}
}
