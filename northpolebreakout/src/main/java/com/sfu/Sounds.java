package com.sfu;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class Sounds {
    private Clip backgroundMusicClip;
    private Clip wallCollision;
    private Clip enemyCollision;
    private Clip present;
    private Clip bomb;

    /**
     * Default constructor
     * Loads the sound clips
     */
    public Sounds (){
        try {
            // Load sound from the classpath (resources folder)
            backgroundMusicClip = loadClip("/Sound/background_music.wav");
            wallCollision = loadClipWithDifferentVolume("/Sound/wall_collision.wav", 6.0f);
            enemyCollision = loadClipWithDifferentVolume("/Sound/enemy_collision.wav", 6.0f);
            present = loadClip("/Sound/present.wav");
            bomb = loadClip("/Sound/bomb.wav");

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Plays background music indefinitely, until {@link Sounds#stopBackgroundMusic()} is called
     */
    public void playBackgroundMusic() {
        backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Stops background music if background music is on
     */
    public void stopBackgroundMusic() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
        }
    }

    /**
     * Loads a clip from a music file so that it can later be played
     * @param filePath the file path of the music clip
     * @return A {@link Clip} with {@link Clip#open(AudioInputStream)} already called
     */
    private Clip loadClip(String filePath)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException, URISyntaxException {
        File soundFile = new File(getClass().getResource(filePath).toURI());
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        return clip;
    }

    /**
     * Loads a clip from a music file so that it can later be played
     * @param filePath the file path of the music clip
     * @param volume the volume the clip sound be increased by, max is about 6.0f/-6.0f
     * @return A {@link Clip} with {@link Clip#open(AudioInputStream)} already called, and adjusted volume
     */
    private Clip loadClipWithDifferentVolume(String filePath, float volume)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException, URISyntaxException {
        // load clip
        File soundFile = new File(getClass().getResource(filePath).toURI());
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);

        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float currentVolume = gainControl.getValue();   // old volume

        float newVolume = currentVolume + volume;
        gainControl.setValue(newVolume);                // set volume to old volume + volume factor

        return clip;
    }

    /**
     * Plays the provided clip from the beginning to end, if the clip is not already running
     * @param clip the clip to be played
     */
    private void playSound(Clip clip) {
        if (!clip.isRunning()) {
            clip.setFramePosition(0); // start from beginning
            clip.start();
        }
    }

    /**
     * Always plays the provided clip from the beginning to end
     * @param clip the clip to be played
     */
    private void alwaysPlaySound(Clip clip) {
        clip.setFramePosition(0); // start from beginning
        clip.start();

    }


    /**
     * Plays the wall collision sound, unless the sound is already being played
     */
    public void playWallCollision() {
        playSound(wallCollision);
    }

    /**
     * Plays the enemy collision sound, unless the sound is already being played
     */
    public void playEnemyCollision() {
        playSound(enemyCollision);
    }


    /**
     * Plays the present collection sound,
     * if the sound is already playing, it stops the sound and plays it from the beginning again
     */
    public void playPresentSound() {
        alwaysPlaySound(present);
    }

    /**
     * Plays the bomb collection sound,
     * if the sound is already playing, it stops the sound and plays it from the beginning again
     */
    public void playBombSound() {
        alwaysPlaySound(bomb);
    }

}
