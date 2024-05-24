package com.findselfback.Entities;


import lombok.Data;

import java.awt.*;
import java.awt.geom.Rectangle2D;

@Data
public abstract class Entity {
    protected float x,y;
    protected float speed;
    protected Rectangle2D.Float hitBox;


    public abstract void update();
    public abstract void eventHandle();
    public abstract void paint(Graphics g);
}
