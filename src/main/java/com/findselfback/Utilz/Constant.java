package com.findselfback.Utilz;

import com.findselfback.GameState.GameState;
import com.findselfback.View.GamePlayPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Constant {
    public static class EnvironmentValue {
        public static float gravity = (float)(0.02f * GamePlayPanel.SCALE);
    }

    public static class AudioPath {
        public static class Player {
            public static final String WALK = "Resources/audio/player/playerWalkSound.wav";
            public static final String JUMP = "Resources/audio/player/jumpPlayerSound.wav";

        }
    }
    public static class AssetPath {
        public static class Environment {
            public static final String SNOW = "Resources/assets/snow.png";
            public static final String RAIN = "Resources/assets/rain.png";
        }
    }
    public static class Animation{
        public static final int IDLE = 0;
        public static final int WALKING = 1;
        public static final int RUNNING = 2;
        public static final int ATTACKING = 3;
        public static final int FALLING = 4;

        public static final int SITTING = 5;
    }
    public static class UI {
        public static class MenuSize {
            public static final float ORIGINAL_WIDTH = 64;
            public static final float ORIGINAL_HEIGHT = 64;
            public static final float SCALING_HEIGHT = (float)(ORIGINAL_HEIGHT * GamePlayPanel.SCALE);
            public static final float SCALING_WIDTH = (float)(ORIGINAL_WIDTH * GamePlayPanel.SCALE);
            public static final Font FVF_FERNANDO;
            public static final Font FVF_FERNANDO_CONVERSATION;
            static {
                try {
                    FVF_FERNANDO = Font.createFont(Font.TRUETYPE_FONT,new File("Resources/font/FVF-Fernando.ttf")).deriveFont(32f);
                    FVF_FERNANDO_CONVERSATION = Font.createFont(Font.TRUETYPE_FONT,new File("Resources/font/FVF-Fernando.ttf")).deriveFont(15f);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (FontFormatException e) {
                    throw new RuntimeException(e);
                }

            }

        }
    }

    public static class DefaultFont{

        public static final Font DEFAULT = new JLabel().getFont();
        public static final Font FVF_FERNANDO_CONVERSATION;
        static {
            try {
                FVF_FERNANDO_CONVERSATION = Font.createFont(Font.TRUETYPE_FONT,new File("Resources/font/FVF-Fernando.ttf")).deriveFont(15f);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (FontFormatException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
