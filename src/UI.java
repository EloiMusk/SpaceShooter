import greenfoot.Actor;
import greenfoot.Color;
import greenfoot.GreenfootImage;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class UI extends Actor {

    private int heartAnimationFrame = 0;
    private int shieldAnimationFrame = 0;
    private int ammunitionAnimationFrame = 0;
    private ArrayList<UpgradeIcon> upgradeIcons = new ArrayList<UpgradeIcon>();

    private GreenfootImage getHealthImage(int health) {
        GreenfootImage healthImage = new GreenfootImage("UI/Health/" + health + "/" + heartAnimationFrame + ".png");
        healthImage.scale(128, 32);
        return healthImage;
    }

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

    public void act() {
        refreshUI();
    }

}
