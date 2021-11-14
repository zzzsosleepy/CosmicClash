import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class Menu {
	//Active menu option index
	protected int selectedIndex;
	//Amount of options in menu
	protected int optionCount;
	//Menu options stored in an array list
	protected List<MenuOption> options = new ArrayList<MenuOption>();
	
	public Menu(int selectedIndex, List<MenuOption> options) {
		this.selectedIndex = selectedIndex;
		this.optionCount = options.size();
		this.options = options;
	}
	
	public Menu(int selectedIndex, int optionCount, List<MenuOption> options) {
		this.selectedIndex = selectedIndex;
		this.optionCount = optionCount;
		this.options = options;
	}
	
	//Update the menu option's current sprite if it is selected, otherwise set the sprite to the default sprite
	public void UpdateOptionLabel() {
		for(int i = 0; i <= optionCount - 1; i++) {
			MenuOption option = options.get(i);
			//If this is the element located at the selected index, set the current sprite to be the option's selected sprite
			if (i == selectedIndex) {
				option = options.get(i);
				option.setCurrentSprite(option.getSelectedSprite());
				option.optionLabel.setIcon(new ImageIcon(getClass().getResource(option.getCurrentSprite())));
			//If this element is not location at the selected index, set the current sprite to be the option's default sprite
			} else{
				option = options.get(i);
				option.setCurrentSprite(option.getDefaultSprite());
				option.optionLabel.setIcon(new ImageIcon(getClass().getResource(option.getCurrentSprite())));
			}
		}
	}
	
	//Decrement index by 1. If index is 0, wrap to end of option list
	public int SelectPrevious() {
		if (selectedIndex == 0) {
			selectedIndex = optionCount - 1;
		} else {
			selectedIndex -= 1;
		}
		UpdateOptionLabel();
		return selectedIndex;
	}
	
	//Increment index by 1. If index is the final option, wrap to start of option list
	public int SelectNext() {
		if (selectedIndex == (optionCount - 1)) {
			selectedIndex = 0;
		} else {
			selectedIndex += 1;
		}
		UpdateOptionLabel();
		return selectedIndex;
	}
}
