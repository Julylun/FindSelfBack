package com.findselfback.Level;

import com.findselfback.View.GamePlayPanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;

public class LoadSave {
    public static String MAP_ATLAS_PATH = "Resources/assets/terrain_sprite_sheet_map.png";

    public static String STAGE_ONE_PATH = "Resources/map/state_1.png";

    public static BufferedImage getSpriteAtlas(String filePath) {

        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bufferedImage;
    }

    public static LayerTile[][] imageToLevelData(String filePath){
        return imageToLevelData(getSpriteAtlas(filePath));
    }

    public static LayerTile[][] imageToLevelData(BufferedImage mapImage){
        if(mapImage.getWidth() == 0 || mapImage.getHeight() == 0) return null;
        LayerTile[][] mapData = new LayerTile[mapImage.getHeight()][mapImage.getWidth()];
        for(int rowIndex = 0; rowIndex < mapImage.getHeight(); rowIndex++){
            for(int columnIndex = 0; columnIndex < mapImage.getWidth(); columnIndex++){
                Color pointColor = new Color(mapImage.getRGB(columnIndex,rowIndex));
                System.out.print(pointColor.getRed() + " " + pointColor.getBlue() + " " + pointColor.getBlue());
                mapData[rowIndex][columnIndex] = new LayerTile();
                int depth = 0;
                if(pointColor.getRed() != 11) mapData[rowIndex][columnIndex].add(pointColor.getRed(),depth++);
                if(pointColor.getGreen() != 11) mapData[rowIndex][columnIndex].add(pointColor.getGreen(),depth++);
                if(pointColor.getBlue() != 11)mapData[rowIndex][columnIndex].add(pointColor.getBlue(),depth++);
                if(pointColor.getAlpha() != 11)mapData[rowIndex][columnIndex].add(pointColor.getAlpha(),depth++);
//                mapData[rowIndex][columnIndex] = pointColor.getRed();
            }
            System.out.println();
        }
        return mapData;
    }
}

