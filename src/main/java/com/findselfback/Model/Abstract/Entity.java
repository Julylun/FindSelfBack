package com.findselfback.Model.Abstract;


import com.findselfback.Control.KeyHandle;
import lombok.Data;

import java.awt.*;

@Data
public abstract class Entity {
    public int x,y;
    public int speed;


    public abstract void update();
    public abstract void eventHandle();
    public abstract void paint(Graphics g);
}
