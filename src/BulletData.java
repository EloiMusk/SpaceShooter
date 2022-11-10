/**
 * Class to hold the data for a bullet.
 *
 * @author EloiMusk
 * @version 1.0
 */
public class BulletData {
    /**
     * The base damage of the bullet.
     */
    int speed;
    /**
     * The base damage of the bullet.
     */
    int damage;
    /**
     * The base size of the bullet.
     */
    int size;
    /**
     * The maximum animation frame of the bullet.
     */
    int maxAnimationFrames;
    /**
     * The size of the explosion.
     */
    int explosionSize;
    /**
     * The damage of the explosion.
     */
    int explosionDamage;
    /**
     * The maximum animation frame of the explosion.
     */
    int explosionAnimationFrameCount;
    /**
     * The cool down of the bullet that determines, how long the player will be able to use the bullet.
     */
    int coolDown;
    /**
     * The delay of the explosion damage to effect the aliens.
     */
    int delay;
    /**
     * The volume offset of the blast used to fine tune the audio composition.
     */
    int blastVolumeOffset;
    /**
     * The volume offset of the explosion used to fine tune the audio composition.
     */
    int explosionVolumeOffset;
    /**
     * The rate at which the player can fire the bullet.
     */
    int fireRate;

    /**
     * Constructor for the BulletData class. Sets the values of the bullet data.
     *
     * @param speed                        The speed of the bullet.
     * @param damage                       The damage of the bullet.
     * @param size                         The size of the bullet.
     * @param maxAnimationFrames           The maximum animation frame of the bullet.
     * @param explosionSize                The size of the explosion.
     * @param explosionDamage              The damage of the explosion.
     * @param explosionAnimationFrameCount The maximum animation frame of the explosion.
     * @param coolDown                     The cool down of the bullet that determines, how long the player will be able to use the bullet.
     * @param delay                        The delay of the explosion damage to effect the aliens.
     * @param blastVolumeOffset            The volume offset of the blast used to fine tune the audio composition.
     * @param explosionVolumeOffset        The volume offset of the explosion used to fine tune the audio composition.
     * @param fireRate                     The rate at which the player can fire the bullet.
     */
    public BulletData(int speed, int damage, int size, int maxAnimationFrames, int explosionSize, int explosionDamage, int explosionAnimationFrameCount, int coolDown, int delay, int blastVolumeOffset, int explosionVolumeOffset, int fireRate) {
        this.speed = speed;
        this.damage = damage;
        this.size = size;
        this.maxAnimationFrames = maxAnimationFrames;
        this.explosionSize = explosionSize;
        this.explosionDamage = explosionDamage;
        this.explosionAnimationFrameCount = explosionAnimationFrameCount;
        this.coolDown = coolDown;
        this.delay = delay;
        this.blastVolumeOffset = blastVolumeOffset;
        this.explosionVolumeOffset = explosionVolumeOffset;
        this.fireRate = fireRate;
    }

    // TODO: Balance Bullets
    /**
     * The values for the bullet types.
     */
    public static BulletData[] bullets = new BulletData[]{
//            Bullet type 1 (Default)
            new BulletData(8, 100, 15, 4, 30, 10, 6, 0, 50, -5, 0, 10),
//            Bullet type 2 (Rocket)
            new BulletData(10, 150, 50, 8, 50, 100, 16, 20, 50, -10, 0, 6),
//            Bullet type 3 (Bomb)
            new BulletData(7, 20, 40, 8, 80, 300, 15, 10, 50, -5, -5, 3),
//            Bullet type 4 (Missile)
            new BulletData(6, 20, 30, 8, 80, 200, 30, 30, 70, -15, 0, 5),
//            Bullet type 5 (Nuke)
            new BulletData(5, 20, 90, 8, 300, 200, 24, 10, 50, -10, 10, 1)};

    /**
     * @param type The bullet type to get the data for.
     *             <ul>
     *             <li>0 - Default</li>
     *             <li>1 - Rocket</li>
     *             <li>2 - Bomb</li>
     *             <li>3 - Missile</li>
     *             <li>4 - Nuke</li>
     *             </ul>
     * @return The bullet data for the given bullet type.
     */
    public static BulletData getBullet(int type) {
        return bullets[type - 1];
    }
}