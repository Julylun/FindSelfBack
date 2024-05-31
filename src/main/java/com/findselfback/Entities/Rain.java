package com.findselfback.Entities;

import com.findselfback.GameState.Playing;
import com.findselfback.Utilz.Coordinate2D;
import com.findselfback.Utilz.Environment;
import com.findselfback.Utilz.SpriteSheet;
import com.findselfback.View.GamePlayPanel;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class Rain extends Entity{
    private SpriteSheet spriteSheet;
    public static final int ALIVE = 0;
    public static final int DEAD = 1;
    private boolean isRenew = false;
    private final float acceleration = 0.01f;
    private boolean isCanBeDead;
    private Playing thisPlaying;
    public Rain(Playing playing){
        thisPlaying = playing;
        spriteSheet = new SpriteSheet("src/main/resources/assets/rain.png", GamePlayPanel.ORIGINAL_TILE_SIZE,GamePlayPanel.ORIGINAL_TILE_SIZE);
        Vector<Coordinate2D> vector = new Vector<Coordinate2D>();
        vector.add(new Coordinate2D(0,0));
        spriteSheet.createSprite(ALIVE,vector);

        vector = new Vector<Coordinate2D>();
        for(int index = 0; index < 6; index++){
            vector.add(new Coordinate2D(index*32, 32));
        }
        spriteSheet.createSprite(DEAD,vector);
        spriteSheet.setCurrentSprite(ALIVE);
        spriteSheet.setDelayTime(10);

        x = ThreadLocalRandom.current().nextInt(-100,GamePlayPanel.SCREEN_WIDTH + 100);
        y = ThreadLocalRandom.current().nextInt(3,GamePlayPanel.SCREEN_HEIGHT/5);
        isCanBeDead = ThreadLocalRandom.current().nextInt(0,3) != 3;
        speed = ThreadLocalRandom.current().nextInt(3,5);
    }

    public void renew(){
        spriteSheet.setCurrentSprite(ALIVE);
        x = ThreadLocalRandom.current().nextInt(thisPlaying.getxLevelOffset()-200,GamePlayPanel.SCREEN_WIDTH + 200 + thisPlaying.getxLevelOffset());
        y = ThreadLocalRandom.current().nextInt(3,GamePlayPanel.SCREEN_HEIGHT/5);
        isCanBeDead = ThreadLocalRandom.current().nextInt(0,3) != 2;
        speed = ThreadLocalRandom.current().nextInt(3,5);
    }
    @Override
    public void update() {
        if(spriteSheet.getCurrentSpriteKey() == ALIVE){
            speed += acceleration;
            if(y > GamePlayPanel.SCREEN_HEIGHT) {renew(); return;}
            if(!isCanBeDead || Environment.canMoveHere(x,y,GamePlayPanel.ORIGINAL_TILE_SIZE/3,GamePlayPanel.ORIGINAL_TILE_SIZE,thisPlaying.getMapManager().getLevel().getLevelData())){
                y += speed;
            } else {
                y = (int) Environment.getEntityYPostUnderWallOrAboveGround(new Rectangle2D.Float(x,y,GamePlayPanel.ORIGINAL_TILE_SIZE,GamePlayPanel.ORIGINAL_TILE_SIZE),
                        speed);
                spriteSheet.setCurrentSprite(DEAD);
            }
        } else {
            if(!isRenew){
                if(spriteSheet.getFrameIndex() >= 5){
                    isRenew = true;
                    spriteSheet.nextFrame(false);
                } else {
                    spriteSheet.nextFrame(false);
                }
            } else {
                isRenew = false;
                renew();
            }

        }

    }

    @Override
    public void eventHandle() {
    }

    @Override
    public void paint(Graphics g, int xOffset) {
        g.drawImage(spriteSheet.getCurrentSprite(),(int)x - xOffset,(int)y,GamePlayPanel.ORIGINAL_TILE_SIZE,GamePlayPanel.ORIGINAL_TILE_SIZE,null);
    }
}
