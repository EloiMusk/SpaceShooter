import greenfoot.Actor;
import greenfoot.Color;
import greenfoot.GreenfootImage;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple UI class that is used to display the current stats to the player.
 *
 * @author EloiMusk
 * @version 1.0
 */
public class UI extends Actor {
    /**
     * Current animation frame for the health bar.
     */
    private int heartAnimationFrame = 0;
    /**
     * Current animation frame for the shield icon.
     */
    private int shieldAnimationFrame = 0;
    /**
     * Current animation frame for the ammunition icon.
     */
    private int ammunitionAnimationFrame = 0;
    /**
     * List of all currently active Upgrades.
     */
    private ArrayList<UpgradeIcon> upgradeIcons = new ArrayList<UpgradeIcon>();

    /**
     * Calls refreshUI() every cycle.
     */
    public void act() {
        refreshUI();
    }

    /**
     * Refreshes all the stats and icons of the HUD.
     */
    public void refreshUI() {
        GreenfootImage bg = getImage();
        bg.scale(800, 600);
        bg.clear();
        bg.setColor(Color.WHITE);
        SpaceShip spaceShip = getWorld().getObjects(SpaceShip.class).get(0);
        bg.drawString("Score: " + Space.score, 100, 50);
        bg.drawString("Level: " + Space.level, 700, 50);
        bg.drawImage(getHealthImage(spaceShip.health), 50, 550);
        bg.drawImage(getShieldImage(spaceShip.shield), 50, 500);
        bg.drawImage(getAmmunitionImage(spaceShip.ammunition), 700, 550);
        bg.drawString(spaceShip.ammunition + "", 700, 550);
        for (Map.Entry<UpgradeType, Boolean> upgrade : spaceShip.activeUpgrades.entrySet()) {
            if (upgrade.getValue() && upgradeIcons.stream().noneMatch(upgradeIcon -> upgradeIcon.upgradeType == upgrade.getKey()) && upgrade.getKey() != UpgradeType.HEALTH && upgrade.getKey() != UpgradeType.SHIELD) {
                upgradeIcons.add(new UpgradeIcon(upgrade.getKey()));
            } else if (!upgrade.getValue() && upgradeIcons.stream().anyMatch(upgradeIcon -> upgradeIcon.upgradeType == upgrade.getKey())) {
                try {
                    upgradeIcons.remove(upgradeIcons.stream().filter(upgradeIcon -> upgradeIcon.upgradeType == upgrade.getKey()).findFirst().get());
                } catch (Exception e) {
                    //Do nothing
                }
            }
        }
        int upgradeIconY = getWorld().getHeight() - 100;
        for (UpgradeIcon upgradeIcon : upgradeIcons) {
            if (Space.animationSeconds % 2 == 0) {
                upgradeIcon.nextFrame();
            }
            bg.drawImage(upgradeIcon, getWorld().getWidth() - upgradeIcon.getWidth(), upgradeIconY);
            upgradeIconY -= upgradeIcon.getHeight() + 10;
        }

        if (Space.animationMilliSeconds % 2 == 0) {
            ammunitionAnimationFrame++;
            if (ammunitionAnimationFrame > 33) {
                ammunitionAnimationFrame = 0;
            }
        }
        if (Space.animationMilliSeconds == 0) {
            shieldAnimationFrame++;
            heartAnimationFrame++;
            if (heartAnimationFrame > 3) {
                heartAnimationFrame = 0;
            }
            if (shieldAnimationFrame > 7) {
                shieldAnimationFrame = 0;
            }
        }
    }

    /**
     * Gets the current health bar image.
     *
     * @param health The current health of the player.
     * @return The current health bar image.
     */
    private GreenfootImage getHealthImage(int health) {
        GreenfootImage healthImage = new GreenfootImage("UI/Health/" + health + "/" + heartAnimationFrame + ".png");
        healthImage.scale(128, 32);
        return healthImage;
    }

    /**
     * Gets the current shield icon image.
     *
     * @param shield The current shield of the player.
     * @return The current shield icon image.
     */
    private GreenfootImage getShieldImage(int shield) {
        GreenfootImage shieldImage;
        if (shield <= 0) {
            shieldImage = new GreenfootImage("UI/Shield/0/0.png");

        } else {
            shieldImage = new GreenfootImage("UI/Shield/" + shield + "/" + shieldAnimationFrame + ".png");
        }
        shieldImage.scale(96, 32);
        return shieldImage;
    }

    /**
     * Gets the current ammunition icon image.
     *
     * @param ammunition The current ammunition of the player.
     * @return The current ammunition icon image.
     */
    private GreenfootImage getAmmunitionImage(int ammunition) {
        GreenfootImage ammunitionImage;
        if (ammunition <= 0) {
            ammunitionImage = new GreenfootImage("UI/Ammunition/0/" + String.format("%02d", ammunitionAnimationFrame) + ".png");
        } else if (ammunition <= 10) {
            ammunitionImage = new GreenfootImage("UI/Ammunition/1/" + String.format("%02d", ammunitionAnimationFrame) + ".png");
        } else if (ammunition <= 20) {
            ammunitionImage = new GreenfootImage("UI/Ammunition/2/" + String.format("%02d", ammunitionAnimationFrame) + ".png");
        } else if (ammunition <= 30) {
            ammunitionImage = new GreenfootImage("UI/Ammunition/3/" + String.format("%02d", ammunitionAnimationFrame) + ".png");
        } else {
            ammunitionImage = new GreenfootImage("UI/Ammunition/4/" + String.format("%02d", ammunitionAnimationFrame) + ".png");
        }
        ammunitionImage.scale(32, 32);
        return ammunitionImage;
    }

}
