package com.findselfback.Utilz;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import lombok.Data;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


@Data
public class AudioPlayer {
    private static final String[] environmentAudioName = {"HowlingWind.mp3"};


    private MP3Player[] environmentAudioList;

    public AudioPlayer(){
        environmentAudioList = new MP3Player[environmentAudioName.length];
        for(int i = 0; i < environmentAudioList.length; i++){
            environmentAudioList[i] = new MP3Player("Resources/audio/" + environmentAudioName[i]);
        }
    }

    public static Clip getClip(String filePath){
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat baseFormat = audioInputStream.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false);
            AudioInputStream decodedAudioInputStream = AudioSystem.getAudioInputStream(decodedFormat, audioInputStream);

            Clip clip = AudioSystem.getClip();
            clip.open(decodedAudioInputStream);
            return clip;
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }
    }
}
