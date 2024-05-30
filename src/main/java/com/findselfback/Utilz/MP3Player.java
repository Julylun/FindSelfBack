package com.findselfback.Utilz;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.io.FileInputStream;
import java.io.IOException;

public class MP3Player {
    private String filename;
    private AdvancedPlayer player;
    private Thread playerThread;
    private boolean isPaused;
    private boolean isLooping;
    private int pausedOnFrame;

    public MP3Player(String filename) {
        this.filename = filename;
    }

    public void play() {
        if (isPaused) {
            resume();
        } else {
            startPlaying(0);
        }
    }

    private void startPlaying(int startFrame) {
        playerThread = new Thread(() -> {
            try (FileInputStream fileInputStream = new FileInputStream(filename)) {
                player = new AdvancedPlayer(fileInputStream);
                player.setPlayBackListener(new PlaybackListener() {
                    @Override
                    public void playbackFinished(PlaybackEvent evt) {
                        pausedOnFrame = evt.getFrame();
                        if (isLooping) {
                            startPlaying(0);
                        } else {
                            pausedOnFrame = 0;
                        }
                    }
                });
                player.play(startFrame, Integer.MAX_VALUE);
            } catch (JavaLayerException | IOException e) {
                e.printStackTrace();
            }
        });
        playerThread.start();
    }

    public void stop() {
        if (player != null) {
            player.close();
            playerThread.interrupt();
            isPaused = false;
            pausedOnFrame = 0;
        }
    }

    public void pause() {
        if (player != null) {
            isPaused = true;
            player.close();
            playerThread.interrupt();
        }
    }

    public void resume() {
        if (isPaused) {
            startPlaying(pausedOnFrame);
            isPaused = false;
        }
    }

    public void setLoop(boolean isLooping) {
        this.isLooping = isLooping;
    }

}
