import greenfoot.GreenfootSound;

public class SoundService extends GreenfootSound {
    public SoundService(String fileName) {
        super(fileName);
    }

    public void playSound() {
        if (Space.volume > 0) {
            setVolume(Space.volume);
            play();
        }
    }
}
