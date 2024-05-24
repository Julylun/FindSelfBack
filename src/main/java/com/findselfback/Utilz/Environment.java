package com.findselfback.Utilz;

import com.findselfback.View.GamePlayPanel;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Environment {

    public static boolean canMoveHere(float x, float y, float width, float height, int[][] levelData){

        if(
                isSolid(x,y,levelData)
                || isSolid(x+width,y+height,levelData)
                || isSolid(x+width,y,levelData)
                || isSolid(x+width,y,levelData)
                || isSolid(x,y+height,levelData)
        ) return false;

        return true;
    }

    public static boolean isSolid(float x, float y, int[][] levelData){
        if(x >= GamePlayPanel.SCREEN_WIDTH || x <= 0 || y >= GamePlayPanel.SCREEN_HEIGHT || y <= 0){
            return true;
        }
        float columnIndex = x / GamePlayPanel.TILE_SIZE;
        float rowIndex = y / GamePlayPanel.TILE_SIZE;

        int tileValue = levelData[(int)columnIndex][(int)rowIndex];

        return tileValue != 11;
    }

    public static boolean isOnGround(Rectangle2D.Float hitBox, int[][] levelData){
        if(isSolid(hitBox.x, hitBox.y + hitBox.height +1, levelData) || isSolid(hitBox.x + hitBox.width + 1, hitBox.y, levelData))
            return true;
        return false;
    }

    public static float getEntityPosNearWall(Rectangle2D.Float hitBox, float xSpeed){
        if(xSpeed == 0) return 0;
        int originalTileXPos = (int)(hitBox.x / GamePlayPanel.TILE_SIZE);
        if(xSpeed > 0){
            int scalingTileXPos = originalTileXPos * GamePlayPanel.TILE_SIZE;
            int xOffset = (int)(GamePlayPanel.TILE_SIZE - hitBox.width);
            return scalingTileXPos + xOffset - 1;
        }
        else {
            return originalTileXPos * GamePlayPanel.TILE_SIZE;
        }
    }

    public static float getEntityYPostUnderWallOrAboveGround(Rectangle2D.Float hitBox, float airSpeed){
        int originalTileXPos = (int)(hitBox.y / GamePlayPanel.TILE_SIZE);
        if(airSpeed > 0){
            int scalingTileYPos = originalTileXPos * GamePlayPanel.TILE_SIZE;
            int yOffset = (int)(GamePlayPanel.TILE_SIZE - hitBox.height);
            return scalingTileYPos + yOffset - 1;
        } else {
            return originalTileXPos * GamePlayPanel.TILE_SIZE;
        }
    }
}
