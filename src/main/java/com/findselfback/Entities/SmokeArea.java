package com.findselfback.Entities;

import com.findselfback.GameState.Playing;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class SmokeArea extends Entity{
    private Smoke[] smokes;
    private Playing thisPlaying;
    public SmokeArea(Playing playing, int smokeNumber, float x, float y, float width, float height){
        thisPlaying = playing;
        smokes = new Smoke[smokeNumber];
        hitBox = new Rectangle2D.Float();
        hitBox.x = x;
        hitBox.y = y;
        hitBox.height = height;
        hitBox.width = width;
        for(int i = 0; i < smokeNumber; i++){
            smokes[i] = new Smoke(thisPlaying,hitBox);
        }
    }
    @Override
    public void update() {
        for(int i = 0; i < smokes.length; i++){
            smokes[i].update();
        }
    }

    @Override
    public void eventHandle() {

    }

    @Override
    public void paint(Graphics g, int xOffset) {
        for(int i = 0; i < smokes.length; i++){
            smokes[i].paint(g,xOffset);
        }

        if(thisPlaying.getGamePlayPanel().isDebugging()){
            g.setColor(Color.RED);
            g.drawRect((int) (hitBox.x - xOffset), (int) hitBox.y, (int) hitBox.width, (int) hitBox.height);
        }
    }
}
