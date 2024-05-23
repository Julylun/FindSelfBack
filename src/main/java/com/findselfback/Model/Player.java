package com.findselfback.Model;

import com.findselfback.Control.KeyHandle;
import com.findselfback.Model.Abstract.Entity;
import com.findselfback.Model.Animation.SpriteSheet;
import com.findselfback.Model.Graphic.Coordinate2D;
import com.findselfback.Model.Math.Environment;
import com.findselfback.Model.Stage.Level;
import com.findselfback.View.GamePlayPanel;
import lombok.Data;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

import static com.findselfback.Model.Constant.Animation.*;


public class Player extends Entity {
    private KeyHandle thisKeyHandle;
    private GamePlayPanel thisGamePlayPanel;
    private SpriteSheet sprite;
    private boolean isMoving, isInAir = false;
    private int direction;
    private float gravity = (float)(0.04f * GamePlayPanel.SCALE);
    private float airSpeed = 0;
    private float jumpSpeed = (float)(-2.2f * GamePlayPanel.SCALE);
    private float fallingSpeedAfterCollision = (float) (0.5f * GamePlayPanel.SCALE);



    public Player(GamePlayPanel gamePlayPanel, String spritePath, KeyHandle keyHandle){
        thisGamePlayPanel = gamePlayPanel;
        thisKeyHandle = keyHandle;
        init();

        this.sprite = new SpriteSheet(spritePath, thisGamePlayPanel.ORIGINAL_TILE_SIZE, thisGamePlayPanel.ORIGINAL_TILE_SIZE);
        loadAnimation();


    }
    private void init(){
        hitBox = new Rectangle2D.Float();

        //Set location and speed
        this.x = 200;
        this.y = 200;
        hitBox.x = (int)this.x;
        hitBox.y = (int)this.y;
        hitBox.width = thisGamePlayPanel.TILE_SIZE*2/3;
        hitBox.height = thisGamePlayPanel.TILE_SIZE;
        this.speed = 1f;

    }

    private void drawHitBox(Graphics g,boolean isEnable){
        if(isEnable){
            g.setColor(Color.pink);
            g.drawRect((int)hitBox.x,(int)hitBox.y,(int)hitBox.width,(int)hitBox.height);
            g.setColor(Color.black);
        }
    }
    private void updateHitBox(){
        hitBox.x = (int)this.x;
        hitBox.y = (int)this.y;
        if(direction == 3){
            hitBox.x = (int)this.x + hitBox.width/3;
        }
    }

    private void updateXPos(float xSpeed){
        if(Environment.canMoveHere(x+xSpeed,y, hitBox.width, hitBox.height,
                thisGamePlayPanel.getMapManager().getLevel().getLevelData())
        ){
            x+= xSpeed;
        } else {
            hitBox.x = Environment.getEntityPosNearWall(hitBox,xSpeed);
        }
    }

    private void resetInAir(){
        isInAir = false;
        airSpeed = 0;
    }
    private void loadAnimation(){
        //Set sprite
        //--Running animation
        Vector<Coordinate2D> coordinate2DVector = new Vector<>();
        for(int i = 0; i < 13; i++)
            coordinate2DVector.add(new Coordinate2D(i*32,32));
        sprite.createSprite(RUNNING,coordinate2DVector);
        //--Idle animation
        coordinate2DVector = new Vector<>();
        for(int i = 0; i < 11; i++)
            coordinate2DVector.add(new Coordinate2D(i*32,0));
        sprite.createSprite(IDLE,coordinate2DVector);
        sprite.setCurrentSprite(IDLE);

        sprite.setDelayTime(2);
    }

    private void jump(){
        if(isInAir) return;
        airSpeed += jumpSpeed;
        isInAir = true;
    }


    @Override
    public void eventHandle() {

    }
    @Override
    public void update() {
        float xSpeed = 0, ySpeed = 0;

        if(!isInAir && !Environment.isOnGround(hitBox,thisGamePlayPanel.getMapManager().getLevel().getLevelData())){
            isInAir = true;
        }

        if(!thisKeyHandle.isNoPressed()){
            if(thisKeyHandle.upPressed && !thisKeyHandle.downPressed){
                jump();
            }
            if(!thisKeyHandle.upPressed && thisKeyHandle.downPressed){
                ySpeed = speed;
            }
            if(thisKeyHandle.leftPressed && !thisKeyHandle.rightPressed){
                xSpeed = -speed;
                direction = 3;
            }
            if(thisKeyHandle.rightPressed && !thisKeyHandle.leftPressed){
                xSpeed = speed;
                direction = 1;
            }
        }
        if(isInAir){
            if(Environment.canMoveHere(x,y+airSpeed,hitBox.width,hitBox.height,thisGamePlayPanel.getMapManager().getLevel().getLevelData())){
                y += airSpeed;
                airSpeed += gravity;
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



        if(Environment.canMoveHere(x,y+ySpeed, hitBox.width, hitBox.height,
                thisGamePlayPanel.getMapManager().getLevel().getLevelData())
        ){
            y+= ySpeed;
        }
        updateHitBox();

    }

    @Override
    public void paint(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g.create();
//                PrintColor.debug(PrintColor.CYAN_BRIGHT, "Player", "update", "x: " + x + " | y: " + y);

        if (!thisKeyHandle.isNoPressed()) {
            if (thisKeyHandle.leftPressed && !thisKeyHandle.rightPressed) {
                sprite.nextFrame(true);
                sprite.setCurrentSprite(RUNNING);
            }
            if (thisKeyHandle.rightPressed && !thisKeyHandle.leftPressed) {
                sprite.setCurrentSprite(RUNNING);
                sprite.nextFrame(false);
            }
            if(thisKeyHandle.leftPressed && thisKeyHandle.rightPressed){
                sprite.setCurrentSprite(IDLE);
                sprite.nextFrame(thisKeyHandle.getLastPressed() == KeyEvent.VK_D);
            }
        } else {
            sprite.setCurrentSprite(IDLE);
            if (thisKeyHandle.getLastPressed() == KeyEvent.VK_A) {
//                PrintColor.debug(PrintColor.CYAN_BRIGHT, "Player", "update", "last key = " + thisKeyHandle.getLastPressed());
                sprite.nextFrame(true);
            } else {
//                PrintColor.debug(PrintColor.RED_BRIGHT, "Player", "update", "last key = " + thisKeyHandle.getLastPressed());
                sprite.nextFrame(false);
            }
        }


        graphics2D.drawImage(sprite.getCurrentSprite(), (int)x, (int)y, (int)(sprite.getTileWidth()*thisGamePlayPanel.SCALE), (int)(sprite.getTileHeight()*thisGamePlayPanel.SCALE), null);
        drawHitBox(g,true);
    }
}
