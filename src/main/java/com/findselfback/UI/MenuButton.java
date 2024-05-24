package com.findselfback.UI;

import com.findselfback.GameState.GameState;
import com.findselfback.Utilz.Constant;
import lombok.Data;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

@Data
public class MenuButton {
    private Rectangle2D.Float border;
    private Rectangle2D.Float hitBox;
    public final Color NO_ON = new Color(145, 145, 145);
    public final Color ON = new Color(218, 218, 218);
    public final Color PRESSED = new Color(255, 255, 255);
    private Color stateColor;
    private String text;
    public MenuButton(float x, float y, String text){
        border = new Rectangle2D.Float();
        hitBox = new Rectangle2D.Float();
        border.x = x;
        border.y = y;
        this.text = text;
        border.width = Constant.UI.MenuSize.SCALING_WIDTH;
        border.height = Constant.UI.MenuSize.SCALING_HEIGHT;
        init();
    }

    public void init(){
        stateColor = NO_ON;
    }

    public void draw(Graphics g){
        Graphics2D graphics2D = (Graphics2D) g.create();
        FontMetrics fontMetrics = new Canvas().getFontMetrics(Constant.UI.MenuSize.FVF_FERNANDO);
        int stringWidth = fontMetrics.stringWidth(text);
        hitBox.x = border.x + (border.width - stringWidth) / 2;
        hitBox.y = (int)(border.y + (fontMetrics.getAscent() - fontMetrics.getDescent() - fontMetrics.getLeading()));
        hitBox.width = stringWidth;
        hitBox.height = fontMetrics.getAscent() - fontMetrics.getDescent() - fontMetrics.getLeading();
//        graphics2D.drawRect((int) hitBox.x, (int) hitBox.y, (int) hitBox.width, (int) hitBox.height);

        graphics2D.setColor(stateColor);
        graphics2D.drawString(text,border.x + (border.width - stringWidth) / 2,
                (border.y + fontMetrics.getAscent() - fontMetrics.getDescent() - fontMetrics.getLeading()) + (border.height - fontMetrics.getHeight())/2
        );
//        System.out.println(hitBox);
        graphics2D.setColor(Color.black);
    }

    public boolean isIn(MouseEvent e){
        return hitBox.contains(e.getX(),e.getY());
    }

}
