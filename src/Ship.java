import javax.swing.JLabel;

public class Ship extends Sprite{
	
	protected int shipHealth;
	protected int projectileDamage;
	protected int projectileSpeed;
	protected String graphicFile;
	protected String hurtGraphicFile;
	protected String projectileGraphic;
	protected Boolean isVisible;
	protected JLabel ShipLabel;
	protected Boolean isPlayer;
	
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

	public Boolean getIsVisible() { return isVisible; }
	public void setIsVisible(Boolean isVisible) { this.isVisible = isVisible; }

	public JLabel getShipLabel() { return ShipLabel; }
	public void setShipLabel(JLabel shipLabel) { ShipLabel = shipLabel; }

	public Boolean getIsPlayer() { return isPlayer; }
	public void setIsPlayer(Boolean isPlayer) { this.isPlayer = isPlayer; }

	public Ship() {
		//Uses "Ship01.png" graphic by default and "Projectile01.png" by default
		super(32,32, "Ship01.png");
		graphicFile = "Ship01.png";
		hurtGraphicFile = "Ship01_Hurt.png";
		shipHealth = 1;
		projectileDamage = 1;
		projectileGraphic = "Projectile01.png";
		projectileSpeed = 20;
		isPlayer = false;
	}
	
	public Ship(int health, int damage, String spritePath, String hurtSpritePath, int bulletSpeed, Boolean isPlayer) {
		super(32, 32, spritePath);
		shipHealth = health;
		projectileDamage = damage;
		graphicFile = spritePath;
		hurtGraphicFile = hurtSpritePath;
		projectileGraphic = "Projectile01.png";
		projectileSpeed = bulletSpeed;
		this.isPlayer = isPlayer;
	}
	
	//Create and shoot a bullet projectile, returning the created bullet
	public Projectile Fire() {
		Projectile bullet = new Projectile(projectileDamage, projectileSpeed, projectileGraphic);
		if (isPlayer) {
			bullet.ActivateProjectile(this, true);			
		} else {
			bullet.ActivateProjectile(this, false);			
		}
		return bullet;
	}
}