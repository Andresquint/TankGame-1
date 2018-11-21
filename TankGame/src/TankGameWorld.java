import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import static javax.imageio.ImageIO.read;

public class TankGameWorld extends JPanel {

    public static final int worldWidth = 1408, worldHeight = 704;
    public static final int screenWidth = 1200, screenHeight = 500;
    private int screenBoundsX, screenBoundsY;
    private Graphics2D buffer, g2;
    private JFrame jf;
    private static Tank tank1, tank2;
    private TankControl tankC1, tankC2;
    private Image wall, breakableWall, powerUp;
    private InputStream textWalls;
    private BufferedReader bufferReader;
    private String input;
    private ArrayList<Wall> walls = new ArrayList<>();
    private BufferedImage battleField, world, tank1View, tank2View, tank1img, tank2img;
    private Rectangle tank1Rec, tank2Rec, wallRec, bulletRec;
    private boolean endGame = false;

    public void init() {

        this.jf = new JFrame("Tank Wars");
        this.jf.setLocation(200, 200);
        this.world = new BufferedImage(TankGameWorld.worldWidth, TankGameWorld.worldHeight, BufferedImage.TYPE_INT_RGB);

        try {
            battleField = read(new File("Resources/Background.bmp"));
            wall = read(new File("Resources/Wall1.gif"));
            breakableWall = read(new File("Resources/Wall2.gif"));
            powerUp = read(new File("Resources/Health.gif"));
            textWalls = new FileInputStream("Resources/WallMap.txt");
            bufferReader = new BufferedReader(new InputStreamReader(textWalls));
            input = bufferReader.readLine();
            tank1img = read(new File("Resources/Tank1.gif"));
            tank2img = read(new File("Resources/Tank2.gif"));
            mapMaker();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        tank1 = new Tank( tank1img, 100, 550,  0);
        tankC1 = new TankControl(tank1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);

        tank2 = new Tank(tank2img, 1240, 90, 180);
        tankC2 = new TankControl(tank2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SHIFT);

        this.jf.setLayout(new BorderLayout());
        this.jf.add(this);
        this.jf.addKeyListener(tankC1);
        this.jf.addKeyListener(tankC2);
        this.jf.setSize(TankGameWorld.screenWidth, TankGameWorld.screenHeight+30);
        this.jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jf.setVisible(true);
        }

    public void paintComponent(Graphics g) {

        g2 = (Graphics2D) g;
        buffer = world.createGraphics();

        // Draws the background, walls, bullets, and tanks
        drawBackGround(buffer);
        drawWall();
        drawBullets(buffer);
        tank1.draw(buffer);
        tank2.draw(buffer);

        /* Draws both of the Tanks views and passes in the tanks screenBounds checker that centers the screens
         * to the center of the tanks make sure it doesn't go beyond the game world */
        tank1View = world.getSubimage(getScreenBoundsX(tank1), getScreenBoundsY(tank1), screenWidth/2, screenHeight);
        tank2View = world.getSubimage(getScreenBoundsX(tank2), getScreenBoundsY(tank2),screenWidth/2, screenHeight);
        g2.drawImage(tank1View, 0, 0, this);
        g2.drawImage(tank2View, screenWidth/2, 0, this);

        // Draws the mini map and the white outlines
        Image scaledMap = world.getScaledInstance(200, 200, 0);
        g2.drawImage(scaledMap, screenWidth/2-125, screenHeight-135, 250, 125, this);
        g2.setColor(Color.white);
        g2.drawRect(471, 361, 258, 133);
        g2.drawLine(screenWidth/2,0,screenWidth/2,screenHeight-140);
        g2.drawLine(screenWidth/2,screenHeight-5,screenWidth/2, screenHeight);

        LivesCount();
        HealthBar();

        // Draws "player 1" and "player 2" at the bottom of the screen
        g2.setColor(Color.white);
        g2.setFont(new Font("", Font.PLAIN, 20));
        g2.drawString("Player 1",screenWidth-1052,screenHeight-70);
        g2.drawString("Player 2",screenWidth-302 ,screenHeight-70);
    }
    // Creates the health bars and reduces them when the tanks get hit by the bullets
    public void HealthBar() {

        g2.setColor(Color.white);
        g2.drawRect(screenWidth-1050, screenHeight-60, 104, 12);
        g2.drawRect(screenWidth-300, screenHeight-60, 104, 12);

        int healthReduction = 0;

        for (int i = 5; i > 1; i--) {

           if (tank1.getHealth() == i) {

               g2.setColor(Color.green);
               g2.fillRect(screenWidth-1048, screenHeight-58, 100-healthReduction, 8);
           }
           if (tank2.getHealth() == i) {

               g2.setColor(Color.green);
               g2.fillRect(screenWidth-298, screenHeight-58, 100-healthReduction , 8);
            }
            healthReduction += 20;
        }
        if (tank1.getHealth() == 1 ) {

            g2.setColor(Color.red);
            g2.fillRect(screenWidth-1048, screenHeight-58, 20, 8);
        }
        if (tank2.getHealth() == 1 ) {

            g2.setColor(Color.red);
            g2.fillRect(screenWidth-298, screenHeight-58, 20, 8);
        }
    }
    // draws the current lives on the screen
    public void LivesCount() {

        g2.setColor(Color.white);

        for (int i = 2; i >= 0 ; i--) {

            if (tank1.getLives() == i) {
                g2.drawString("Lives: " + i, screenWidth-930, screenHeight-50);
            }
            if (tank2.getLives() == i) {
                g2.drawString("Lives: " + i, screenWidth-180, screenHeight-50);
            }
        }
        if (tank1.getLives() == -1) {

            g2.drawString("Lives: 0", screenWidth-930, screenHeight-50);
            g2.setFont(new Font("", Font.PLAIN, 60));
            g2.drawString("Game Over: Player 2 Wins", screenWidth-1000, screenHeight/2);
            endGame = true;
            }
        if (tank2.getLives() == -1) {

            g2.drawString("Lives: 0", screenWidth-180, screenHeight-50);
            g2.setFont(new Font("", Font.PLAIN, 60));
            g2.drawString("Game Over: Player 1 Wins", screenWidth-1000, screenHeight/2);
            endGame = true;
        }
    }

    public boolean isEndGame() {
        return endGame;
    }

    public void drawBullets(Graphics2D buffer) {

        for (int i = 0; i < tank1.getBulletList().size(); i++) {
            tank1.getBulletList().get(i).draw(buffer);
        }
        for (int i = 0; i < tank2.getBulletList().size(); i++) {
            tank2.getBulletList().get(i).draw(buffer);
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

        int x = worldWidth / tileWidth;
        int y = worldHeight / tileHeight;

        for (int i = -1; i <= y; i++) {

            for (int j = 0; j <= x; j++) {
                buffer.drawImage(battleField, j * tileWidth,i * tileHeight + tileHeight,
                        tileWidth, tileHeight, this);
            }
        }
    }

    public void mapMaker() {

        int j = 0;
        try {
            while (input != null) {

                for (int i = 0; i < input.length(); i++ ) {

                    if (input.charAt(i) == '1') {
                        walls.add(new Wall(wall, i * wall.getWidth(null),
                                j * wall.getHeight(null), 1));
                    }
                    if (input.charAt(i) == '2') {
                        walls.add(new Wall(breakableWall, i * breakableWall.getWidth(null),
                                j * breakableWall.getHeight(null), 2));
                    }
                    else if (input.charAt(i) == '3') {
                        walls.add(new Wall(powerUp, i * powerUp.getWidth(null),
                                j * powerUp.getHeight(null), 3));
                    }
                }
                j++;
                input = bufferReader.readLine();
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void drawWall() {

        if (!walls.isEmpty()) {

            for (int i = 0; i <= walls.size()-1; i++) {
                walls.get(i).draw(buffer);
            }
        }
    }

    public void checkCollision() {

        tank1Rec = tank1.getRectangle();
        tank2Rec = tank2.getRectangle();

        // Checks to see if player 1 collides with player 2
        if (tank1Rec.intersects(tank2Rec)) {

            tank1.setCollides(true);
            tank1.handleCollision();
        }
        if (tank2Rec.intersects(tank1Rec)) {

            tank2.setCollides(true);
            tank2.handleCollision();
        }
        // Checks to see if the Tanks collide with the walls and the health power up
        for (int i = 0; i <= walls.size()-1; i++) {
            wallRec = walls.get(i).getRectangle();

            if (tank1Rec.intersects(wallRec) || tank2Rec.intersects(wallRec)) {

                if ((walls.get(i).getType() == 3 && (tank2Rec.intersects(wallRec))))
                {
                    if(!(tank2.getHealth() == 5)){

                        walls.remove(i);
                        tank2.setHealth(5);
                    }
                }
                else if ((walls.get(i).getType() == 3 && (tank1Rec.intersects(wallRec))))
                {
                    if(!(tank1.getHealth() == 5)){

                        walls.remove(i);
                        tank1.setHealth(5);
                    }
                }
                else {

                tank1.setCollides(true);
                tank1.handleCollision();
                tank2.setCollides(true);
                tank2.handleCollision();
                }
            }
            /* Checks to see if the bullets intersect with the breakable walls and
             * removes the bullet and the breakable wall */
            for (int j = 0; j < tank1.getBulletList().size(); j++) {
                bulletRec = tank1.getBulletList().get(j).getRectangle();

                if ((wallRec.intersects(bulletRec))&& !(walls.get(i).getType() == 3)) {
                    tank1.getBulletList().remove(j);

                    if (walls.get(i).getType() == 2)
                        walls.remove(i);
                }
            }
            for (int j = 0; j < tank2.getBulletList().size(); j++) {
                bulletRec = tank2.getBulletList().get(j).getRectangle();

                if ((wallRec.intersects(bulletRec))&& !(walls.get(i).getType() == 3)) {
                    tank2.getBulletList().remove(j);

                    if (walls.get(i).getType() == 2)
                        walls.remove(i);
                }
            }
        }
        /* Checks to see if the bullets collides with the other tank and removes it, and decreases
         * the health. It also respawns the tanks to their original positions after one tank losses all
         * of their health */
        for (int i = 0; i < tank1.getBulletList().size(); i++) {
            bulletRec = tank1.getBulletList().get(i).getRectangle();

            if (tank2Rec.intersects(bulletRec)) {

                tank1.getBulletList().remove(i);
                tank2.setHealth(tank2.getHealth()-1);

                if (tank2.getHealth() == 0) {
                    tank2.setLives(tank2.getLives()-1);

                    if (!(tank2.getLives() == -1)) {
                        tank2.Respawn(5);
                        tank1.Respawn(tank1.getHealth());
                    }
                }
            }
        }
        for (int i = 0; i < tank2.getBulletList().size(); i++) {
            bulletRec = tank2.getBulletList().get(i).getRectangle();

            if (tank1Rec.intersects(bulletRec)){

                tank2.getBulletList().remove(i);
                tank1.setHealth(tank1.getHealth()-1);

                if (tank1.getHealth() == 0) {
                    tank1.setLives(tank1.getLives()-1);

                    if (!(tank1.getLives() == -1)) {

                        tank1.Respawn(5);
                        tank2.Respawn(tank2.getHealth());
                    }
                }
            }
        }
    }
    /* centers the tank's screens when the tanks move around and stops the screen
    *  when the the screen would go out of bounds of the game world */
    private int getScreenBoundsX(Tank tank) {

        if (tank.getTankCenterX() + screenWidth/4 <= worldWidth) {
            screenBoundsX = tank.getTankCenterX()-screenWidth/4;
        }
        if (tank.getTankCenterX() + screenWidth/4 >= worldWidth) {
            screenBoundsX = (worldWidth-screenWidth/2);
        }
        if (tank.getTankCenterX() <= screenWidth/4) {
            screenBoundsX = 0;
        }
        return screenBoundsX;
    }

    private int getScreenBoundsY(Tank tank) {

        if (tank.getTankCenterY() + screenHeight/2 <= worldHeight) {
            screenBoundsY = tank.getTankCenterY()-screenHeight/2;
        }
        if (tank.getTankCenterY() + screenHeight/2 >= worldHeight) {
            screenBoundsY = worldHeight-screenHeight;
        }
        if (tank.getTankCenterY() <= screenHeight/2) {
            screenBoundsY = 0;
        }
        return screenBoundsY;
    }
    public static void main(String[] args) {

        TankGameWorld TGW = new TankGameWorld();
        TGW.init();

        try {
            while (!TGW.isEndGame()) {

                tank1.update();
                tank2.update();
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

