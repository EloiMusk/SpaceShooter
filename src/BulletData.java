public class BulletData {
    int speed;
    int damage;
    int size;
    int explosionSize;
    int explosionDamage;
    int explosionAnimatonFrameCount;
    int coolDown;
    int delay;

    public BulletData(int speed, int damage, int size, int explosionSize, int explosionDamage, int explosionAnimatonFrameCount, int coolDown, int delay) {
        this.speed = speed;
        this.damage = damage;
        this.size = size;
        this.explosionSize = explosionSize;
        this.explosionDamage = explosionDamage;
        this.explosionAnimatonFrameCount = explosionAnimatonFrameCount;
        this.coolDown = coolDown;
        this.delay = delay;
    }

    public static BulletData[] bullets =
            new BulletData[]{
//            Bullet type 1 (Default)
                    new BulletData(8, 100, 15, 30, 10, 6, 0, 50),
//            Bullet type 2 (Rocket)
                    new BulletData(10, 150, 50, 50, 100, 16, 20, 50),
//            Bullet type 3 (Bomb)
                    new BulletData(7, 20, 40, 80, 300, 15, 10, 50),
//            Bullet type 4 (Missile)
                    new BulletData(6, 20, 30, 80, 200, 30, 30, 70),
//            Bullet type 5 (Nuke)
                    new BulletData(5, 20, 90, 90, 200, 23, 10, 50)
            };

    public static BulletData getBullet(int type) {
        return bullets[type - 1];
    }
}