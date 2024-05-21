package com.findselfback.Model.Graphic;

import lombok.Data;

@Data
public class Coordinate2D {
    private int x,y;
    public Coordinate2D(){

    }
    public Coordinate2D(int x, int y){
        this.x = x;
        this.y = y;
    }
}
