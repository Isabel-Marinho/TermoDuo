import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {
    private static final String SOUNDS_FOLDER = "sounds/";

    public static void playSound(String fileName) {
        // Garante que fileName seja "final" ou "effectively final"
        final String soundFileToPlay = fileName.startsWith(SOUNDS_FOLDER)
                ? fileName.replace(SOUNDS_FOLDER, "")
                : fileName;

        new Thread(() -> {
            try {
                String fullPath = SOUNDS_FOLDER + soundFileToPlay;
                File soundFile = new File(fullPath);

                if (!soundFile.exists()) {
                    System.err.println("Arquivo não encontrado: " + soundFile.getAbsolutePath());
                    return;
                }

                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();

                if (!soundFileToPlay.equals("win.wav")) {
                    Thread.sleep(clip.getMicrosecondLength() / 1000);
                }
            } catch (Exception e) {
                System.err.println("Erro ao reproduzir " + soundFileToPlay + ": " + e.getMessage());
            }
        }).start();
    }
}