public class GameProperties {
//	13 x 32 ~ tweaked to account for java window sizing
	public static final int SCREEN_WIDTH = 432;
	public static final int SCREEN_HEIGHT = 454;
	
	//Low character step allows for smaller movements
	public static final int CHARACTER_STEP = 16;
	
	public static String PLAYER_NAME = "";
	public static int PLAYER_SCORE = 0;
	public static int SHOT_COUNT = 0;
	public static int CURRENT_LEVEL = 1;
	
	//This will return a random number between the min and max values inputted
	public static int RandomRange(int min, int max) {
		double r = Math.random();
		int randomNum = (int)(r * (max - min)) + min;
		return randomNum;
	}
}