package com.findselfback.Level;

import com.findselfback.View.GamePlayPanel;
import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;

@Data
/**
 * MapManager dùng để tạo và chứa map
 * Cách sử dụng:
 * - Tạo đối tượng: MapManager mapManager = new MapManager(GamePlayManager);
 * - Cài đặt map texture: mapManager.autoGetSprite(LoadSave.getSpriteAtlas(filePath))
 * - Cài đặt map data (vị trí đặt tile: mapManager.setLevel(dataPath);
 * - Đưa hàm mapManager.draw(g) vào vòng lặp paint();
 *
 */
public class MapManager {
    private GamePlayPanel thisGamePlayPanel;
    private BufferedImage[] spriteMap;
    private Level level;
    public MapManager(GamePlayPanel gamePlayPanel){
        thisGamePlayPanel = gamePlayPanel;
    }

    /**
     * Sử dụng đối tượng BufferImage có chứa sprite sheet để tự động tạo ra từng sprite riêng biệt
     * dựa trên ORIGINAL_TILE_SIZE.
     * Để sprite hoàn hảo, ràng buộc rằng sprite sheet có tỉ lệ 1:1 và chiều dài chia
     * hết cho ORIGINAL_TILE_SIZE
     * @param spriteSheet
     */
    public void autoGetSprite(BufferedImage spriteSheet){
        autoGetSprite(spriteSheet,GamePlayPanel.ORIGINAL_TILE_SIZE,GamePlayPanel.ORIGINAL_TILE_SIZE);
    }

    /**
     * Sử dụng đối tượng BufferImage có chứa sprite sheet để tự động tạo ra từng sprite riêng biệt
     * dựa trên các tham số tileWidth và tileHeight
     * Để sprite hoàn hảo, ràng buộc rằng sprite sheet có tỉ lệ tileWidth:tileHeight và chiều dài chia
     * hết cho tileWidth, chiều cao chia hết cho tileHeight. Mỗi sprite trong sprite sheet phải có kích thước
     * đúng bằng tileWidth x tileHeight
     * @param spriteSheet
     * @param tileWidth
     * @param tileHeight
     */
    public void autoGetSprite(BufferedImage spriteSheet, int tileWidth, int tileHeight){
        int column = spriteSheet.getWidth()/tileWidth;
        int row = spriteSheet.getHeight()/tileHeight;
        spriteMap = new BufferedImage[column*row];
        int spriteIndex = 0;
        for(int rowIndex = 0; rowIndex < row; rowIndex++){
            for(int columnIndex = 0; columnIndex < column; columnIndex++){
                spriteMap[spriteIndex] = spriteSheet.getSubimage(columnIndex*tileWidth,
                        rowIndex*tileHeight,
                        tileWidth,
                        tileHeight);
                spriteIndex++;
            }
        }
    }

    public static BufferedImage[] _autoGetSprite(BufferedImage spriteSheet, int tileWidth, int tileHeight){
        int column = spriteSheet.getWidth()/tileWidth;
        int row = spriteSheet.getHeight()/tileHeight;
        BufferedImage[] _spriteMap = new BufferedImage[column*row];
        int spriteIndex = 0;
        for(int rowIndex = 0; rowIndex < row; rowIndex++){
            for(int columnIndex = 0; columnIndex < column; columnIndex++){
                _spriteMap[spriteIndex] = spriteSheet.getSubimage(columnIndex*tileWidth,
                        rowIndex*tileHeight,
                        tileWidth,
                        tileHeight);
                spriteIndex++;
            }
        }
        return _spriteMap;
    }

    /**
     * Cài đặt vị trí của các thực thể map
     * @param levelImagePath
     */
    public void setLevel(String levelImagePath){
        level = new Level();
        level.setLevelData(LoadSave.imageToLevelData(LoadSave.STAGE_ONE_PATH));
    }

    public void draw(Graphics g, int xOffset){
        Graphics2D graphics2D = (Graphics2D) g.create();
        for(int rowIndex = 0; rowIndex < GamePlayPanel.MAX_SCREEN_ROW; rowIndex++){
            for(int columnIndex = 0; columnIndex < level.levelData[0].length; columnIndex++){
                graphics2D.drawImage(
                        spriteMap[level.getSpriteMapIndex(rowIndex,columnIndex)],
                        columnIndex*GamePlayPanel.TILE_SIZE - xOffset,
                        rowIndex*GamePlayPanel.TILE_SIZE,
                        GamePlayPanel.TILE_SIZE,
                        GamePlayPanel.TILE_SIZE,
                        null
                );
            }
        }
    }
    public void update(){

    }
}
