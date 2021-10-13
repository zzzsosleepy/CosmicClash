
public class Enemy extends Sprite{

	protected String graphicFile;
	protected int health;
	
	public Enemy() {
		//Graphic is enemy ship by default
		super(32, 32, "EnemyShip01.png");
		
		//Health is 1 by default
		this.health = 1;
	}
	
	public Enemy(int health, String graphicFile) {
		super(32, 32, graphicFile);
		this.health = health;
	}
	
}
