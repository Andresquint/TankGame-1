import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import static javax.imageio.ImageIO.read;

public class TankGameWorld extends JPanel {

    public static final int width = 1216, height = 608;
    private Graphics2D buffer, g2;
    private JFrame jf;
    private Tank tank1;
    private Tank tank2;
    private Image wall, breakableWall;
    private BufferedImage battleField, world;
    private ArrayList<Wall> walls = new ArrayList<>();
    private InputStream textWalls;

    public void init() {

        this.jf = new JFrame("Tank Wars");
        this.jf.setLocation(200, 200);
        this.world = new BufferedImage(TankGameWorld.width, TankGameWorld.height, BufferedImage.TYPE_INT_RGB);
        BufferedImage tank1img = null, tank2img = null;

        try {
            BufferedImage tmp;
            battleField = ImageIO.read(new File("Resources/Background.bmp"));
            wall = ImageIO.read(new File("Resources/Wall1.gif"));
            breakableWall = ImageIO.read(new File("Resources/Wall2.gif"));
            textWalls = new FileInputStream("Resources/WallMap.txt");
            System.out.println(System.getProperty("user.dir"));
            tank1img = read(new File("Resources/Tank1.gif"));
            tank2img = read(new File("Resources/Tank2.gif"));
            mapMaker();
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        tank1 = new Tank(100, 450, 0, 0, 0, tank1img);
        TankControl tankC1 = new TankControl(tank1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);

        tank2 = new Tank(1065, 100, 0, 0, 180, tank2img);
        TankControl tankC2 = new TankControl(tank2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SHIFT);

        this.jf.setLayout(new BorderLayout());
        this.jf.add(this);
        this.jf.addKeyListener(tankC1);
        this.jf.addKeyListener(tankC2);
        this.jf.setSize(TankGameWorld.width, TankGameWorld.height + 30);
        this.jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jf.setVisible(true);
    }

        public void paintComponent(Graphics g) {

            g2 = (Graphics2D) g;
            g2.drawImage(world, 0, 0, null);
            buffer = world.createGraphics();

            drawBullets();
            drawBackGround(buffer);
            drawWall();
            tank1.draw(buffer);
            tank2.draw(buffer);
        }

        public void drawBullets() {

                for (int i = 0; i < tank1.getBulletList().size(); i++) {
                    tank1.getBulletList().get(i).draw(g2, this);
                }
                for (int i = 0; i < tank2.getBulletList().size(); i++) {
                    tank2.getBulletList().get(i).draw(g2, this);
                }
            }

        public void updateBullets() {

                for (int i = 0; i < tank1.getBulletList().size(); i++) {
                    tank1.getBulletList().get(i).update();
                }
                for (int i = 0; i < tank2.getBulletList().size(); i++) {
                    tank2.getBulletList().get(i).update();
                    }
                }

        public void drawBackGround(Graphics2D buffer) {

            int tileWidth = battleField.getWidth();
            int tileHeight = battleField.getHeight();

            int NumberX = width / tileWidth;
            int NumberY = height / tileHeight;

            for (int i = -1; i <= NumberY; i++) {

                for (int j = 0; j <= NumberX; j++) {
                    buffer.drawImage(battleField, j * tileWidth,i * tileHeight + tileHeight,
                            tileWidth, tileHeight, this);
                }
            }
        }

    public void mapMaker() {

        BufferedReader line = new BufferedReader(new InputStreamReader(textWalls));
        int i, j = 0;
        String num;

        try {
            num = line.readLine();

            while (num != null) {

                for ( i = 0; i < num.length(); i++) {

                    if (num.charAt(i) == '1') {
                        walls.add(new Wall(wall, i * 32, j * 32));
                    }
                    else if (num.charAt(i) == '2') {
                        walls.add(new Wall(breakableWall, i * 32, j * 32));
                    }
                }
                j++;
                num = line.readLine();
            }
        }
        catch (Exception e) {
            System.err.println("Map Maker" + e);
        }
    }

    public void drawWall() {

        if (!walls.isEmpty()) {

            for (int i = 0; i <= walls.size() - 1; i++) {
                walls.get(i).draw( buffer);
            }
        }
    }

    public static void main(String[] args) {

        TankGameWorld TGW = new TankGameWorld();
        TGW.init();

        try {
            while (true) {

                TGW.tank1.update();
                TGW.tank2.update();
                TGW.updateBullets();
                TGW.repaint();
                Thread.sleep(1000 / 144);
            }
        }
        catch (InterruptedException ignored) {

        }
    }
}

