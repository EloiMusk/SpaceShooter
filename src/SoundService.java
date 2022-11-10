import greenfoot.GreenfootSound;

/**
 * The sound service. This class is responsible for playing sounds. This way, we can easily control the volume of the sounds.
 *
 * @author EloiMusk
 * @version 1.0
 */
public class SoundService {
    /**
     * The offset for the volume.
     *
     * @default: 0
     */
    int volumeOffset = 0;
    /**
     * The instance of the sound service.
     */
    GreenfootSound sound = new GreenfootSound("");
    /**
     * Whether the sound is looping or not.
     */
    boolean loop = false;
    /**
     * The file name of the sound.
     */
    String fileName;

    /**
     * Constructor for the sound service.
     *
     * @param file The file name of the sound.
     */
    public SoundService(String file) {
        setSound(file);
    }

    /**
     * Empty constructor for the sound service.
     */
    public SoundService() {
    }

    /**
     * Sets the sound from a parameter.
     *
     * @param file The file name of the sound.
     */
    public void setSound(String file) {
        this.fileName = file;
        sound = new GreenfootSound(fileName);
        setVolume();
    }

    /**
     * Plays the sound from the instance variable fileName.
     */
    public void setSound() {
        sound = new GreenfootSound(fileName);
        setVolume();
    }

    /**
     * Sets the volume of the sound from the instance variable volumeOffset.
     */
    public void setVolume() {
        if (Space.volume <= 0) {
            sound.setVolume(Space.volume);
        } else {
            sound.setVolume(Space.volume + volumeOffset);
        }
    }

    /**
     * Plays the sound from the instance variable fileName.
     */
    public void playSound() {
        setSound();
        setVolume();
        if (loop) {
            sound.playLoop();
        } else {
            sound.play();
        }
    }

    /**
     * Plays the sound from a parameter.
     *
     * @param fileName The file name of the sound.
     */
    public void playSound(String fileName) {
        setSound(fileName);
        setVolume();
        if (loop) {
            sound.playLoop();
        } else {
            sound.play();
        }
    }

    /**
     * Stops the sound.
     */
    public void stopSound() {
        sound.stop();
    }

    /**
     * Checks if the sound is playing.
     *
     * @return Whether the sound is playing or not.
     */
    public boolean isPlaying() {
        return sound.isPlaying();
    }
}
