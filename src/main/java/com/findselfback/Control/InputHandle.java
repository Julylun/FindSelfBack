package com.findselfback.Control;

import com.findselfback.GameState.GameState;
import com.findselfback.View.GamePlayPanel;
import lombok.Data;

import java.awt.event.*;


@Data
/**
 * InputHandle is used to detect player's typing key
 */
public class InputHandle implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
    public boolean upPressed, rightPressed, downPressed, leftPressed;
    private int lastPressed;
    private GamePlayPanel thisGamePlayPanel;
    private KeyEvent lastKeyEvent;
    private MouseEvent lastMouseEvent, lastClickEvent;
    private MouseWheelEvent lastMouseWheelEvent;
    public InputHandle(GamePlayPanel gamePlayPanel){
        thisGamePlayPanel = gamePlayPanel;
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
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            GameState.state = GameState.MENU;
        }
        if(e.getKeyCode() == KeyEvent.VK_F1){
            thisGamePlayPanel.setDebugging(!thisGamePlayPanel.isDebugging());
        }
        switch (GameState.state){
            case PLAYING:{
                if (!thisGamePlayPanel.getPlaying().getSubtitle().isDisplay()) {
                    thisGamePlayPanel.getPlaying().keyPressed(e);
                } else {
//                    thisGamePlayPanel.getPlaying().getSubtitle().nextSubtitle();
                }
            }

                break;
            case MENU:
                thisGamePlayPanel.getMenuState().keyPressed(e);
                break;
        }
        lastKeyEvent = e;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //When player release key -> set false -> means that key is released
        if(e.getKeyCode() == KeyEvent.VK_F11){
            thisGamePlayPanel.isMapEnable = !thisGamePlayPanel.isMapEnable;
        }
        if(e.getKeyCode() == KeyEvent.VK_F6){
            thisGamePlayPanel.isChooseTileEnable = !thisGamePlayPanel.isChooseTileEnable;
        }
        if(e.getKeyCode() == KeyEvent.VK_F2){
            thisGamePlayPanel.isSaving = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_DELETE){
            thisGamePlayPanel.deleteAllMap();
        }
        if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
            System.out.println("AAA");
            thisGamePlayPanel.deleteTileAtCursorPosition();
        }
        switch (GameState.state){
            case PLAYING:{
                if (!thisGamePlayPanel.getPlaying().getSubtitle().isDisplay()) {
                    thisGamePlayPanel.getPlaying().keyReleased(e);
                } else {
                    thisGamePlayPanel.getPlaying().getSubtitle().nextSubtitle();
                }

                break;
            }
            case MENU:
                thisGamePlayPanel.getMenuState().keyReleased(e);
                break;
        }
        lastKeyEvent = e;
    }
    public boolean isNoPressed(){
        return (upPressed || rightPressed || downPressed || leftPressed) ? false : true;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        thisGamePlayPanel.isPressing = true;
        switch (GameState.state){
            case PLAYING: {
                if (!thisGamePlayPanel.getPlaying().getSubtitle().isDisplay()) {
                    thisGamePlayPanel.getPlaying().mousePressed(e);
                }
                break;
            }
            case MENU:
                thisGamePlayPanel.getMenuState().mousePressed(e);
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        thisGamePlayPanel.isPressing = false;
            switch (GameState.state){
                case PLAYING: {
                    if (!thisGamePlayPanel.getPlaying().getSubtitle().isDisplay()) {
                        thisGamePlayPanel.getPlaying().mouseReleased(e);
                    } else {
                        thisGamePlayPanel.getPlaying().getSubtitle().nextSubtitle();
                    }
                }
                    break;
                case MENU:
                    thisGamePlayPanel.getMenuState().mouseReleased(e);
                    break;
            }
            lastClickEvent = e;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
        switch (GameState.state){
            case PLAYING:
                thisGamePlayPanel.getPlaying().mouseExited(e);
                break;
            case MENU:
                thisGamePlayPanel.getMenuState().mouseExited(e);
                break;
        }
    }


    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        lastMouseEvent = e;
        switch (GameState.state){
            case PLAYING:
                break;
            case MENU:
                thisGamePlayPanel.getMenuState().mouseMoved(e);
                break;
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
            if(e.getWheelRotation() < 0 && thisGamePlayPanel.currentTileEditingMode > -1){
                thisGamePlayPanel.currentTileEditingMode -= 1;
            } else {
                thisGamePlayPanel.currentTileEditingMode +=1;
                if(thisGamePlayPanel.currentTileEditingMode > 255) thisGamePlayPanel.currentTileEditingMode = -1;
            }

    }
}
