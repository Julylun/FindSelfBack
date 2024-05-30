package com.findselfback.Utilz;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import lombok.Data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


@Data
public class AudioPlayer {
    private static final String[] environmentAudioName = {"rainAudio.mp3"};

    private Player[] backgroundMusicList, effectSoundList;
    private MP3Player[] environmentAudioList;

    public AudioPlayer(){
        environmentAudioList = new MP3Player[environmentAudioName.length];
        for(int i = 0; i < environmentAudioList.length; i++){
            environmentAudioList[i] = new MP3Player("src/main/resources/audio/" + environmentAudioName[i]);
        }
    }
}
