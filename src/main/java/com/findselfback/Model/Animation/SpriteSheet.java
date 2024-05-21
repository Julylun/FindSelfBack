package com.findselfback.Model.Animation;

import com.findselfback.Model.Graphic.Coordinate2D;
import com.findselfback.Model.PrintColor;
import lombok.Data;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

@Data
public class SpriteSheet {
    private BufferedImage assetSheet;
    private BufferedImage currentSprite;
    private Vector<Coordinate2D> currentCoordinate;
    private Integer currentSpriteKey;
    private Map<Integer, Vector<Coordinate2D>> spriteMap;
    private int delayTime;
    private int currentTime;
    private int tileWidth, tileHeight;
    private int frameIndex;
    public SpriteSheet(String assetSheetPath, int w, int h){
        try {
            this.assetSheet = ImageIO.read(new File(assetSheetPath));
            currentSpriteKey = -1;
            this.tileHeight = h;
            this.tileWidth = w;
            spriteMap = new HashMap<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set sprite by sprite name
     * @param spriteKey
     */
    public void setCurrentSprite(int spriteKey){
        if(currentSpriteKey == spriteKey) return;
        currentCoordinate = spriteMap.get(spriteKey);
        currentSpriteKey = spriteKey;
        Coordinate2D coordinate2D = currentCoordinate.get(0);
        currentSprite = assetSheet.getSubimage(coordinate2D.getX(),
                coordinate2D.getY(),
                tileWidth,
                tileHeight
        );
            frameIndex = 0;
            currentTime = 0;
    }

    /**
     * Skip current frame and set current sprite to next sprite
     */
    public void nextFrame(boolean isFlip){
        if(currentTime >= delayTime){
            if(frameIndex >= currentCoordinate.size() - 1) frameIndex = -1;
            frameIndex++;
            Coordinate2D coordinate2D = currentCoordinate.get(frameIndex);
            currentSprite = assetSheet.getSubimage(coordinate2D.getX(),
                    coordinate2D.getY(),
                    tileWidth,
                    tileHeight
            );
            if(isFlip) currentSprite = createFlipped(currentSprite);
            currentTime = 0;
        } else currentTime++;

    }

    /**
     * Create a new sprite (animation), for example walking, idle, running, attacking...
     *
     * @param spriteKey
     */
    public void createSprite(int spriteKey){
        spriteMap.put(spriteKey, new Vector<Coordinate2D>());
    }
    public void createSprite(int spriteKey, Vector<Coordinate2D> spriteCoordinate){
        spriteMap.put(spriteKey, spriteCoordinate);
    }

    /**
     * Remove a sprite by sprite name
     * @param spriteKey
     */
    public void removeSprite(int spriteKey){
        spriteMap.remove(spriteKey);
    }

    /**
     * Add a frame to sprite;
     * return true if exist sprite name, else return false
     * @param spriteKey
     * @param spriteCoordinate
     * @return
     */
    public boolean addSpriteFrame(int spriteKey, Coordinate2D spriteCoordinate){
        Vector<Coordinate2D> spriteElement = spriteMap.get(spriteKey);
        if(spriteElement == null){
            return false;
        }
        spriteElement.add(spriteCoordinate);
        return true;
    }

    /**
     * Remove a frame from sprite coordinated vector containing your frame.
     * @param spriteKey
     * @param spriteIndex
     * @return true if removing is completed, return false if the sprite map have
     * no sprite having name like spriteName or no frame is deleted
     */
    public boolean removeSpriteFrame(int spriteKey, int spriteIndex){
        Vector<Coordinate2D> spriteElement = spriteMap.get(spriteKey);
        if(spriteElement == null){
            return false;
        }
        if(spriteElement.remove(spriteIndex) == null){
            return false;
        }
        return true;
    }

    /**
     * Ham copy khong can hieu, muon hieu thi ib giai thich cho nma cai nay khong can thiet de hieu dau
     * @param image
     * @return
     */
    private static BufferedImage createFlipped(BufferedImage image)
    {
        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(-1, 1));
        at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(), 0));
        return createTransformed(image, at);
    }
    private static BufferedImage createTransformed(
            BufferedImage image, AffineTransform at)
    {
        BufferedImage newImage = new BufferedImage(
                image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.transform(at);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }
}
