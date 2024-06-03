package com.findselfback.Entities;

import com.findselfback.GameState.Playing;
import com.findselfback.Utilz.Constant;
import com.findselfback.Utilz.SpriteSheet;
import com.findselfback.View.GamePlayPanel;
import lombok.Data;

import java.awt.*;

@Data
public class FX extends Entity{
    private SpriteSheet spriteSheet;
    private boolean isPainted = true;
    private float drawWidth, drawHeight;
    private Playing playing;
    public FX(Playing playing,String assetPath, float x, float y, float width, float height, int frameNumber,int delayTime){
        this.playing = playing;
        this.spriteSheet = new SpriteSheet(assetPath, (int) width, (int) height);
        this.spriteSheet.createSprite(Constant.Animation.FX.RUNNING,0,0, (int) width, (int) height,frameNumber);
        spriteSheet.setCurrentSprite(Constant.Animation.FX.RUNNING);
        this.spriteSheet.setDelayTime(delayTime);
        drawHeight = height;
        drawWidth = width;
        this.x = x;
        this.y = y;
    }
    @Override
    public void update() {

    }

    @Override
    public void eventHandle() {

    }

    @Override
    public void paint(Graphics g, int xOffset) {
        if(isPainted && (x - xOffset >= -drawWidth && x - xOffset - drawWidth <= GamePlayPanel.SCREEN_WIDTH)) {
            g.drawImage(spriteSheet.getCurrentSprite(), (int) x - xOffset, (int) y, (int) drawWidth, (int) drawHeight,null);
            spriteSheet.nextFrame(false);
        }


    }
}
