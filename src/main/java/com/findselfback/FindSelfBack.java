package com.findselfback;
import  com.findselfback.View.*;

import java.awt.*;


public class FindSelfBack {
    static {
        System.setProperty("sun.java2d.opengl", "true");
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                GameFrame gameFrame = new GameFrame();
            }
        });
    }
}