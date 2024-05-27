package com.findselfback.Level;

import lombok.Data;

@Data
public class Level {
    int levelData[][];
    public Level(){
    }
    public int getSpriteMapIndex(int row, int column){
        return levelData[row][column];
    }
}
