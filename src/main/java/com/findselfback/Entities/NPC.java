package com.findselfback.Entities;

import com.findselfback.GameState.Playing;
import com.findselfback.Utilz.SpriteSheet;
import com.findselfback.View.GamePlayPanel;
import lombok.Data;

import java.awt.*;
import java.awt.geom.Rectangle2D;

@Data
public class NPC extends Entity{
    private static final int TALK_STATE = 0;

    private SpriteSheet spriteSheet;
    private SpriteSheet state;
    private Rectangle2D.Float eventBox;
    private Playing thisPlaying;
    private float width, height;
    private int role;
    private boolean isDisplayState = false;
    public static final int TRAFFIC_POLICE = 0;
    public NPC(Playing playing, String assetPath, float assetWidth, float assetHeight, int role){
        thisPlaying = playing;
        this.role = role;
        spriteSheet = new SpriteSheet(assetPath, (int) assetWidth, (int) assetHeight);
        this.x = x;
        this.y = y;
        init();
    }

    private void init(){
        stateInit();
        eventBox = new Rectangle2D.Float();
        hitBox = new Rectangle2D.Float();
        eventBox = new Rectangle2D.Float();
    }

    private void stateInit(){
        state = new SpriteSheet("Resources/assets/NPC/state_symbol.png", 16,16);
        state.createSprite(TALK_STATE,0,0,16,16,10);
        state.setDelayTime(10);
        state.setCurrentSprite(TALK_STATE);
        isDisplayState = true;
    }

    public void setEventBox(float x, float y, float width, float height){
        eventBox.x = x;
        eventBox.y = y;
        eventBox.width = width;
        eventBox.height = height;
    }

    public void configure(float xPos, float yPos, float width, float height){
        stateInit();
        x = xPos;
        y = yPos;
        this.width = width;
        this.height = height;
    }

    @Override
    public void update() {
        if(eventBox.contains(thisPlaying.getPlayer().getHitBox())){
            isDisplayState = true;
            if(thisPlaying.EButton){
                thisPlaying.getSubtitle().setEventSubtitleByNPC(role);
            }
        } else isDisplayState = false;
    }

    @Override
    public void eventHandle() {

    }

    @Override
    public void paint(Graphics g, int xOffset) {
        g.drawImage(spriteSheet.getCurrentSprite(), (int) x - xOffset, (int) y, (int) width, (int) height,null);
        spriteSheet.nextFrame(false);
        if(isDisplayState){
            g.drawImage(state.getCurrentSprite(), (int) ((x + width/2- 8) - xOffset), (int) (y - 5 - 16),16,16,null);
            state.nextFrame(false);
        }

        if(thisPlaying.getGamePlayPanel().isDebugging()){
            g.setColor(Color.orange);
            g.drawRect((int) x - xOffset, (int) y, (int) width, (int) height);
            g.drawRect((int)eventBox.x - xOffset, (int) eventBox.y, (int) eventBox.width, (int) eventBox.height);
        }
    }
}
