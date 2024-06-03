package com.findselfback.Entities;

import com.findselfback.GameState.Playing;
import com.findselfback.Utilz.Constant;
import com.findselfback.View.GamePlayPanel;
import lombok.Data;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

import static com.findselfback.Utilz.Conversation.Begin.POLICE_CONVERSATION;
import static java.awt.SystemColor.text;

@Data
public class Subtitle extends Entity{
    private Playing thisPlaying;
    private boolean isDisplay;
    private boolean isHasTask;
    private String[] currentConversation;
    private int currentSubtitleIndex;
    public Subtitle(Playing playing){
        thisPlaying = playing;
        currentSubtitleIndex = 0;
        isDisplay = false;
        isHasTask = false;
    }
    public void nextSubtitle(){
        currentSubtitleIndex+=1;
        if(currentSubtitleIndex >= currentConversation.length){
            isDisplay = false;
            currentSubtitleIndex = 0;
            if(isHasTask){
                thisPlaying.getEventManager().setDisplayTask(true);
                isHasTask = false;
            }
        }
    }

    public void setEventSubtitleByNPC(int EventCode){
        switch (EventCode){
            case NPC.TRAFFIC_POLICE: {
                thisPlaying.getEventManager().removeTask(-1);
                setCurrentConversation(POLICE_CONVERSATION);
                setDisplay(true);
                setHasTask(true);
                thisPlaying.getEventManager().isDisableS[3] = true;
                thisPlaying.EButton = false;
                break;
            }
        }
    }

    @Override
    public void update() {

    }
    @Override
    public void paint(Graphics g, int xOffset) {
        if(isDisplay){
//            System.out.println(currentConversation[currentSubtitleIndex]);
            Graphics2D g2d = (Graphics2D) g.create();
            AffineTransform transform = g2d.getTransform();

            g2d.setColor(Color.black);
            g.setFont(Constant.DefaultFont.FVF_FERNANDO_CONVERSATION);
            FontRenderContext frc = g2d.getFontRenderContext();
            TextLayout tl = new TextLayout(currentConversation[currentSubtitleIndex], g.getFont().deriveFont(11F), frc);
            transform.setToScale(1,1);
            transform.translate(((double) GamePlayPanel.SCREEN_WIDTH-tl.getBounds().getWidth())/2, GamePlayPanel.SCREEN_HEIGHT-30);
            g2d.transform(transform);
            Shape shape = tl.getOutline(null);
            g2d.setStroke(new BasicStroke(4f));
            g2d.draw(shape);
            g2d.setColor(Color.WHITE);
            g2d.fill(shape);
        }
    }



    @Override
    public void eventHandle() {

    }


}
