package com.findselfback.Level;

import lombok.Data;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;




@Data
public class Level {
    LayerTile[][] levelData;
    private int col = 0;
    private int row = 0;
    public Level(){
    }

    public LayerTile[][] getLevelData() {
        return levelData;
    }

    public LayerTile getSpriteMapIndex(int row, int column){
        return levelData[row][column];
    }
}
