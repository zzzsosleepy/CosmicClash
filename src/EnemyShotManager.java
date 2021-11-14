import java.util.List;
import javax.swing.JLayeredPane;

public class EnemyShotManager implements Runnable{
	protected List<Ship> enemies;
	protected JLayeredPane layeredPane;
	private Thread shotThread;
	protected Boolean isActive;
	protected GameStateManager stateManager;
	
	public EnemyShotManager(List<Ship> enemies, JLayeredPane layeredPane, GameStateManager stateManager) {
		this.enemies = enemies;
		this.layeredPane = layeredPane;
		this.stateManager = stateManager;
	}
	
	//Activate the shooting thread
	public void Activate() {
		isActive = true;
		shotThread = new Thread(this, "Enemy Shot Manager Thread");
		shotThread.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isActive) {
			//If the game state is gameplay and the enemies array has more than 0 items
			//Randomly fire a shot from one of the enemies in the array every x seconds
			if (stateManager.getGameState() == 1) {
				if (enemies.size() > 0) {
					int randNum = GameProperties.RandomRange(0, enemies.size());
					if (enemies.get(randNum).healthManager.isAlive) {
						Projectile bullet = enemies.get(randNum).Fire();
						layeredPane.add(bullet.ProjectileLabel, Integer.valueOf(99));					
					}					
				}
				try {
					Thread.sleep(700);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		}
	}
}
