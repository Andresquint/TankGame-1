import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import static javax.imageio.ImageIO.read;

public class TankGameWorld extends JPanel {

    public static final int width = 1408, height = 704;
    private Graphics2D buffer, g2;
    private JFrame jf;
    private static Tank tank1, tank2;
    private Image wall, breakableWall, bulletImg;
    private InputStream textWalls;
    private ArrayList<Wall> walls = new ArrayList<>();
    private BufferedImage battleField, world;
    private Rectangle tank1Rec, tank2Rec, wallRec, bulletRec;


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
            bulletImg = ImageIO.read(new File("Resources/Shell 2.gif"));
            System.out.println(System.getProperty("user.dir"));
            tank1img = read(new File("Resources/Tank1.gif"));
            tank2img = read(new File("Resources/Tank2.gif"));
            mapMaker();
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        tank1 = new Tank(3,100, 550,  0, tank1img);
        TankControl tankC1 = new TankControl(tank1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);

        tank2 = new Tank(3,1240, 90, 180, tank2img);
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

    public static Tank getTank1() {
        return tank1;
    }

    public static Tank getTank2() {
        return tank2;
    }

    public Image getBulletImage() {
        return bulletImg;
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

        int numberX = width / tileWidth;
        int numberY = height / tileHeight;

        for (int i = -1; i <= numberY; i++) {

            for (int j = 0; j <= numberX; j++) {
                buffer.drawImage(battleField, j * tileWidth,i * tileHeight + tileHeight,
                        tileWidth, tileHeight, this);
            }
        }
    }
    
    public void mapMaker() {

        BufferedReader line = new BufferedReader(new InputStreamReader(textWalls));
        int i, j = 0;
        String number;

        try {
            number = line.readLine();

            while (number != null) {

                for ( i = 0; i < number.length(); i++ ) {

                    if (number.charAt(i) == '1') {
                        walls.add(new Wall(wall, i * 32, j * 32, 1));
                    }
                    else if (number.charAt(i) == '2') {
                        walls.add(new Wall(breakableWall, i * 32, j * 32, 2));
                    }
                }
                j++;
                number = line.readLine();
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

    public void checkCollision() {

        tank1Rec = tank1.getTankRectangle();
        tank2Rec = tank2.getTankRectangle();

        if (tank1Rec.intersects(tank2Rec)) {

            tank1.setCollides(true);
            tank1.handleCollision();
        }
        if (tank2Rec.intersects(tank1Rec)) {

            tank2.setCollides(true);
            tank2.handleCollision();
        }
        for (int i = 0; i <= walls.size() - 1; i++) {

            wallRec = walls.get(i).getWallRectangle();

            if (tank1Rec.intersects(wallRec) || tank2Rec.intersects(wallRec)) {

                tank1.setCollides(true);
                tank1.handleCollision();
                tank2.setCollides(true);
                tank2.handleCollision();
                }
            for (int j = 0; j < tank1.getBulletList().size(); j++) {

                bulletRec = tank1.getBulletList().get(j).getBulletRectangle();

                if (wallRec.intersects(bulletRec)) {
                    System.out.println("hit");
                    tank1.getBulletList().remove(j);
                if (walls.get(i).getType() == 2)
                    walls.remove(i);
                }
            }
            for (int j = 0; j < tank2.getBulletList().size(); j++) {

                bulletRec = tank2.getBulletList().get(j).getBulletRectangle();

                if (wallRec.intersects(bulletRec)) {
                    System.out.println("hit");
                    tank2.getBulletList().remove(j);

                    if (walls.get(i).getType() == 2)
                        walls.remove(i);
                }
            }
        }
        for (int i = 0; i < tank1.getBulletList().size(); i++) {

            bulletRec = tank1.getBulletList().get(i).getBulletRectangle();

            if (tank2Rec.intersects(bulletRec)) {
                System.out.println("hit");
                tank1.getBulletList().remove(i);
                tank2.setHealth(tank2.getHealth()-1);
                System.out.println(tank2.getHealth());
                tank2.Respawn();
            }
        }
        for (int i = 0; i < tank2.getBulletList().size(); i++) {

            bulletRec = tank2.getBulletList().get(i).getBulletRectangle();

            if (tank1Rec.intersects(bulletRec)){
                System.out.println("hit");
                tank2.getBulletList().remove(i);
                tank1.setHealth(tank1.getHealth()-1);
                System.out.println(tank1.getHealth());
                tank1.Respawn();
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
                TGW.checkCollision();
                TGW.repaint();
                Thread.sleep(1000 / 144);
            }
        }
        catch (InterruptedException ignored) {
        }
    }
}

