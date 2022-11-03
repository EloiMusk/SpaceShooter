import greenfoot.GreenfootImage;

public class UpgradeIcon extends GreenfootImage {
    private int animationFrame = 0;
    public final UpgradeType upgradeType;

    public UpgradeIcon(UpgradeType type) {
        super("Upgrade/" + type.ordinal() + "/0.png");
        upgradeType = type;
    }

    public void nextFrame() {
        if (animationFrame > 7) {
            animationFrame = 0;
        }
        clear();
        drawImage(new GreenfootImage("Upgrade/" + upgradeType.ordinal() + "/" + animationFrame + ".png"), 0, 0);
        animationFrame++;
    }
}
