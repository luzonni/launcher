package studio.retrozoni.engine;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Configs {

    private static Map<String, Object> VALUES;

    public static void init() {
        VALUES = new HashMap<>();
        VALUES.put("VOLUM", 100);
        VALUES.put("SCALE", 3);
    }

    public static void load() {
        String path = "config.json";
        File file = new File(path);
        if(!file.exists()) {
            update();
            return;
        }
        JSONObject object = null;
        try {
            InputStream istream = new FileInputStream(file);
            Reader isr = new InputStreamReader(istream);
            JSONParser parse = new JSONParser();
            parse.reset();
            object = (JSONObject) parse.parse(isr);
        }catch (Exception e) {}
        try {
            String[] keys = VALUES.keySet().toArray(new String[0]);
            for(int i = 0; i < keys.length; i++) {
                String key = keys[i];
                if(object != null && object.containsKey(key)) {
                    VALUES.replace(key, object.get(key));
                }
            }
        } catch (Exception e) {
            file.delete();
        }
    }

    public static void update() {
        String path = "config.json";
        JSONObject object = new JSONObject();
        String[] keys = VALUES.keySet().toArray(new String[0]);
        for(int i = 0; i < keys.length; i++) {
            String key = keys[i];
            object.put(key, VALUES.get(key));
        }
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(object.toJSONString());
            writer.close();
        } catch (IOException ignore) { }
    }
    public static int Scale() {
        return ((Number)VALUES.get("SCALE")).intValue();
    }

    public static void setScale(int scale) {
        VALUES.replace("SCALE", scale);
    }
    public static int Volum() {
        return ((Number)VALUES.get("VOLUM")).intValue();
    }

    public static void setVolum(int VOLUM) {
        VALUES.replace("VOLUM", VOLUM);
        update();
    }

}
