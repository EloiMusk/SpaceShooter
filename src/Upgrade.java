import greenfoot.Actor;

public class Upgrade extends Actor {
    public UpgradeType upgradeType;
    private int animationFrame = 0;

    public Upgrade() {
        this.upgradeType = UpgradeType.HEALTH;
        setImage("Upgrade/" + this.upgradeType.ordinal() + "/0.png");
    }

    public Upgrade(UpgradeType upgradeType) {
        this.upgradeType = upgradeType;
        setImage("Upgrade/" + upgradeType.ordinal() + "/" + animationFrame + ".png");
    }

    private void animate() {
        if (Space.animationTimer % 2 == 0) {
            if (animationFrame >= 3) {
                animationFrame = 0;
            }
            setImage("Upgrade/" + upgradeType.ordinal() + "/" + animationFrame + ".png");
            animationFrame++;
        }
    }

    public void act() {
        animate();
        setLocation(getX(), getY() + 1);
        setRotation(getRotation() + 1);
    }
}
