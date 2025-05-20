package studio.retrozoni.activities;

import studio.retrozoni.activities.shared.Buttom;
import studio.retrozoni.engine.Activity;
import studio.retrozoni.engine.Engine;

import java.awt.*;

public class OverView implements Activity {

    Buttom buttonClose;

    public OverView() {
        this.buttonClose = new Buttom(Engine.getSheet("icon_x"), new Point(10, 10)).setColor(0xfa0707, 0xa8072c);
    }

    @Override
    public void tick() {
        if(this.buttonClose.clicked()) {
            Engine.close();
        }
    }

    @Override
    public void render(Graphics2D g) {
        this.buttonClose.render(g);
    }

    @Override
    public void dispose() {

    }
}
