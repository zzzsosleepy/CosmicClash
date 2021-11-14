import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Projectile extends Sprite implements Runnable{
	protected int damage;
	protected int speed;
	protected String graphicFile;
	protected Boolean isVisible, isActive;
	private Thread t;
	public JLabel ProjectileLabel;
	protected Boolean isPlayerProjectile;
	protected List<Ship> enemyList;
	protected Ship playerShip;
	
	public int getDamage() { return damage; }
	
	public void setDamage(int damage) { this.damage = damage; }
	
	public int getSpeed() { return speed; }
	
	public void setSpeed(int speed) { this.speed = speed; }
	
	public String getGraphicFile() { return graphicFile; }
	
	public void setGraphicFile(String graphicFile) { this.graphicFile = graphicFile; }
	
	public Boolean getIsVisible() { return isVisible; }
	
	public void setIsVisible(Boolean isVisible) { this.isVisible = isVisible; }
	
	public Boolean getIsActive() { return isActive; }
	
	public void setIsActive(Boolean isActive) { this.isActive = isActive; }
	
	public Projectile() {
		super(24, 10, "Projectile01.png");
		this.damage = 1;
		this.speed = 20;
		this.graphicFile = "Projectile01.png";
		this.isVisible = true;
		this.isActive = true;
	}
	
	public Projectile(int dmg, int spd, String sprite, Boolean isPlayerProjectile) {
		super(24, 10, sprite);
		this.damage = dmg;
		this.speed = spd;
		this.graphicFile = sprite;
		this.isVisible = true;
		this.isActive = true;
		this.isPlayerProjectile = isPlayerProjectile;
	}
	
	//Start the projectile thread
	public void MoveProjectile() {
		t = new Thread(this, "Projectile Thread");
		t.start();
	}
	
	//Setup the projectile label and position
	public void ActivateProjectile(Ship ship) {
		ImageIcon ProjectileImage;
		ProjectileLabel = new JLabel();
		ProjectileImage = new ImageIcon(getClass().getResource(graphicFile));
		ProjectileLabel.setIcon(ProjectileImage);
		ProjectileLabel.setSize(this.getWidth(), this.getHeight());
		if (isPlayerProjectile) {
			this.SetVectors(ship.getX() + 12, ship.getY() + 10);			
		} else {
			this.SetVectors(ship.getX() - 12, ship.getY() + 10);
		}
		ProjectileLabel.setLocation(this.getX(), this.getY());
		MoveProjectile();
	}
	
	//Hide label and set to inactive
	public void DestroySelf() {
		this.isVisible = false;
		this.isActive = false;
		ProjectileLabel.setVisible(false);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		ProjectileLabel.setIcon( new ImageIcon( getClass().getResource(graphicFile)));
		
		while (isActive) {
			//movement routine
			//get current x,y
			int tx = this.x;
			
			//Move along the x axis
			if (isPlayerProjectile) {
				tx += GameProperties.CHARACTER_STEP;	
				this.detectEnemyCollision(enemyList);
			} else {
				tx -= GameProperties.CHARACTER_STEP;	
				this.detectPlayerCollision(playerShip, playerShip.ShipLabel);
			}
			
			//Destroy projectile if off-screen
			if ( tx > (GameProperties.SCREEN_WIDTH) || tx < 0) {
				DestroySelf();
			}
			
			this.setX(tx);
			ProjectileLabel.setLocation(this.x, this.y);
			
			try {
				Thread.sleep(speed);
			} catch (Exception e) {
				//Catch exception
			}
		}
		
	}
	
	private void detectPlayerCollision(Ship playerShip, JLabel playerLabel) {
		if (this.r.intersects(playerShip.getRectangle())) {
			this.isActive = false;
			playerShip.healthManager.TakeDamage(damage);
			this.DestroySelf();
		}
	}
	
	private void detectEnemyCollision(List<Ship> enemyList) {
		if (enemyList.size() > 0) {			
			for(Ship enemy : enemyList) {
				//If the projectile comes into contact with an enemy from the enemy list, and the enemy is visible; damage the enemy and destroy this projectile
				if (this.r.intersects(enemy.getRectangle())) {
					if (enemy.ShipLabel.isVisible()) {
						this.isActive = false;
						enemy.healthManager.TakeDamage(damage);
						this.DestroySelf();					
					}
				}
			}
		}
	}

}
