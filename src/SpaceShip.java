import greenfoot.Actor;
import greenfoot.Greenfoot;

public class SpaceShip extends Actor {
    private int animationFrame = 0;
    public int health = 3;
    public int shield = 0;
    public int ammunition = 50;
    private boolean isShooting = false;

    public SpaceShip() {
        setImage("SpaceShip/SpaceShip0.png");
    }

    private void animation() {
        if (Space.animationTimer == 1){
            if (animationFrame > 2) {
                animationFrame = 0;
            }
            setImage("SpaceShip/SpaceShip" + animationFrame + ".png");
            animationFrame++;
        }
    }

    public void controls() {
        if (Greenfoot.isKeyDown("a")) {
            setLocation(getX() - 5, getY());
        }
        if (Greenfoot.isKeyDown("d")) {
            setLocation(getX() + 5, getY());
        }
        if (Greenfoot.isKeyDown("w")) {
            setLocation(getX(), getY() - 5);
        }
        if (Greenfoot.isKeyDown("s")) {
            setLocation(getX(), getY() + 5);
        }
        if (Greenfoot.isKeyDown("space") && !isShooting) {
            if (this.ammunition > 0){
                getWorld().addObject(new Bullet(), getX(), getY() - 60);
                isShooting = true;
                this.ammunition--;
            }else {
//                TODO: Add effect for no ammunition
                System.out.println("Out of ammunition");
            }
        }
        if (!Greenfoot.isKeyDown("space") && isShooting) {
            isShooting = false;
        }
    }

    public void isHit(){
        if (isTouching(Alien.class)){
            removeTouching(Alien.class);
            if (this.shield > 0){
                this.shield--;
            }else {
                this.health--;
            }
        }
        if (this.health <= 0){
            Space.gameOver();
        }
    }

    public void isHitByUpgrade(){
        if (isTouching(Upgrade.class)){
            Upgrade upgrade = (Upgrade) getOneIntersectingObject(Upgrade.class);
            switch (upgrade.upgradeType){
                case HEALTH:
                    if (this.health < 4){
                        this.health++;
                    }
                    break;
                case SHIELD:
                    if (this.shield < 3){
                        this.shield++;
                    }
                    break;
            }
            getWorld().removeObject(upgrade);
        }
        if(isTouching(Ammunition.class)){
            if ((this.ammunition + 5) <= 40){
                this.ammunition += 5;
            }else {
                this.ammunition = 40;
            }
            getWorld().removeObject(getOneIntersectingObject(Ammunition.class));
        }
    }

    public void act() {
        animation();
        controls();
        isHit();
        isHitByUpgrade();
    }
}
