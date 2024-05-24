package com.findselfback.Entities;

import com.findselfback.Control.InputHandle;
import com.findselfback.GameState.Playing;
import com.findselfback.Utilz.SpriteSheet;
import com.findselfback.Utilz.Coordinate2D;
import com.findselfback.Utilz.Environment;
import com.findselfback.View.GamePlayPanel;
import lombok.Data;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import static com.findselfback.Utilz.Constant.Animation.*;

@Data
public class Player extends Entity {
    private InputHandle thisInputHandle;
    private Playing thisPlaying;
    private SpriteSheet sprite;
    private boolean isMoving, isInAir = false;

    private boolean moveLeft = false, moveRight = false, moveTop = false, moveBot = false;
    private boolean isFlip = false;
    private int direction;
    private float gravity = (float)(0.04f * GamePlayPanel.SCALE);
    private float airSpeed = 0;
    private float jumpSpeed = (float)(-2.3f * GamePlayPanel.SCALE);
    private float fallingSpeedAfterCollision = (float) (0.5f * GamePlayPanel.SCALE);



    public Player(Playing playing, String spritePath, InputHandle inputHandle){
        thisPlaying = playing;
        thisInputHandle = inputHandle;
        init();

        this.sprite = new SpriteSheet(spritePath, GamePlayPanel.ORIGINAL_TILE_SIZE, GamePlayPanel.ORIGINAL_TILE_SIZE);
        loadAnimation();


    }
    private void init(){
        hitBox = new Rectangle2D.Float();

        //Set location and speed
        this.x = 200;
        this.y = 200;
        hitBox.x = (int)this.x;
        hitBox.y = (int)this.y;
        hitBox.width = GamePlayPanel.TILE_SIZE*2/3;
        hitBox.height = GamePlayPanel.TILE_SIZE;
        this.speed = 1.5f; //map editor
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
                thisPlaying.getMapManager().getLevel().getLevelData())
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

        if(!isInAir && !Environment.isOnGround(hitBox,thisPlaying.getMapManager().getLevel().getLevelData())){
            isInAir = true;
        }

        if(!thisPlaying.isNoPressed()){
            if(thisPlaying.upPressed && !thisPlaying.downPressed){
                jump();
            }
            if(!thisPlaying.upPressed && thisPlaying.downPressed){
                ySpeed = speed;
            }
            if(thisPlaying.leftPressed && !thisPlaying.rightPressed){
                xSpeed = -speed;
                isFlip = true;
                direction = 3;
            }
            if(thisPlaying.rightPressed && !thisPlaying.leftPressed){
                xSpeed = speed;
                isFlip = false;
                direction = 1;
            }
        }
        if(isInAir){
            if(Environment.canMoveHere(x,y+airSpeed,hitBox.width,hitBox.height,thisPlaying.getMapManager().getLevel().getLevelData())){
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
                thisPlaying.getMapManager().getLevel().getLevelData())
        ){
            y+= ySpeed;
        }
        updateHitBox();

    }

    @Override
    public void paint(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g.create();
//                PrintColor.debug(PrintColor.CYAN_BRIGHT, "Player", "update", "x: " + x + " | y: " + y);

        if (!thisPlaying.isNoPressed()) {
            if (thisPlaying.leftPressed && !thisPlaying.rightPressed) {
                sprite.nextFrame(false);
                sprite.setCurrentSprite(RUNNING);
            }
            if (thisPlaying.rightPressed && !thisPlaying.leftPressed) {
                sprite.setCurrentSprite(RUNNING);
                sprite.nextFrame(false);
            }
            if(thisPlaying.leftPressed && thisPlaying.rightPressed){
                sprite.setCurrentSprite(IDLE);
                sprite.nextFrame(thisInputHandle.getLastPressed() == KeyEvent.VK_D);
            }
        } else {
            sprite.setCurrentSprite(IDLE);
            if (thisPlaying.lastPressed == KeyEvent.VK_A) {
                isFlip = true;
                sprite.nextFrame(false);
            } else {
                isFlip = false;
                sprite.nextFrame(false);
            }
        }

        graphics2D.drawImage(sprite.getCurrentSprite(), ((isFlip) ?(int)(x + GamePlayPanel.TILE_SIZE):(int)x), (int)y, (int)(sprite.getTileWidth()*GamePlayPanel.SCALE)*((isFlip) ? -1 : 1), (int)(sprite.getTileHeight()*GamePlayPanel.SCALE), null);
        drawHitBox(g,thisPlaying.getGamePlayPanel().isDebugging());
    }

    public String getStringLocation(){
        return "x = " + x + " | y = " + y;
    }
}
