package com.findselfback.Level;

import com.findselfback.Entities.Entity;
import com.findselfback.Entities.Player;
import com.findselfback.GameState.Playing;
import com.findselfback.Utilz.Constant;
import com.findselfback.Utilz.Conversation;
import com.findselfback.Utilz.Coordinate2D;
import com.findselfback.View.GamePlayPanel;
import lombok.Data;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.security.PrivateKey;
import java.util.Vector;

@Data
public class EventManager extends Entity {
    public static final int
    TRASH_TASK = 1;
    private Playing thisPlaying;
    private Coordinate2D[] eventCoordinateS;
    private String taskString;
    private boolean isDisplayTask;
    public boolean[] isDisableS;
    public EventManager(Playing playing){
        thisPlaying = playing;
        eventCoordinateS =  new Coordinate2D[]{
                new Coordinate2D(348,0), //Trash can event
                new Coordinate2D(704,0), //Remove Trash can event
                new Coordinate2D(1400,0), //Talk to police event
                new Coordinate2D(1800,0),
                new Coordinate2D(1750,0), //Stop
                new Coordinate2D(1945,0) //Run and jump
        };
        isDisableS = new boolean[]{false,false,false,false,true,false};
    }
    @Override
    public void update() {
        Rectangle2D playerHitBox = thisPlaying.getPlayer().getHitBox();
        if(eventCoordinateS[0].getX() < playerHitBox.getX() && !isDisableS[0]){
            setSubtitleEvent(Conversation.Begin.TRASH_EVENT,
                    true,true,
                    Conversation.Task.TRASH_CAN_CLIMBING_TASK,
                    0);
        }
        if(eventCoordinateS[1].getX() < playerHitBox.getX() && !isDisableS[1]){
            removeTask(1);
        }
        if(eventCoordinateS[2].getX() < playerHitBox.getX() && !isDisableS[2]){
            setSubtitleEvent(Conversation.Begin.POLICE_TALKING_EVENT,
                    true, true,
                    Conversation.Task.TALK_TO_THE_POLICE,2);
        }
        if(eventCoordinateS[3].getX() < playerHitBox.getX() && !isDisableS[3]){
            setSubtitle(Conversation.Begin.RETURN_THE_POLICE);
            stopPlayerMoving();
            thisPlaying.leftPressed = true;
            isDisableS[4] = false;
        }
        if(eventCoordinateS[4].getX() > playerHitBox.getX() && !isDisableS[4]){
            stopPlayerMoving();
            isDisableS[4] = true;
        }
        if(eventCoordinateS[5].getX() < playerHitBox.getX() && !isDisableS[5]){
            stopPlayerMoving();
            setSubtitleEvent(Conversation.Begin.CANT_WALK_THROUGH_GLASS,
                    true,true,
                    Conversation.Task.JUMP_THROUGH_THE_GLASSES,
                    5);
        }
    }
    public void removeTask(int eventCode){
        if(eventCode != -1)
            isDisableS[eventCode] = true;
        isDisplayTask = false;
        taskString = "";
    }

    private void setSubtitle(String[] currentConversation){
        thisPlaying.getSubtitle().setCurrentConversation(currentConversation);
        thisPlaying.getSubtitle().setDisplay(true);
    }
    private void setSubtitleEvent(String[] currentConversation, boolean isDisplaySubtitle, boolean isHasTask, String task, int eventIndex){
        thisPlaying.getSubtitle().setCurrentConversation(currentConversation);
        thisPlaying.getSubtitle().setDisplay(isDisplaySubtitle);
        thisPlaying.getSubtitle().setHasTask(isHasTask);
        taskString = task;
        isDisableS[eventIndex] = true;
        if(isDisplaySubtitle)
            stopPlayerMoving();
    }
    private void stopPlayerMoving(){
        Player player = thisPlaying.getPlayer();
        thisPlaying.setDownPressed(false);
        thisPlaying.setRightPressed(false);
        thisPlaying.setLeftPressed(false);
        thisPlaying.setUpPressed(false);
        thisPlaying.isNoPressed = true;
//        player.setSpeed(0);
    }

    @Override
    public void eventHandle() {

    }

    @Override
    public void paint(Graphics g, int xOffset) {
//        System.out.println(isDisplayTask);
        if(isDisplayTask){
//            System.out.println(currentConversation[currentSubtitleIndex]);
            Graphics2D g2d = (Graphics2D) g.create();
            AffineTransform transform = g2d.getTransform();

            g2d.setColor(Color.black);
            g.setFont(Constant.DefaultFont.FVF_FERNANDO_CONVERSATION);
            FontRenderContext frc = g2d.getFontRenderContext();
            TextLayout tl = new TextLayout("Task: " + taskString, g.getFont().deriveFont(11F), frc);
            transform.setToScale(1,1);
            transform.translate(10, 30);
            g2d.transform(transform);
            Shape shape = tl.getOutline(null);
            g2d.setStroke(new BasicStroke(4f));

            g2d.draw(shape);
            g2d.setColor(Color.YELLOW);
            g2d.fill(shape);
        }
    }
}
