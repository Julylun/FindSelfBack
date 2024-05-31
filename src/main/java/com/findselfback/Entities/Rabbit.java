package com.findselfback.Entities;

import com.findselfback.Control.InputHandle;
import com.findselfback.GameState.GameState;
import com.findselfback.GameState.Playing;
import com.findselfback.Utilz.Constant;
import com.findselfback.Utilz.Coordinate2D;
import com.findselfback.Utilz.Environment;
import com.findselfback.Utilz.SpriteSheet;
import com.findselfback.View.GamePlayPanel;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class Rabbit extends Animal{
    private float speedDebugging = 0;
    private int movingDelay = 0;
    private int currentTick = 0;
    private boolean isFlip;
    public Rabbit(Playing playing, String spritePath){
        skin = new SpriteSheet(spritePath, GamePlayPanel.ORIGINAL_TILE_SIZE, GamePlayPanel.ORIGINAL_TILE_SIZE);
        thisPlaying = playing;
        init();
    }
    private void init(){
        x = ThreadLocalRandom.current().nextInt(0,
                thisPlaying.getMapManager().getLevel().getLevelData()[0].length*GamePlayPanel.TILE_SIZE
        );
        y = 30;
        hitBox = new Rectangle2D.Float();
        hitBox.x = 7;
        hitBox.width = 18;
        hitBox.y = 4;
        hitBox.height = 23;
        nextX = x;
        nextY = y;
        speed = 0.5f;
        loadAnimation();
        skin.setCurrentSprite(Constant.Animation.IDLE);
    }

    private void loadAnimation() {
        Vector<Coordinate2D> animationSet = new Vector<Coordinate2D>();
        for(int positionIndex = 0; positionIndex < 12; positionIndex++)
            animationSet.add(new Coordinate2D(positionIndex*32, 0));
        skin.createSprite(Constant.Animation.IDLE,animationSet);

        animationSet = new Vector<Coordinate2D>();
        for(int positionIndex = 0; positionIndex < 5; positionIndex++)
            animationSet.add(new Coordinate2D(positionIndex*32, 32));
        skin.createSprite(Constant.Animation.RUNNING,animationSet);

        animationSet = new Vector<Coordinate2D>();
        for(int positionIndex = 0; positionIndex < 10; positionIndex++)
            animationSet.add(new Coordinate2D(positionIndex*32, 64));
        skin.createSprite(Constant.Animation.SITTING,animationSet);

        skin.setDelayTime(40);
    }
    @Override
    public void update() {
        float xSpeed = 0, ySpeed = 0;
        if(currentTick >= movingDelay) {
            if ((int) x == (int) nextX) {
                if (ThreadLocalRandom.current().nextBoolean()) {
                    nextX = (float) ThreadLocalRandom.current().nextDouble(x - 100, x + 100);
                } else {
                    nextX = x;
                    movingDelay = ThreadLocalRandom.current().nextInt(100, 800);
                    currentTick = 0;
                    int randomSprite = ThreadLocalRandom.current().nextInt(1,3);
                    switch (randomSprite){
                        case 1:{
                            skin.setCurrentSprite(Constant.Animation.IDLE);
                            break;
                        }
                        case 2:{
                            skin.setCurrentSprite(Constant.Animation.SITTING);
                            break;
                        }
                    }
                }
            }
        } else {
            currentTick++;
        }

        if(nextX == -1.0) {
            nextX = (float) ThreadLocalRandom.current().nextDouble(x - 40, x + 40);

        }
        if(!Environment.canMoveHere(hitBox.x+10,y-1,hitBox.width-10,hitBox.height,thisPlaying.getMapManager().getLevel().getLevelData())) {
            x += Environment.getEntityPosNearWall(hitBox,xSpeed);
        }

        if(x > nextX){
            xSpeed = -speed;
            skin.setCurrentSprite(Constant.Animation.RUNNING);
//            isMoving = true;
            isFlip = true;
        } else if(x < nextX) {
            xSpeed = speed;
            skin.setCurrentSprite(Constant.Animation.RUNNING);
//            isMoving = true;
            isFlip = false;
        }
        if(!isInAir && !Environment.isOnGround(hitBox,thisPlaying.getMapManager().getLevel().getLevelData())){
            isInAir = true;
        } else {
            isInAir = false;
        }

        if(isInAir){
            if(Environment.canMoveHere(hitBox.x,y+airSpeed,hitBox.width,hitBox.height,thisPlaying.getMapManager().getLevel().getLevelData())){
                y += airSpeed;
                airSpeed += Constant.EnvironmentValue.gravity;
                updateXPos(xSpeed);

            }
            else {
                hitBox.y = Environment.getEntityYPostUnderWallOrAboveGround(hitBox,airSpeed);
                if(airSpeed > 0) resetInAir();
                else {
                    airSpeed = fallingSpeedAfterCollision;
                    updateXPos(xSpeed);
                }
            }
        } else{
            updateXPos(xSpeed);
        }


        updateHitBox();
        speedDebugging = xSpeed;
        skin.nextFrame(false);
    }

    @Override
    public void eventHandle() {

    }

    @Override
    public void paint(Graphics g, int xOffset) {
        g.drawImage(skin.getCurrentSprite(), (int) x - xOffset + ((isFlip) ? GamePlayPanel.ORIGINAL_TILE_SIZE : 0), (int) y,GamePlayPanel.ORIGINAL_TILE_SIZE * ((isFlip)? -1 : 1),GamePlayPanel.ORIGINAL_TILE_SIZE,null);
        if(thisPlaying.getGamePlayPanel().isDebugging()){
            g.setColor(Color.BLUE);
            g.drawString("Is in air: " + isInAir, (int) (x-xOffset), (int) y-30);
            g.drawString("Current pos: " + x + ":" + y, (int) (x-xOffset), (int) y-20);
            g.drawString("Next pos " + nextX + ":" + nextY, (int) (x-xOffset), (int) y-10);
            g.drawString("XSpeed: " + speedDebugging, (int) (x-xOffset), (int) y);

            g.setColor(Color.PINK);
            g.drawRect((int) hitBox.x - xOffset, (int) hitBox.y, (int) hitBox.width, (int) hitBox.height);
            g.setColor(Color.green);
            g.drawRect((int) x - xOffset, (int) y,GamePlayPanel.ORIGINAL_TILE_SIZE,GamePlayPanel.ORIGINAL_TILE_SIZE);
            g.setColor(Color.RED);
            g.drawLine((int) (hitBox.x+ hitBox.width/2)-xOffset, (int) (hitBox.y + hitBox.height/2), (int) (nextX+ hitBox.width/2)-xOffset, (int)  (hitBox.y + hitBox.height/2));
        }
    }

    @Override
    protected void updateHitBox() {
        hitBox.x = x + 7;
        hitBox.y = y + 4;
    }
}
