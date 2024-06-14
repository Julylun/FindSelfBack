package com.findselfback.Entities;

import com.findselfback.GameState.Playing;
import com.findselfback.Utilz.Constant;
import com.findselfback.Utilz.SpriteSheet;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.ThreadLocalRandom;

public class Smoke extends Entity{
    private SpriteSheet spriteSheet;
    private float flySpeed, increaseSizeSpeed, maxSize;
    private float drawWidth, drawHeight;
    private Playing playing;
    public Smoke(Playing playing, Rectangle2D.Float hitBox){
        this.playing = playing;
        this.hitBox = hitBox;
        spriteSheet = new SpriteSheet(Constant.AssetPath.FX.SMOKE,32,32);
        spriteSheet.createSprite(Constant.Animation.FX.RUNNING,0,0,32,32,8);
        spriteSheet.setCurrentSprite(Constant.Animation.FX.RUNNING);

        renew();
    }

    public void renew(){
        spriteSheet.setCurrentSprite(Constant.Animation.FX.RUNNING);
        spriteSheet.setFrameIndex(0);
        flySpeed = (float) ThreadLocalRandom.current().nextDouble(0,0.8);

        spriteSheet.setDelayTime(ThreadLocalRandom.current().nextInt(20,70));
//        spriteSheet.setDelayTime(35);

        drawWidth = (float) ThreadLocalRandom.current().nextDouble(16,32);
        increaseSizeSpeed = (float) ThreadLocalRandom.current().nextDouble(1,5);
        maxSize = (float) ThreadLocalRandom.current().nextDouble(32,64);
        drawHeight = drawWidth;
        x = (float) ThreadLocalRandom.current().nextDouble(hitBox.x,hitBox.x+hitBox.width);
        y = (float) ThreadLocalRandom.current().nextDouble(hitBox.y,hitBox.y+hitBox.height);
    }
    @Override
    public void update() {
        if(spriteSheet.getFrameIndex() >= 3){
            renew();
            return;
        }
        if(drawWidth < maxSize){
            drawWidth+=increaseSizeSpeed;
            drawHeight = drawWidth;
        }
        x-= playing.windSpeed;
        y-=flySpeed;
        spriteSheet.nextFrame(false);
    }

    @Override
    public void eventHandle() {

    }

    @Override
    public void paint(Graphics g, int xOffset) {
        g.drawImage(spriteSheet.getCurrentSprite(), (int) (x - xOffset), (int) y, (int) drawWidth, (int) drawHeight,null);

        if(playing.getGamePlayPanel().isDebugging()){
            g.drawRect((int) (x-xOffset), (int) y, (int) drawWidth, (int) drawHeight);
        }
    }
}
