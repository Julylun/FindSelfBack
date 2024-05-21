package com.findselfback.Model;

import com.findselfback.Control.KeyHandle;
import com.findselfback.Model.Abstract.Entity;
import com.findselfback.Model.Animation.SpriteSheet;
import com.findselfback.Model.Graphic.Coordinate2D;
import com.findselfback.View.GamePlayPanel;
import lombok.Data;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Vector;

import static com.findselfback.Model.Constant.Animation.*;

@Data
public class Player extends Entity {
    private KeyHandle thisKeyHandle;
    private GamePlayPanel thisGamePlayPanel;
    private SpriteSheet sprite;
    public Player(GamePlayPanel gamePlayPanel, String spritePath, KeyHandle keyHandle){
        thisGamePlayPanel = gamePlayPanel;
        thisKeyHandle = keyHandle;
        this.sprite = new SpriteSheet(spritePath, 128, 128);


        init();
    }

    private void init(){
        //Set location and speed
        this.x = 200;
        this.y = 200;
        this.speed = 4;

        //Set sprite
        //--Running animation
        Vector<Coordinate2D> coordinate2DVector = new Vector<>();
        for(int i = 0; i < 8; i++)
        coordinate2DVector.add(new Coordinate2D(i*128,129));
        sprite.createSprite(RUNNING,coordinate2DVector);
        //--Idle animation
        coordinate2DVector = new Vector<>();
        for(int i = 0; i < 8; i++)
            coordinate2DVector.add(new Coordinate2D(i*128,0));
        sprite.createSprite(IDLE,coordinate2DVector);
        sprite.setCurrentSprite(IDLE);

        sprite.setDelayTime(5);
    }


    @Override
    public void eventHandle() {

    }
    @Override
    public void update() {
        if(!thisKeyHandle.isNoPressed()){
            if(thisKeyHandle.upPressed){
                y-= speed;
            }
            if(thisKeyHandle.downPressed){
                y+= speed;
            }
            if(thisKeyHandle.leftPressed){
//                thisKeyHandle.setLastPressed(KeyEvent.VK_A);
                sprite.nextFrame(true);
                sprite.setCurrentSprite(RUNNING);
                x-= speed;
            }
            if(thisKeyHandle.rightPressed){
//                thisKeyHandle.setLastPressed(KeyEvent.VK_D);
                sprite.setCurrentSprite(RUNNING);
                sprite.nextFrame(false);
                x+= speed;
            }
        } else{
            sprite.setCurrentSprite(IDLE);
            if(thisKeyHandle.getLastPressed() == KeyEvent.VK_A){
                PrintColor.debug(PrintColor.CYAN_BRIGHT,"Player","update","last key = " + thisKeyHandle.getLastPressed());
                sprite.nextFrame(true);
            }
            else {
                PrintColor.debug(PrintColor.RED_BRIGHT,"Player","update","last key = " + thisKeyHandle.getLastPressed());
                sprite.nextFrame(false);
            }
        }

    }

    @Override
    public void paint(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g.create();
        graphics2D.drawImage(sprite.getCurrentSprite(),x,y,sprite.getTileWidth(),sprite.getTileHeight(),null);
    }


}
