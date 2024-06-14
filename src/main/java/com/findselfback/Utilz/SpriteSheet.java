package com.findselfback.Utilz;

import lombok.Data;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
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

    /**
     * SpriteSheet dùng để đơn giản hóa cài đặt asset cho nhân vật
     * tham số bao gồm "đường dẫn tới sprite sheet", tile width, tile height.
     * Cách sử dụng:
     * - Tạo ra một đối tượng SpriteSheet;
     * - Tạo ra một đối tượng Vector<Coordinate2D>
     * - Thêm vào đối tượng vector trên các tọa độ của frame trong animation
     * - Sử dụng hàm createSprite(int spriteKey, Vector<Coordinate2>) để tạo ra một animation mới
     * - Sử dụng hàm setDelayTime(int delayTime) để tùy chỉnh thời gian một frame chay
     * - Sử dụng hàm setCurrentSprite(int spriteKey) để gọi animation cần chạy
     * - Sử dụng hàm nextFrame(bool isFlip) để chuyển qua frame tiếp theo của animation đã đặt
     * @param assetSheetPath
     * @param w
     * @param h
     */
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
     * Đặt animation hiện tại bằng sprite key
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
     * Bỏ qua frame hiện tại và chuyển qua frame tiếp theo của animation.
     * Nếu tham số isFlip = true thì frame sẽ được lật theo phương thẳng đứng, ngược lại không lật
     */
    public void nextFrame(boolean isFlip){
        if(currentTime >= delayTime){
            if(frameIndex >= currentCoordinate.size() - 1) frameIndex = -1;
            frameIndex++;

            //Lấy tọa độ ảnh -> lấy ảnh từ sprite sheet bằng cách lấy tọa độ + chiều dài/chiều rộng ảnh
            Coordinate2D coordinate2D = currentCoordinate.get(frameIndex);
            currentSprite = assetSheet.getSubimage(coordinate2D.getX(),
                    coordinate2D.getY(),
                    tileWidth,
                    tileHeight
            );
            //bỏ qua
            if(isFlip) currentSprite = createFlipped(currentSprite);
            currentTime = 0;
        } else currentTime++;

    }

    /**
     * Create a new sprite (animation), for example walking, idle, running, attacking...
     * Thêm một animation vào sprite sheet, ví dụ như ĐI, ĐỨNG YÊN, CHẠY, TẤN CÔNG...
     *
     * @param spriteKey
     */
    public void createSprite(int spriteKey){
        spriteMap.put(spriteKey, new Vector<Coordinate2D>());
    }
    public void createSprite(int spriteKey,int xPos, int yPos, int width, int height, int numberOfSprite){
        Vector<Coordinate2D> spriteCoordinateList = new Vector<Coordinate2D>();
        for(int i = 0; i < numberOfSprite; i++){
            spriteCoordinateList.add(new Coordinate2D(xPos + width*i,yPos));
        }
        createSprite(spriteKey,spriteCoordinateList);
    }
    public void createSprite(int spriteKey, Vector<Coordinate2D> spriteCoordinate){
        spriteMap.put(spriteKey, spriteCoordinate);
    }

    /**
     * Loại bỏ một animation ra khỏi sprite sheet bằng sprite key
     * @param spriteKey
     */
    public void removeSprite(int spriteKey){
        spriteMap.remove(spriteKey);
    }

    /**
     * Thêm một frame vào animation
     * Trả về true nếu animation tồn tại và thêm thành công, trả về false nếu animation không tồn tại
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
     * Loại bỏ một frame ra khỏi animation
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
