import greenfoot.Actor;

public class AlienDeath extends Actor {
    private final int variant;
    private int animationFrame = 1;

    public AlienDeath(int variation) {
        this.variant = variation;
        if (variant <= 3) {
            SoundService deathSound = new SoundService("DeathExplosion/AlienOrganic/1.wav");
            deathSound.volumeOffset = 10;
            deathSound.playSound();
        }else {
            new SoundService("DeathExplosion/AlienMetal/1.wav").playSound();
        }

    }

    public void act() {
        if (variant <= 3) {
            if (Space.animationSeconds % 4 == 0) {
                if (animationFrame <= 10) {
                    setImage("DeathExplosion/AlienOrganic/" + String.format("%02d", animationFrame) + ".png");
                    getImage().scale(100, 100);
                    animationFrame++;
                } else {
                    getWorld().removeObject(this);
                }
            }
        } else {
            if (Space.animationSeconds % 4 == 0) {
                if (animationFrame <= 12) {
                    setImage("DeathExplosion/AlienMetal/" + String.format("%02d", animationFrame) + ".png");
                    getImage().scale(64, 64);
                    animationFrame++;
                } else {
                    getWorld().removeObject(this);
                }
            }
        }
    }
}
