package com.findselfback.Model;

import com.findselfback.Control.KeyHandle;
import com.findselfback.Model.Abstract.Entity;
import com.findselfback.Model.Animation.SpriteSheet;
import com.findselfback.Model.Graphic.Coordinate2D;
import com.findselfback.View.GamePlayPanel;
import lombok.Data;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
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
        this.sprite = new SpriteSheet(spritePath, thisGamePlayPanel.ORIGINAL_TILE_SIZE, thisGamePlayPanel.ORIGINAL_TILE_SIZE);

        init();
    }
    private void init(){
        //Set location and speed
        this.x = 200;
        this.y = 200;
        this.speed = 1f;

        loadAnimation();
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
                x-= speed;
            }
            if(thisKeyHandle.rightPressed){
                x+= speed;
            }
        }

    }

    @Override
    public void paint(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g.create();
//                PrintColor.debug(PrintColor.CYAN_BRIGHT, "Player", "update", "x: " + x + " | y: " + y);

        if (!thisKeyHandle.isNoPressed()) {
            if (thisKeyHandle.leftPressed) {
                sprite.nextFrame(true);
                sprite.setCurrentSprite(RUNNING);
            }
            if (thisKeyHandle.rightPressed) {
                sprite.setCurrentSprite(RUNNING);
                sprite.nextFrame(false);
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
    }
}
