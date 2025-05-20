package studio.retrozoni.activities.shared;

import studio.retrozoni.engine.Configs;
import studio.retrozoni.engine.graphics.FontHandler;
import studio.retrozoni.engine.graphics.sheet.Sheet;
import studio.retrozoni.engine.inputs.Mouse;
import studio.retrozoni.engine.inputs.Mouse_Button;

import java.awt.*;

public class Buttom {

    private final Rectangle bounds;

    private Sheet imageContent;
    private String textContent;
    private Font font;
    private Color[] color;

    private final int padding;
    private int over;

    public Buttom(String textContent, Point position) {
        this.padding = Configs.Scale()*2;
        this.textContent = textContent;
        this.font = FontHandler.font(FontHandler.Septem, Configs.Scale()*8);
        int wF = FontHandler.getWidth(textContent, font);
        int hF = FontHandler.getHeight(textContent, font);
        this.bounds = sizing(position, wF, hF);
        this.color = new Color[]{new Color(0xfa0740), new Color(0xa8072c)};
    }

    public Buttom(Sheet imageContent, Point position) {
        this.padding = Configs.Scale()*2;
        int imageWidth = imageContent.getWidth();
        int imageHeight = imageContent.getHeight();
        this.imageContent = imageContent;
        bounds = sizing(position, imageWidth, imageHeight);
        this.color = new Color[]{new Color(0xfa0740), new Color(0xa8072c)};
    }

    public Buttom setColor(int... color) {
        if(color.length != 2)
            throw new RuntimeException("The color need 2 values");
        this.color[0] = new Color(color[0]);
        this.color[1] = new Color(color[1]);
        return this;
    }

    private Rectangle sizing(Point position, int width, int height) {
        return new Rectangle(position.x, position.y, width + padding*2, height + padding*2);
    }

    public boolean clicked() {
        this.over = Mouse.on(this.bounds) ? 1 : 0;
        return Mouse.clickOn(Mouse_Button.LEFT, this.bounds);
    }

    public void render(Graphics2D graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        g.setStroke(new BasicStroke(Configs.Scale()));
        g.setColor(color[over]);
        g.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, (int)(bounds.width*0.15f), (int)(bounds.width*0.15f));
        if(this.imageContent != null)
            g.drawImage(this.imageContent.getSprite(over), bounds.x + padding, bounds.y + padding, null);
    }

}
