package studio.retrozoni.engine.graphics.sheet;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sheet {

    private final BufferedImage[] sprites;
    private Rectangle bounds;
    private int index;

    public Sheet(BufferedImage spriteSheet, int width, int height) {
        this.bounds = new Rectangle(width, height);
        int length = spriteSheet.getWidth()/width;
        this.sprites = new BufferedImage[length];
        for(int i = 0; i < length; i++) {
            this.sprites[i] = spriteSheet.getSubimage(i*width, 0, width, height);
        }
    }

    public int getWidth() {
        return this.bounds.width;
    }

    public int getHeight() {
        return this.bounds.height;
    }

    public BufferedImage getSprite() {
        return sprites[this.index];
    }

    public BufferedImage getSprite(int index) {
        return sprites[index];
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void plus() {
        this.index ++;
        if(this.index > sprites.length-1) {
            this.index = 0;
        }
    }

}
