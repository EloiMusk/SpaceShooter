import greenfoot.Actor;
import greenfoot.Greenfoot;

public class Upgrade extends Actor {
    public UpgradeType upgradeType;
    private int animationFrame = 0;

    public Upgrade() {
        int random = Greenfoot.getRandomNumber(100);
        if (random < 50) {
            this.upgradeType = UpgradeType.values()[Greenfoot.getRandomNumber(2)];
        } else if (random < 80) {
            this.upgradeType = UpgradeType.values()[Greenfoot.getRandomNumber(5) + 2];
        } else if (random < 95) {
            this.upgradeType = UpgradeType.values()[(Greenfoot.getRandomNumber(3) + 7)];
        } else {
            this.upgradeType = UpgradeType.NUKE;
        }
        this.upgradeType = UpgradeType.NUKE;
        setImage("Upgrade/" + this.upgradeType.ordinal() + "/0.png");
    }

    public Upgrade(UpgradeType upgradeType) {
        this.upgradeType = upgradeType;
        setImage("Upgrade/" + upgradeType.ordinal() + "/" + animationFrame + ".png");
    }

    private void animate() {
        if (Space.animationMilliSeconds % 10 == 0) {
            if (animationFrame > 7) {
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
        if (isAtEdge()) {
            getWorld().removeObject(this);
        }
    }
}
