/**
 * The SoundProcessor class is responsible for handling audio playback for a specified sound file.
 */
package assets.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;


public class SoundProcessor {
    private String soundFilePath;

    public SoundProcessor(String soundFilePath) {
        this.soundFilePath = soundFilePath;
    }

    public void playSound() {
        try {
            File soundFile = new File(soundFilePath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        
        }
    }
}