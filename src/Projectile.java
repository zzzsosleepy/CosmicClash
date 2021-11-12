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
		super(32,32, "Projectile01.png");
		this.damage = 1;
		this.speed = 20;
		this.graphicFile = "Projectile01.png";
		this.isVisible = true;
	}
	
	public Projectile(int dmg, int spd, String sprite) {
		super(32,32, sprite);
		this.damage = dmg;
		this.speed = spd;
		this.graphicFile = sprite;
		this.isVisible = true;
	}
	
	//Start the projectile thread
	public void MoveProjectile() {
		t = new Thread(this, "Projectile Thread");
		t.start();
	}
	
	//Setup the projectile label and position
	public void ActivateProjectile(Ship ship, Boolean isPlayerProjectile) {
		ImageIcon ProjectileImage;
		ProjectileLabel = new JLabel();
		ProjectileImage = new ImageIcon(getClass().getResource(graphicFile));
		ProjectileLabel.setIcon(ProjectileImage);
		ProjectileLabel.setSize(this.getWidth(), this.getHeight());
		this.SetVectors(ship.getX(), ship.getY());
		ProjectileLabel.setLocation(this.getX(), this.getY());
		this.isPlayerProjectile = isPlayerProjectile;
		MoveProjectile();
	}
	
	//Hide label and set to inactive
	public void DestroySelf() {
		this.isVisible = false;
		this.isActive = false;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.isActive = true;
		
		ProjectileLabel.setIcon( new ImageIcon( getClass().getResource(graphicFile)));
		
		while (isActive) {
			//movement routine
			//get current x,y
			int tx = this.x;
			
			//Move along the x axis
			if (isPlayerProjectile) {
				tx += GameProperties.CHARACTER_STEP;								
			} else {
				tx -= GameProperties.CHARACTER_STEP;	
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
	
//	private void detectPlayerCollision(Ship playerShip, JLabel playerLabel) {
//		if (this.r.intersects(playerShip.getRectangle())) {
//			System.out.println("Boom!");
//			this.isActive = false;
//			playerLabel.setIcon( new ImageIcon( getClass().getResource(".png") ));
//			DoctorLabel.setIcon( new ImageIcon( getClass().getResource("redDw12.png") ));
//		}
//	}
//	
//	private void detectEnemyCollision() {
//		if (this.r.intersects(myDoctor.getRectangle())) {
//			System.out.println("Boom!");
//			this.moving = false;
//			animationButton.setText("Run");
//			TardisLabel.setIcon( new ImageIcon( getClass().getResource("redTardis.png") ));
//			DoctorLabel.setIcon( new ImageIcon( getClass().getResource("redDw12.png") ));
//		}
//	}

}
