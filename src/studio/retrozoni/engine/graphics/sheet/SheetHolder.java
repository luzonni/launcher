package studio.retrozoni.engine.graphics.sheet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import studio.retrozoni.engine.Engine;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SheetHolder {

    private final Map<String, Sheet> SHEETS;
    private final JSONObject spriteSheet;

    public SheetHolder(JSONObject spriteSheet, int scale) {
        this.spriteSheet = spriteSheet;
        this.SHEETS = new HashMap<>();
        loadSprites(scale);
    }

    private void loadSprites(int scale) {
        JSONArray modules = (JSONArray) this.spriteSheet.get("Modules");
        JSONObject spritesList = (JSONObject) this.spriteSheet.get("Sprites");
        for(int i = 0; i < modules.size(); i++) {
            String module = modules.get(i).toString();
            JSONArray listModule = (JSONArray) spritesList.get(module);
            for(int j = 0; j < listModule.size(); j++) {
                JSONObject sprite = (JSONObject) listModule.get(j);
                String name = sprite.get("name").toString();
                int width = ((Number)sprite.get("width")).intValue() * scale;
                int height = ((Number)sprite.get("height")).intValue() * scale;
                BufferedImage spriteSheet = loadSpriteSheet(module.toLowerCase(), name, scale);
                Sheet sheet = new Sheet(spriteSheet, width, height);
                this.SHEETS.put(name, sheet);
            }
        }
    }

    private BufferedImage loadSpriteSheet(String module, String name, int scale) {
        BufferedImage sheet;
        String path = Engine.resourcesPath + "/sprites/" + module + "/" + name + ".png";
        try {
            URL resource = Objects.requireNonNull(this.getClass().getResource(path));
            sheet = ImageIO.read(resource);
            if(scale > 1) {
                sheet = setScale(sheet, sheet.getWidth()*scale, sheet.getHeight()*scale);
            }
        }catch(Exception e) {
            sheet = sadImage(scale);
        }
        return sheet;
    }

    private static BufferedImage setScale(BufferedImage img, int width, int height) {
        BufferedImage NewImg = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
        NewImg.getGraphics().drawImage(img, 0, 0, width, height, null);
        return NewImg;
    }

    private BufferedImage sadImage(int scale) {
        BufferedImage sad = new BufferedImage(16 * scale, 16 * scale, BufferedImage.TYPE_INT_RGB);
        Graphics g = sad.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 6 * scale, 6 * scale);
        g.setColor(Color.MAGENTA);
        g.fillRect(0, 0, 8 * scale, 8 * scale);
        g.fillRect(8 * scale, 8 * scale, 8 * scale, 8 * scale);
        g.dispose();
        return sad;
    }

    public Sheet getSHEETS(String name) {
        if(!this.SHEETS.containsKey(name))
            throw new RuntimeException("Sprite not found!");
        return this.SHEETS.get(name);
    }

}
