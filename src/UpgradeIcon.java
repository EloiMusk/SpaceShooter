import greenfoot.GreenfootImage;

/**
 * UpgradeIcon class for all upgrade icons.
 *
 * @author EloiMusk
 * @version 1.0
 */
public class UpgradeIcon extends GreenfootImage {
    /**
     * The current frame of the animation.
     */
    private int animationFrame = 0;
    /**
     * The type of upgrade.
     */
    public final UpgradeType upgradeType;

    /**
     * Constructor for the upgrade icon. Sets the image to the first frame of the animation.
     *
     * @param type The type of upgrade.
     */
    public UpgradeIcon(UpgradeType type) {
        super("Upgrade/" + type.ordinal() + "/0.png");
        upgradeType = type;
    }

    /**
     * Animates the upgrade icon by setting the image to the next frame of the animation.
     */
    public void nextFrame() {
        if (animationFrame > 7) {
            animationFrame = 0;
        }
        clear();
        drawImage(new GreenfootImage("Upgrade/" + upgradeType.ordinal() + "/" + animationFrame + ".png"), 0, 0);
        animationFrame++;
    }
}
