public class BulletData {
    int speed;
    int damage;
    int size;
    int explosionSize;
    int explosionDamage;
    int explosionAnimatonFrameCount;
    int coolDown;
    int delay;
    int blastVolumeOffset;
    int explosionVolumeOffset;
    int fireRate;

    public BulletData(int speed, int damage, int size, int explosionSize, int explosionDamage, int explosionAnimatonFrameCount, int coolDown, int delay, int blastVolumeOffset, int explosionVolumeOffset, int fireRate) {
        this.speed = speed;
        this.damage = damage;
        this.size = size;
        this.explosionSize = explosionSize;
        this.explosionDamage = explosionDamage;
        this.explosionAnimatonFrameCount = explosionAnimatonFrameCount;
        this.coolDown = coolDown;
        this.delay = delay;
        this.blastVolumeOffset = blastVolumeOffset;
        this.explosionVolumeOffset = explosionVolumeOffset;
        this.fireRate = fireRate;
    }

    // TODO: Balance Bullets
    public static BulletData[] bullets = new BulletData[]{
//            Bullet type 1 (Default)
            new BulletData(8, 100, 15, 30, 10, 6, 0, 50, -5, 0, 10),
//            Bullet type 2 (Rocket)
            new BulletData(10, 150, 50, 50, 100, 16, 20, 50, -10, 0, 6),
//            Bullet type 3 (Bomb)
            new BulletData(7, 20, 40, 80, 300, 15, 10, 50, -5, -5, 3),
//            Bullet type 4 (Missile)
            new BulletData(6, 20, 30, 80, 200, 30, 30, 70, -15, 0, 5),
//            Bullet type 5 (Nuke)
            new BulletData(5, 20, 90, 300, 200, 24, 10, 50, -10, 10, 1)};

    public static BulletData getBullet(int type) {
        return bullets[type - 1];
    }
}