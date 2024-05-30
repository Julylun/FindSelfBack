package com.findselfback.Utilz;

import com.findselfback.Level.LayerTile;
import com.findselfback.Level.Tile;
import com.findselfback.View.GamePlayPanel;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.TreeSet;

public class Environment {

    public static boolean canMoveHere(float x, float y, float width, float height, LayerTile[][] levelData){

        if(
                isSolid(x,y,levelData)
                || isSolid(x+width,y+height,levelData)
                || isSolid(x+width,y,levelData)
                || isSolid(x+width,y,levelData)
                || isSolid(x,y+height,levelData)
        ) return false;

        return true;
    }

    /**
     * Đưa vào một vị trí (x,y) và một mảng gồm các phần tử tile map, xác định xem
     * có thể di chuyển vào đó không
     * @param x
     * @param y
     * @param levelData
     * @return
     */
    public static boolean isSolid(float x, float y, LayerTile[][] levelData){
        int maxWidth = levelData[0].length * GamePlayPanel.TILE_SIZE;
        if(x >= maxWidth || x <= 0 || y >= GamePlayPanel.SCREEN_HEIGHT || y <= 0){
            return true;
        }
        float columnIndex = x / GamePlayPanel.TILE_SIZE;
        float rowIndex = y / GamePlayPanel.TILE_SIZE;

        TreeSet<Tile> tileSet = levelData[(int)rowIndex][(int)columnIndex].getTileTreeSet();
        for(Tile tile: tileSet){
//            System.out.println(tile.value);
            if((tile.value <= 47 && tile.value != 11)) return true;
        }
        return false;
    }

    public static boolean isOnGround(Rectangle2D.Float hitBox, LayerTile[][] levelData){
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
