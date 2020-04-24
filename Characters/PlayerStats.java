package Characters;

public class PlayerStats {
    private double playerSpeed;
    private double playerDamage;
    private double playerHealth;
    private double playerVisibility;

    public PlayerStats(){
        playerSpeed = 1;
        playerDamage = 1;
        playerHealth = 6;
        playerVisibility = 1;
    }

    public void increaseHealth(){
        playerHealth = playerHealth + 2;
    }

    public void increasePlayerSpeed(){
        playerSpeed = playerSpeed * 1.1;
    }

    public void decreaseVisibility(){
        playerVisibility = playerVisibility*0.9;
    }



}
