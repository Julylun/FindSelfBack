package com.findselfback.Entities;

import com.findselfback.GameState.Playing;
import com.findselfback.Utilz.Environment;
import com.findselfback.Utilz.SpriteSheet;
import com.findselfback.View.GamePlayPanel;

public abstract class Animal extends Entity{
    protected float nextX, nextY;
    protected boolean isMoveLeft, isMoveRight, isMoving, isInAir = false;
    protected float airSpeed = 0;
    protected float fallingSpeedAfterCollision = (float) (0.5f * GamePlayPanel.SCALE);
    protected void resetInAir(){
        isInAir = false;
        airSpeed = 0;
    }
    protected void updateXPos(float xSpeed){
        if(Environment.canMoveHere(x+xSpeed,y, hitBox.width, hitBox.height,
                thisPlaying.getMapManager().getLevel().getLevelData())
        ){
            x+= xSpeed;
        } else {
            nextX = -1;
        }
    }
    protected abstract void updateHitBox();
    protected SpriteSheet skin;
    protected Playing thisPlaying;
}
