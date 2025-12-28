package classes;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class SoundManager {

    private static MediaPlayer bgPlayer;
    private static boolean muted = false;
    private static Path tempAudioFile;

    public static void playBackground(String resourcePath) {
        if (bgPlayer != null) return;

        try {
            InputStream is = SoundManager.class.getResourceAsStream(resourcePath);
            tempAudioFile = Files.createTempFile("bg_sound", ".mp3");
            Files.copy(is, tempAudioFile, StandardCopyOption.REPLACE_EXISTING);
            tempAudioFile.toFile().deleteOnExit();

            Media media = new Media(tempAudioFile.toUri().toString());
            bgPlayer = new MediaPlayer(media);
            bgPlayer.setCycleCount(MediaPlayer.INDEFINITE);

            if (!muted) bgPlayer.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isMuted() {
        return muted;
    }
    
    public static void pauseBackground() {
            if (bgPlayer != null) {
                bgPlayer.pause();
            }
        }

    public static void resumeBackground() {
        if (bgPlayer != null && !muted) {
            bgPlayer.play();
        }
    }

    public static void toggleMute() {
        muted = !muted;
        if (bgPlayer != null) {
            if (muted) bgPlayer.pause();
            else bgPlayer.play();
        }
    }
}

