import greenfoot.GreenfootSound;

public class SoundService {
    int volumeOffset = 0;
    GreenfootSound sound = new GreenfootSound("");
    boolean loop = false;
    String fileName;

    public SoundService(String file) {
        setSound(file);
    }

    public SoundService() {
    }

    public void setSound(String file) {
        this.fileName = file;
        sound = new GreenfootSound(fileName);
        sound.setVolume(Space.volume + volumeOffset);
    }

    public void setSound() {
        sound = new GreenfootSound(fileName);
        sound.setVolume(Space.volume + volumeOffset);
    }

    public void setVolume() {
        if (Space.volume <= 0) {
            sound.setVolume(Space.volume);
        } else {
            sound.setVolume(Space.volume + volumeOffset);
        }
    }

    public void playSound() {
        setSound();
        sound.setVolume(Space.volume + volumeOffset);
        if (loop) {
            sound.playLoop();
        } else {
            sound.play();
        }
    }

    public void playSound(String fileName) {
        setSound(fileName);
        if (Space.volume > 0) {
            sound.setVolume(Space.volume + volumeOffset);
            if (loop) {
                sound.playLoop();
            } else {
                sound.play();
            }
        }
    }

    public void stopSound() {
        sound.stop();
    }

    public boolean isPlaying() {
        if (sound == null) {
            return false;
        }
        return sound.isPlaying();
    }
}
