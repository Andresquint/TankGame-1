import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Observable;
import java.util.Observer;


public class TankGameWorld extends JApplet implements Runnable {

    private Thread thread;
    private static final TankGameWorld tankGame = new TankGameWorld();
    private final int width = 900, height = 600;

    private BufferedImage battleField;

    public void init() {

        setFocusable(true);
        try {
            battleField = ImageIO.read(new File("Resources/Background.bmp"));
        }
        catch (Exception e) {
            System.out.print(e.getStackTrace() +" No resources are found");
        }
    }

    public void start() {
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    public void run() {

        Thread me = Thread.currentThread();
        while (thread == me) {
            repaint();
            try {
                thread.sleep(25);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void paint(Graphics g) {

        int tileWidth = battleField.getWidth();
        int tileHeight = battleField.getHeight();

        int NumberX = 5;
        int NumberY = 7;

        for (int i = 0; i < NumberY; i++) {
            for (int j = 0; j < NumberX; j++) {
                g.drawImage(battleField, j * tileWidth, i * tileHeight, tileWidth, tileHeight, this);
            }
        }
    }

    public static void main(String[] args) {

        TankGameWorld TGW = new TankGameWorld();
        TGW.init();
        JFrame JF = new JFrame("Tank Game");
        JF.addWindowListener(new WindowAdapter() {});
        JF.getContentPane().add("Center",TGW);
        JF.pack();
        JF.setSize(new Dimension(tankGame.width, tankGame.height));
        JF.setResizable(false);
        JF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JF.setVisible(true);
        TGW.start();
    }
}
