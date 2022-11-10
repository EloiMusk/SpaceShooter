import greenfoot.Actor;
import greenfoot.Greenfoot;

/**
 * Upgrade class for all upgrades.
 *
 * @author EloiMusk
 * @version 1.0
 */
public class Upgrade extends Actor {
    /**
     * The type of upgrade.
     */
    public UpgradeType upgradeType;
    /**
     * The current frame of the animation.
     */
    private int animationFrame = 0;

    /**
     * Constructor for the upgrade. Chooses a random upgrade type:
     * <ul>
     *     <li>50%: Health or Shield</li>
     *     <li>35%: Booster Upgrades</li>
     *     <li>13%: Weapon Upgrades</li>
     *     <li>2%: Nuke</li>
     * </ul>
     */
    public Upgrade() {
        int random = Greenfoot.getRandomNumber(100);
        if (random < 50) {
            this.upgradeType = UpgradeType.values()[Greenfoot.getRandomNumber(2)];
        } else if (random < 85) {
            this.upgradeType = UpgradeType.values()[Greenfoot.getRandomNumber(5) + 2];
        } else if (random < 98) {
            this.upgradeType = UpgradeType.values()[(Greenfoot.getRandomNumber(3) + 7)];
        } else {
            this.upgradeType = UpgradeType.NUKE;
        }
        setImage("Upgrade/" + this.upgradeType.ordinal() + "/0.png");
    }

    /**
     * Constructor for the upgrade. Chooses a specific upgrade type.
     *
     * @param upgradeType The type of upgrade.
     */
    public Upgrade(UpgradeType upgradeType) {
        this.upgradeType = upgradeType;
        setImage("Upgrade/" + upgradeType.ordinal() + "/" + animationFrame + ".png");
    }

    /**
     * Moves the upgrade down the screen and calls the animation.
     */
    public void act() {
        animate();
        setLocation(getX(), getY() + 1);
        setRotation(getRotation() + 1);
        if (isAtEdge()) {
            getWorld().removeObject(this);
        }
    }

    /**
     * Animates the upgrade.
     */
    private void animate() {
        if (Space.animationMilliSeconds % 10 == 0) {
            if (animationFrame > 7) {
                animationFrame = 0;
            }
            setImage("Upgrade/" + upgradeType.ordinal() + "/" + animationFrame + ".png");
            animationFrame++;
        }
    }
}
