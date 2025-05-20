package studio.retrozoni.engine;

import studio.retrozoni.engine.inputs.Mouse;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serial;

public class Window extends Canvas {

    @Serial
    private static final long serialVersionUID = 36752349087L;
    private final String name;
    private final int width;
    private final int height;
    private boolean pointing;
    private JFrame frame;
    private final Toolkit toolkit;

    public Window(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.toolkit = Toolkit.getDefaultToolkit();
        initFrame();
        Mouse m = new Mouse();
        addMouseListener(m);
        addMouseMotionListener(m);
        addMouseWheelListener(m);
    }

    public void initFrame(){
        this.frame = new JFrame(this.name);
        frame.add(this);
        frame.setUndecorated(false);
        frame.setResizable(true);
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = new Dimension(width, height);
        setPreferredSize(dim);
        frame.setMinimumSize(dim);
        createOpenGl();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        showAccelerators();
    }

    private void createOpenGl() {
        try {
            System.setProperty("sun.java2d.opengl", "true");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void showAccelerators() {
        boolean oglEnabled = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .getImageCapabilities()
                .isAccelerated();
        System.out.println("OpenGL: " + System.getProperty("sun.java2d.opengl")); // "true" se OpenGL estiver ativado
        System.out.println("DirectX: " + System.getProperty("sun.java2d.d3d"));   // "true" se Direct3D estiver ativado
        System.out.println("OpenGL Pipeline enabled: " + oglEnabled);
    }

    public void pointing() {
        this.pointing = true;
    }


    private void closeFrame() {
        frame.setVisible(false);
        frame.dispose();
        frame = null;
    }

    public void resetWindow() {
        closeFrame();
        initFrame();
        requestFocus();
    }

    public synchronized void setCursor(BufferedImage cursor) {
        Cursor c = toolkit.createCustomCursor(cursor, new Point(0,0), "cursor");
        frame.setCursor(c);
    }

    public synchronized void setCursor(BufferedImage cursor, Point pointClick) {
        Cursor c = toolkit.createCustomCursor(cursor, pointClick, "cursor");
        frame.setCursor(c);
    }

    //Getter's and Setter's

    public JFrame getFrame() {
        return this.frame;
    }

    public int getWidth() {
        Component c = frame.getComponent(0);
        return c.getWidth();
    }

    public int getHeight() {
        Component c = frame.getComponent(0);
        return c.getHeight();
    }

    public Dimension getScreenSize() {
        return toolkit.getScreenSize();
    }


    public void normalCursor() {
    }
}
