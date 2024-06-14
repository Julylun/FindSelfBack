package com.findselfback.Entities;

import com.findselfback.Control.InputHandle;
import com.findselfback.GameState.Playing;
import com.findselfback.Utilz.*;
import com.findselfback.View.GamePlayPanel;
import lombok.Data;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import static com.findselfback.Utilz.Constant.Animation.*;

@Data
public class Player extends Entity {
    private InputHandle thisInputHandle;
    private Playing thisPlaying;
    private SpriteSheet sprite;
    private boolean isMoving, isInAir = false;

    private boolean moveLeft = false, moveRight = false, moveTop = false, moveBot = false;
    private boolean isFlip = false;
    private int direction;

    private Clip walkSound, jumpSound;

    //Thuộc tính môi trường -> trọng trường, gió, lực nhảy
    private float gravity = (float)(0.02f * GamePlayPanel.SCALE);
    private float airSpeed = 0;
    private float jumpSpeed = (float)(-1.3f * GamePlayPanel.SCALE);
    private float fallingSpeedAfterCollision = (float) (0.5f * GamePlayPanel.SCALE);

//    [Frame -> windows chứa components]
//    GameFrame
//    [GamePlayPanel]; frame nó chỉ thị được duy nhất một phần tử, nó tương tự 1 panel;
//    Muốn GameFrame hiển thị game -> thêm một cái panel vào cái GameFrame


    public Player(Playing playing, String spritePath, InputHandle inputHandle){
        thisPlaying = playing;
        thisInputHandle = inputHandle; //gán vào một biết để nhận biêết nút khi mà người dùng bấm
        init();

        this.sprite = new SpriteSheet(spritePath, GamePlayPanel.ORIGINAL_TILE_SIZE, GamePlayPanel.ORIGINAL_TILE_SIZE);
        loadAnimation();
        loadAudio();


    }
    private void init(){
        hitBox = new Rectangle2D.Float();


        //Set location and speed
        this.x = 200;
        this.y = 200;
        hitBox.x = (int)this.x;
        hitBox.y = (int)this.y + GamePlayPanel.TILE_SIZE/5;
        hitBox.width = GamePlayPanel.TILE_SIZE*2/5;
        hitBox.height = GamePlayPanel.TILE_SIZE*4/5;
        this.speed = 0.8f;
    }

    private void drawHitBox(Graphics g, int xOffset,boolean isEnable){
        if(isEnable){
            g.setColor(Color.pink);
            g.drawRect((int)hitBox.x - xOffset,(int)hitBox.y,(int)hitBox.width,(int)hitBox.height);
            g.setColor(Color.black);
        }
    }
    private void updateHitBox(){
        hitBox.x = (int)this.x;
        hitBox.y = (int)this.y + GamePlayPanel.TILE_SIZE/5;
        if(direction == 3){
            hitBox.x = (int)this.x + hitBox.width*2/5;
        }
    }

    /**
     * Đưa vào một tốc độ, dịch chuyển nhân vật đúng bằng tốc độ đó và tránh bị xuyên tường
     * @param xSpeed
     */
    private void updateXPos(float xSpeed){
        if(Environment.canMoveHere(x+xSpeed,hitBox.y-1, hitBox.width, hitBox.height,
                thisPlaying.getMapManager().getLevel().getLevelData())
        ){
            x+= xSpeed;
        } else {
            hitBox.x = Environment.getEntityPosNearWall(hitBox,xSpeed);
        }
    }

    /**
     * đặt trạng thái bay thành false
     */
    private void resetInAir(){
        isInAir = false;
        airSpeed = 0;
    }

    private void loadAudio(){
        walkSound = AudioPlayer.getClip(Constant.AudioPath.Player.WALK);
        jumpSound = AudioPlayer.getClip(Constant.AudioPath.Player.JUMP);
        FloatControl volume = (FloatControl)walkSound.getControl(FloatControl.Type.MASTER_GAIN);
        volume.setValue(3/100f);

    }
    private void loadAnimation(){
        //Set sprite
        //--Running animation
        Vector<Coordinate2D> coordinate2DVector = new Vector<>();
        //Chứa list coordinate2D các tọa độ của frame;

        for(int i = 0; i < 13; i++) //13 frame
            coordinate2DVector.add(new Coordinate2D(i*32,32));
        sprite.createSprite(RUNNING,coordinate2DVector);
        //--Idle animation
        coordinate2DVector = new Vector<>();
        for(int i = 0; i < 11; i++)
            coordinate2DVector.add(new Coordinate2D(i*32,0));
        sprite.createSprite(IDLE,coordinate2DVector);
        sprite.setCurrentSprite(IDLE);

        coordinate2DVector = new Vector<>();
        for(int i = 0; i < 6; i++){
            coordinate2DVector.add(new Coordinate2D(i*32,3*32));
        }
        sprite.createSprite(FALLING,coordinate2DVector);

        sprite.setDelayTime(2);
    }

