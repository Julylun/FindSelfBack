package com.findselfback.Control;

import com.findselfback.Model.PrintColor;
import lombok.Data;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;


@Data
/**
 * KeyHandle is used to detect player's typing key
 */
public class KeyHandle implements KeyListener {
    public boolean upPressed, rightPressed, downPressed, leftPressed;
    private int lastPressed;
    public KeyHandle(){
        upPressed = false;
        rightPressed = false;
        downPressed = false;
        leftPressed = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //When player press key -> set true -> means that key is being press
        int eventCode = e.getKeyCode();
        PrintColor.debug(PrintColor.GREEN_BACKGROUND,"KeyHandle","keyPressed","W is pressed");
        if(eventCode == KeyEvent.VK_W){ //W button
            PrintColor.debug(PrintColor.GREEN_BACKGROUND,"KeyHandle","keyPressed","W is pressed");
            upPressed = true;
        }
        if(eventCode == KeyEvent.VK_D){ //D button
            PrintColor.debug(PrintColor.GREEN_BACKGROUND,"KeyHandle","keyPressed","D is pressed");
            rightPressed = true;
        }
        if(eventCode == KeyEvent.VK_S){ //S button
            PrintColor.debug(PrintColor.GREEN_BACKGROUND,"KeyHandle","keyPressed","S is pressed");
            downPressed = true;
        }if(eventCode == KeyEvent.VK_A){ //A button
            PrintColor.debug(PrintColor.GREEN_BACKGROUND,"KeyHandle","keyPressed","A is pressed");
            leftPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //When player release key -> set false -> means that key is released
        int eventCode = e.getKeyCode();
        if(eventCode == KeyEvent.VK_W){
            upPressed = false;
        }
        if(eventCode == KeyEvent.VK_D){
            rightPressed = false;
        }
        if(eventCode == KeyEvent.VK_S){
            downPressed = false;
        }if(eventCode == KeyEvent.VK_A){
            leftPressed = false;
        }
        lastPressed = eventCode;
    }
    public boolean isNoPressed(){
        return (upPressed || rightPressed || downPressed || leftPressed) ? false : true;
    }
}
