public class Ship extends Sprite{
	
	protected int shipHealth;
	protected int projectileDamage;
	protected int projectileSpeed;
	protected String graphicFile;
	protected String projectileGraphic;
	protected Boolean isVisible;
	
	public int getShipHealth() { return shipHealth; }
	
	public void setShipHealth(int shipHealth) { this.shipHealth = shipHealth; }
	
	public String getGraphicFile() { return graphicFile; }
	
	public void setGraphicFile(String graphicFile) { this.graphicFile = graphicFile; }
	
	public String getProjectileGraphic() { return projectileGraphic; }
	
	public void setProjectileGraphic(String projectileGraphic) { this.projectileGraphic = projectileGraphic; }
	
	public int getProjectileDamage() { return projectileDamage; }

	public void setProjectileDamage(int projectileDamage) { this.projectileDamage = projectileDamage; }

	public int getProjectileSpeed() { return projectileSpeed; }

	public void setProjectileSpeed(int projectileSpeed) { this.projectileSpeed = projectileSpeed; }

	public Ship() {
		//Uses "Ship01.png" graphic by default and "Projectile01.png" by default
		super(32,32, "Ship01.png");
		graphicFile = "Ship01.png";
		shipHealth = 1;
		projectileDamage = 1;
		projectileGraphic = "Projectile01.png";
		projectileSpeed = 20;
	}
	
	public Ship(int health, int damage, String spritePath, int bulletSpeed) {
		super(32, 32, spritePath);
		shipHealth = health;
		projectileDamage = damage;
		graphicFile = spritePath;
		projectileGraphic = "Projectile01.png";
		projectileSpeed = bulletSpeed;
	}
	
	//Create and shoot a bullet projectile, returning the created bullet
	public Projectile Fire() {
		Projectile bullet = new Projectile(projectileDamage, projectileSpeed, projectileGraphic);
		bullet.ActivateProjectile(this);
		return bullet;
	}
}