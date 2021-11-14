
public class GameStateManager {
	//0 is title screen // 1 is game-play // 2 is paused // 3 is level end // 4 is dead
	public int gameState;

	public int getGameState() {
		return gameState;
	}

	public void setGameState(int gameState) {
		this.gameState = gameState;
	}
	
	public GameStateManager() {
		this.gameState = 0;
	}
	
	public GameStateManager(int state) {
		this.gameState = state;
	}
}
