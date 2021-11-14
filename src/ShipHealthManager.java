import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ShipHealthManager implements Runnable{
	protected int currentHealth;
	protected int maxHealth;
	protected String defaultSprite;
	protected String hurtSprite;
	protected Ship parentShip;
	
	public String getDefaultSprite() { return defaultSprite; }
	public void setDefaultSprite(String defaultSprite) { this.defaultSprite = defaultSprite; }
	
	public String getHurtSprite() { return hurtSprite; }
	public void setHurtSprite(String hurtSprite) { this.hurtSprite = hurtSprite; }
	
	public JLabel getShipLabel() { return shipLabel; }
	public void setShipLabel(JLabel shipLabel) { this.shipLabel = shipLabel; }
	
	public Boolean getIsAlive() { return isAlive; }
	public void setIsAlive(Boolean isAlive) { this.isAlive = isAlive; }
	
	public Thread getFlashThread() { return flashThread; }
	public void setFlashThread(Thread flashThread) { this.flashThread = flashThread; }
	
	public Boolean getIsActive() { return isActive; }
	public void setIsActive(Boolean isActive) { this.isActive = isActive; }
	
	public Boolean getIsFlashing() { return isFlashing; }
	public void setIsFlashing(Boolean isFlashing) { this.isFlashing = isFlashing; }
	
	public int getCurrentHealth() { return currentHealth; }
	public void setCurrentHealth(int currentHealth) { this.currentHealth = currentHealth; }

	public int getMaxHealth() { return maxHealth; }
	public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }
	
	public Ship getParentShip() { return parentShip; }
	public void setParentShip(Ship parentShip) { this.parentShip = parentShip; }	
	
	protected JLabel shipLabel;
	protected Boolean isAlive = true;
	
	private Thread flashThread;
	private Boolean isActive;
	private Boolean isFlashing = false;
	
	public ShipHealthManager(int maxHealth, String defaultSprite, String hurtSprite, JLabel shipLabel, Ship parentShip) {
		this.currentHealth = maxHealth;
		this.maxHealth = maxHealth;
		this.defaultSprite = defaultSprite;
		this.hurtSprite = hurtSprite;
		this.shipLabel = shipLabel;
		this.parentShip = parentShip;
	}

	public void TakeDamage(int damage) {
		currentHealth -= damage;
		isFlashing = true;
		Activate();
		if (currentHealth <= 0) {
			currentHealth = 0;
			Die();
		}
	}
	
	public void Heal(int amount) {
		currentHealth += amount;
		if (currentHealth > maxHealth) {
			currentHealth = maxHealth;
		}
	}
	
	public void Die() {
		shipLabel.setVisible(false);
		isAlive = false;
		if (parentShip.getIsPlayer()) {
			//Player dies
			System.out.println("Player has died");
		} else {
			//Enemy dies
			GameProperties.PLAYER_SCORE++;
		}
	}
	
	public void Activate() {
		isActive = true;
		flashThread = new Thread(this, "Health flash thread");
		flashThread.start();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(isActive) {
			try {
				if (isFlashing) {
					shipLabel.setIcon(new ImageIcon(getClass().getResource(hurtSprite)));
				}
				Thread.sleep(200);
				shipLabel.setIcon(new ImageIcon(getClass().getResource(defaultSprite)));
				isFlashing = false;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
