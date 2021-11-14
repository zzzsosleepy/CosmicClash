import java.util.List;
import javax.swing.JLabel;

public class Ship extends Sprite implements Runnable{
	
	protected int shipHealth;
	protected int projectileDamage;
	protected int projectileSpeed;
	protected String graphicFile;
	protected String hurtGraphicFile;
	protected String projectileGraphic;
	protected JLabel ShipLabel;
	protected Boolean isPlayer;
	//Shot is on cooldown by default to prevent double inputs from title screen
	public Boolean canShoot = false;
	public int shotCooldown = 200;
	
	public List<Ship> enemyList;
	public Ship playerShip;
	
	private Thread t;
	public ShipHealthManager healthManager;
	
	public int getShipHealth() { return shipHealth; }
	public void setShipHealth(int shipHealth) { this.shipHealth = shipHealth; }
	
	public String getGraphicFile() { return graphicFile; }
	public void setGraphicFile(String graphicFile) { this.graphicFile = graphicFile; }
	
	public String getHurtGraphicFile() {return hurtGraphicFile; }
	public void setHurtGraphicFile(String hurtGraphicFile) { this.hurtGraphicFile = hurtGraphicFile; }
	
	public String getProjectileGraphic() { return projectileGraphic; }
	public void setProjectileGraphic(String projectileGraphic) { this.projectileGraphic = projectileGraphic; }
	
	public int getProjectileDamage() { return projectileDamage; }
	public void setProjectileDamage(int projectileDamage) { this.projectileDamage = projectileDamage; }

	public int getProjectileSpeed() { return projectileSpeed; }
	public void setProjectileSpeed(int projectileSpeed) { this.projectileSpeed = projectileSpeed; }

	public JLabel getShipLabel() { return ShipLabel; }
	public void setShipLabel(JLabel shipLabel) { ShipLabel = shipLabel; }

	public Boolean getIsPlayer() { return isPlayer; }
	public void setIsPlayer(Boolean isPlayer) { this.isPlayer = isPlayer; }

	public Ship() {
		//Uses "Ship01.png" graphic by default and "Projectile01.png" by default
		super(32,32, "Ship01.png");
		this.graphicFile = "Ship01.png";
		this.hurtGraphicFile = "Ship01_Hurt.png";
		this.shipHealth = 1;
		this.projectileDamage = 1;
		this.projectileGraphic = "Projectile01.png";
		this.projectileSpeed = 20;
		this.isPlayer = false;
		//Force a cooldown when ship is created
		healthManager = new ShipHealthManager(shipHealth, graphicFile, hurtGraphicFile, ShipLabel, this);
		ProjectileCooldown();
	}
	
	public Ship(int shipHealth, int projectileDamage, String graphicFile, String hurtGraphicFile, int bulletSpeed, Boolean isPlayer, String projectileGraphic, JLabel ShipLabel) {
		super(32, 32, graphicFile);
		this.shipHealth = shipHealth;
		this.projectileDamage = projectileDamage;
		this.graphicFile = graphicFile;
		this.hurtGraphicFile = hurtGraphicFile;
		this.projectileGraphic = projectileGraphic;
		this.projectileSpeed = bulletSpeed;
		this.isPlayer = isPlayer;
		this.ShipLabel = ShipLabel;
		healthManager = new ShipHealthManager(shipHealth, graphicFile, hurtGraphicFile, ShipLabel, this);
		//Force a cooldown when ship is created
		ProjectileCooldown();
	}
	
	//Create and shoot a bullet projectile, returning the created bullet
	public Projectile Fire() {
		Projectile bullet;
		if (isPlayer) {
			bullet = new Projectile(projectileDamage, projectileSpeed, projectileGraphic, isPlayer);
		} else{
			bullet = new Projectile(projectileDamage, projectileSpeed, projectileGraphic, isPlayer);
			bullet.playerShip = playerShip;
		}
		bullet.enemyList = enemyList;
		bullet.ActivateProjectile(this);			
		//Force a cooldown between projectile shots
		ProjectileCooldown();
		
		//return the created projectile
		return bullet;
	}
	
	//Move the ship in a specified direction and update it's label location
	public void Move(int direction) {
		this.setY(direction);
		ShipLabel.setLocation(this.getX(), this.getY());
	}
	
	//Start the projectile cooldown thread
	public void ProjectileCooldown() {
		canShoot = false;
		t = new Thread(this, "Projectile Cooldown Thread");
		t.start();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

		//If the projectile is on cooldown, sleep for the cooldown duration and set the projectile to off cooldown
		if (!canShoot && healthManager.getIsAlive()) {
			try {
				Thread.sleep(shotCooldown);
				canShoot = true;
			} catch (Exception e) {
			}
		}
		
	}
}