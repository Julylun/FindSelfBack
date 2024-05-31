package com.findselfback.Entities;

import com.findselfback.GameState.Playing;
import com.findselfback.Utilz.Coordinate2D;
import com.findselfback.Utilz.Environment;
import com.findselfback.Utilz.SpriteSheet;
import com.findselfback.View.GamePlayPanel;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class Snow extends Entity{
    private SpriteSheet spriteSheet;
    public static final int ALIVE = 0;
    public static final int DEAD = 1;
    private boolean isRenew = false;
    private final float acceleration = 0.005f;
    private boolean isCanBeDead;
    private Playing thisPlaying;
    public Snow(Playing playing){
        thisPlaying = playing;
        spriteSheet = new SpriteSheet("src/main/resources/assets/snow.png", GamePlayPanel.ORIGINAL_TILE_SIZE,GamePlayPanel.ORIGINAL_TILE_SIZE);
        hitBox = new Rectangle2D.Float();
        hitBox.x = 13;
        hitBox.width = 6;
        hitBox.y = 26;
        hitBox.height = 6;
        Vector<Coordinate2D> vector = new Vector<Coordinate2D>();
        for(int index = 0; index < 6; index++)
        vector.add(new Coordinate2D(index*32,0));
        spriteSheet.createSprite(ALIVE,vector);

        vector = new Vector<Coordinate2D>();
        for(int index = 0; index < 6; index++){
            vector.add(new Coordinate2D(index*32, 32));
        }
        spriteSheet.createSprite(DEAD,vector);
        spriteSheet.setCurrentSprite(ALIVE);
        spriteSheet.setDelayTime(20);

        x = ThreadLocalRandom.current().nextInt(-100,GamePlayPanel.SCREEN_WIDTH + 100);
        y = ThreadLocalRandom.current().nextInt(3,GamePlayPanel.SCREEN_HEIGHT/5);
        isCanBeDead = ThreadLocalRandom.current().nextInt(0,3) != 3;
        speed = (float) ThreadLocalRandom.current().nextDouble(0.5,1);
    }

    public void updateHitbox(){
        hitBox.x = x + 13;
        hitBox.y = y + 26;
    }

    public void renew(){

        spriteSheet.setCurrentSprite(ALIVE);
        x = ThreadLocalRandom.current().nextInt(thisPlaying.getxLevelOffset()-200,GamePlayPanel.SCREEN_WIDTH + 200 + thisPlaying.getxLevelOffset());
        y = ThreadLocalRandom.current().nextInt(3,GamePlayPanel.SCREEN_HEIGHT/5);
        updateHitbox();
        isCanBeDead = ThreadLocalRandom.current().nextInt(0,3) != 2;
        speed = (float) ThreadLocalRandom.current().nextDouble(0.5,1);
    }
    @Override
    public void update() {
        if(spriteSheet.getCurrentSpriteKey() == ALIVE){
            updateHitbox();
            speed += acceleration;
            if(y > GamePlayPanel.SCREEN_HEIGHT) {renew(); return;}
            if(!isCanBeDead || Environment.canMoveHere(hitBox.x-thisPlaying.windSpeed,hitBox.y+speed,hitBox.width,hitBox.height,thisPlaying.getMapManager().getLevel().getLevelData())){
                spriteSheet.nextFrame(false);
                y += speed;
                x -=thisPlaying.windSpeed;
            } else {
                y += (int) Environment.getEntityYPostUnderWallOrAboveGround(new Rectangle2D.Float(hitBox.x-thisPlaying.windSpeed,hitBox.y+speed,hitBox.width,hitBox.height),
                        speed)-hitBox.y;
//                x -= acceleration;
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
        if(thisPlaying.getGamePlayPanel().isDebugging()){
            g.setColor(Color.green);
            g.drawRect((int) x - xOffset, (int) y,GamePlayPanel.ORIGINAL_TILE_SIZE,GamePlayPanel.ORIGINAL_TILE_SIZE);
            g.setColor(Color.pink);
            g.drawRect((int) hitBox.x - xOffset, (int) hitBox.y, (int) hitBox.width, (int) hitBox.height);
        }
    }
}