    /**
     *
     */
    private void jump(){
        if(isInAir) return;
        airSpeed += jumpSpeed;
        isInAir = true;
    }


    @Override
    public void eventHandle() {

    }
    @Override
    public void update() {
        float xSpeed = 0, ySpeed = 0;
        float newSpeed = (thisPlaying.shiftPressed)? speed*1.4f : speed;


        if(!isInAir && !Environment.isOnGround(hitBox,thisPlaying.getMapManager().getLevel().getLevelData())){
            isInAir = true;
        }

        if(!thisPlaying.isNoPressed()){
            if(thisPlaying.upPressed && !thisPlaying.downPressed){
                if(isInAir == false){
                    jumpSound.setFramePosition(0);
                    jumpSound.start();
                }
                jump();
            }
            if(!thisPlaying.upPressed && thisPlaying.downPressed){
//                ySpeed = newSpeed;
            }
            if(thisPlaying.leftPressed && !thisPlaying.rightPressed){
                xSpeed = -newSpeed;
                isFlip = true;
                direction = 3;
            }
            if(thisPlaying.rightPressed && !thisPlaying.leftPressed){
                xSpeed = newSpeed;
                isFlip = false;
                direction = 1;
            }
        }

        if(isInAir){
            if(Environment.canMoveHere(hitBox.x,hitBox.y+airSpeed,hitBox.width,hitBox.height,thisPlaying.getMapManager().getLevel().getLevelData())){
                y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);

            }
            else {
                hitBox.y = Environment.getEntityYPostUnderWallOrAboveGround(hitBox,airSpeed);
                if(airSpeed > 0) resetInAir();
                else {
                    airSpeed = fallingSpeedAfterCollision;
                    updateXPos(xSpeed);
                }
            }
        } else{
            updateXPos(xSpeed);
        }



        if(Environment.canMoveHere(hitBox.x,hitBox.y+ySpeed, hitBox.width, hitBox.height,
                thisPlaying.getMapManager().getLevel().getLevelData())
        ){
            y+= ySpeed;
        }
        updateHitBox();

    }

    @Override
    public void paint(Graphics g, int xOffset) {
        Graphics2D graphics2D = (Graphics2D) g.create();
//                PrintColor.debug(PrintColor.CYAN_BRIGHT, "Player", "update", "x: " + x + " | y: " + y);
        if(isInAir){
            sprite.setCurrentSprite(FALLING);
            sprite.nextFrame(false);
        }
        else{

            // Mail != gmail
            // Yahoo -> yahoo mail -> abcxyz@yahoo.com.vn
            // gmail -> abcxyz@gmail.com
            // @outlook.com.vn -> gooogle -> token -> chặn <- google != @outlook.com.vn
            // bắt buộc ng dùng đăng nhập bằng tkhoan google
            //
            // Tai khoan -> google -> API token -> fetch Gooogle -> lay data -> googgle calendar



            if (!thisPlaying.isNoPressed()) {
                if (thisPlaying.leftPressed && !thisPlaying.rightPressed) {
                    sprite.nextFrame(false);
                    sprite.setCurrentSprite(RUNNING);
                }
                if (thisPlaying.rightPressed && !thisPlaying.leftPressed) {
                    sprite.setCurrentSprite(RUNNING);
                    sprite.nextFrame(false);
                }
                if(thisPlaying.leftPressed && thisPlaying.rightPressed){
                    sprite.setCurrentSprite(IDLE);
                    sprite.nextFrame(thisInputHandle.getLastPressed() == KeyEvent.VK_D);
                }
                if(walkSound.getMicrosecondPosition() >= walkSound.getMicrosecondLength()){
                    walkSound.setFramePosition(0);
                }
                walkSound.start();


            } else {
                sprite.setCurrentSprite(IDLE);
                if (thisPlaying.lastPressed == KeyEvent.VK_A) {
                    isFlip = true;
                    sprite.nextFrame(false);
                } else {
                    isFlip = false;
                    sprite.nextFrame(false);
                }
            }
        }


        graphics2D.drawImage(sprite.getCurrentSprite(), ((isFlip) ?(int)(x + GamePlayPanel.TILE_SIZE - hitBox.width):(int)x) - xOffset, (int)y, (int)(sprite.getTileWidth()*GamePlayPanel.SCALE)*((isFlip) ? -1 : 1), (int)(sprite.getTileHeight()*GamePlayPanel.SCALE), null);
        drawHitBox(g,xOffset,thisPlaying.getGamePlayPanel().isDebugging());
    }

    public String getStringLocation(){
        return "x = " + x + " | y = " + y;
    }
}
