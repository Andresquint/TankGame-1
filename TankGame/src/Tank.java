import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Tank{

    private int x;
    private int y;
    private int vx;
    private int vy;
    private int angle;
    private final int tankSpeed = 1;
    private final int rotationSpeed = 1;
    private BufferedImage img;
    private Image bulletImg;
    private ArrayList<Bullet> myBulletList;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean FirePressed;
    private double time;
    private double lastAttack = 0;
    private double bulletDelayTime = 1000;

    Tank(int x, int y, int vx, int vy, int angle, BufferedImage img) {

        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
        this.myBulletList = new ArrayList<>();

        try {
            bulletImg = ImageIO.read(new File("Resources/Shell 2.gif"));
        } catch (Exception e) {
            System.out.print(e.getMessage() + "Player plane: no resources are found");
        }
    }

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void toggleFirePressed() {
        this.FirePressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void untoggleFirePressed() {
        this.FirePressed = false;
    }

    public ArrayList<Bullet> getBulletList(){
        return this.myBulletList;
    }

    public void update() {

        if (this.UpPressed) {
            this.moveForwards();
        }
        if (this.DownPressed) {
            this.moveBackwards();
        }
        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }
        if (this.FirePressed) {
            fire();
        }
    }

    private void rotateLeft() {
        this.angle -= this.rotationSpeed;
    }

    private void rotateRight() {
        this.angle += this.rotationSpeed;
    }

    private void moveBackwards() {

        vx = (int) Math.round(tankSpeed * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(tankSpeed * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        checkBorder();
    }

    private void moveForwards() {

        vx = (int) Math.round(tankSpeed * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(tankSpeed * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
    }

    private void fire() {

        time = System.currentTimeMillis();

        if (time > lastAttack + bulletDelayTime) {
            myBulletList.add(new Bullet(bulletImg, angle,x+16,y+16));
            lastAttack = time;
        }
    }

    private void checkBorder() {

        if (x < 30) {
            x = 30;
        }
        if (x >= TankGameWorld.width - 88) {
            x = TankGameWorld.width - 88;
        }
        if (y < 40) {
            y = 40;
        }
        if (y >= TankGameWorld.height - 80) {
            y = TankGameWorld.height - 80;
        }
    }

    public int getTankCenterX() {

        return x + img.getWidth(null) / 2;
    }

    public int getTankCenterY() {

        return y + img.getWidth(null) / 2;
    }

    @Override
    public String toString() {

        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }

    void draw(Graphics2D g) {

        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        g.drawImage(this.img, rotation, null);
    }
}
