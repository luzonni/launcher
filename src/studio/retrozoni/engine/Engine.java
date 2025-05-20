package studio.retrozoni.engine;

import org.json.simple.JSONObject;
import studio.retrozoni.activities.OverView;
import studio.retrozoni.activities.loading.Loading;
import studio.retrozoni.engine.graphics.FontHandler;
import studio.retrozoni.engine.graphics.sheet.Sheet;
import studio.retrozoni.engine.graphics.sheet.SheetHolder;
import studio.retrozoni.activities.menu.Menu;
import studio.retrozoni.engine.sound.Sound;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

public class Engine implements Runnable {

    public static String LancherTag = "RetroZoni Studio";
    public static String VERSION = "ALPHA 0.0.1";

    private static SheetHolder sheetHolder;
    public static String resourcesPath = "/studio/retrozoni/resources";

    private static Thread thread;
    private static boolean isRunning;
    public static final double HZ = 60;
    public static final double T = 1_000_000_000.0;
    public static int FRAMES;
    public static int HERTZ;

    private static Activity OverView;
    private static List<Activity> stackActivities;
    private static boolean ACTIVITY_RUNNING;

    public static Window window;
    private static BufferStrategy BUFFER;


    public static Sheet getSheet(String name) {
        return Engine.sheetHolder.getSHEETS(name);
    }

    public Engine() {
        stackActivities = new ArrayList<>();
        Configs.init();
        Configs.load();
        FontHandler.addFont("septem");
        Sound.load();
        loadSpriteHolder();
        Engine.window = new Window(LancherTag, 1080, 720);
        Engine.OverView = new OverView();
        heapActivity(new Menu());
        start();
    }

    public static void close() {
        Engine.ACTIVITY_RUNNING = false;
        Engine.isRunning = false;
    }

    private void loadSpriteHolder() {
        JSONObject json;
        try {
            json = Resources.getJsonFile("/sprites", "SpriteSheet");
        }catch (Exception ignore) {
            throw new RuntimeException();
        }
        sheetHolder = new SheetHolder(json, Configs.Scale());
    }

    public static void heapActivity(Activity activity) {
        ACTIVITY_RUNNING = true;
        Engine.stackActivities.add(activity);
    }

    public static void heapActivity(Activity activity, ActionConsumer action) {
        ACTIVITY_RUNNING = true;
        Engine.stackActivities.add(new Loading(stackActivities, activity, action));
    }

    public static void backActivity() {
        Activity ac = Engine.getACTIVITY();
        if(ac != null) {
            ac.dispose();
        }
        Engine.stackActivities.removeLast();
    }

    public static void backActivity(int amount) {
        for(int i = 0; i < amount; i++) {
            Activity ac = Engine.getACTIVITY();
            if(ac != null) {
                ac.dispose();
            }
            Engine.stackActivities.removeLast();
        }
    }


    public static Activity getACTIVITY() {
        return Engine.stackActivities.getLast();
    }

    private Graphics2D getGraphics() {
        if(Engine.BUFFER == null) {
            window.createBufferStrategy(3);
            Engine.BUFFER = window.getBufferStrategy();
            return null;
        }
        Graphics2D graphics = (Graphics2D) BUFFER.getDrawGraphics();
        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, window.getWidth(), window.getHeight());
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        graphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
        return graphics;
    }

    private synchronized void start() {
        thread = new Thread(this, "Engine");
        isRunning = true;
        thread.start();
    }

    private synchronized void stop() {
        Sound.dispose();
        window.getFrame().dispose();
    }

    private void render(Graphics2D graphics) {
        graphics.dispose();
        BUFFER.show();
    }

    @Override
    public void run() {
        long lastTimeHZ = System.nanoTime();
        double amountOfHz = Engine.HZ;
        double ns_HZ = Engine.T / amountOfHz;
        double delta_HZ = 0;
        long lastTimeFPS = System.nanoTime();
        double amountOfFPS = 60;
        double ns_FPS = Engine.T / amountOfFPS;
        double delta_FPS = 0;
        int Hz = 0;
        int frames = 0;
        double timer = System.currentTimeMillis();
        window.requestFocus();
        while (isRunning) {
            try {
                long nowHZ = System.nanoTime();
                delta_HZ += (nowHZ - lastTimeHZ) / ns_HZ;
                lastTimeHZ = nowHZ;
                if (delta_HZ >= 1) {
                    OverView.tick();
                    if (ACTIVITY_RUNNING && !stackActivities.isEmpty()) {
                        getACTIVITY().tick();
                    }
                    Hz++;
                    delta_HZ--;
                }
                long nowFPS = System.nanoTime();
                delta_FPS += (nowFPS - lastTimeFPS) / ns_FPS;
                lastTimeFPS = nowFPS;
                if (delta_FPS >= 1) {
                    Graphics2D g = getGraphics();
                    if(g == null)
                        continue;
                    if (!stackActivities.isEmpty()) {
                        getACTIVITY().render(g);
                    }
                    OverView.render(g);
                    render(g);
                    frames++;
                    delta_FPS--;
                }
                //Show fps
                if (System.currentTimeMillis() - timer >= 1000) {
                    Engine.FRAMES = frames;
                    frames = 0;
                    Engine.HERTZ = Hz;
                    Hz = 0;
                    timer += 1000;
                }
                Thread.sleep(1);
            } catch (Exception e) {
                System.err.println("Exception: " + e.getMessage());
                System.err.println("==============================================================");
                e.printStackTrace();
                System.exit(1);
            }
        }
        stop();
        System.out.println("Exit");
    }


}
