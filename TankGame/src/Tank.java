import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Tank extends GameObject {

    private int moveXDirection, moveYDirection, angle;
    private int tankSpeed, rotationSpeed = 2;
    private static BufferedImage img;
    private Image bulletImg;
    private ArrayList<Bullet> BulletList;
    private boolean UpPressed, DownPressed, RightPressed, LeftPressed, ShootPressed;
    private double time, lastAttack = 0, bulletDelayTime;
    private int saveX, saveY, respawnX, respawnY, respawnAngle;
    private boolean collides;
    private int health;
    private TankGameWorld TGW;
    private Tank tank1, tank2;

    Tank(int speed, int x, int y, int angle, BufferedImage img) {

        super(img, x, y);
        this.img = img;
        this.angle = angle;
        this.BulletList = new ArrayList<>();
        this.tankSpeed = speed;
        this.health = 2;
        respawnX = x;
        respawnY = y;
        respawnAngle = angle;

        try {
            bulletImg = ImageIO.read(new File("Resources/Shell 2.gif"));
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
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

    void toggleShootPressed() {
        this.ShootPressed = true;
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

    void untoggleShootPressed() {
        this.ShootPressed = false;
    }

    public ArrayList<Bullet> getBulletList(){
       return this.BulletList;
    }

    public void update() {

       if (!collides) {
           saveCordinates();
        }
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
       if (this.ShootPressed) {
           Shoot();
       }
    }

    private void rotateLeft() {
        this.angle -= this.rotationSpeed;
    }

    private void rotateRight() {
        this.angle += this.rotationSpeed;
    }

    private void moveBackwards() {

        moveXDirection = (int) Math.round(tankSpeed * Math.cos(Math.toRadians(angle)));
        moveYDirection = (int) Math.round(tankSpeed * Math.sin(Math.toRadians(angle)));
        x -= moveXDirection;
        y -= moveYDirection;
        checkBorder();
    }

    private void moveForwards() {

        moveXDirection = (int) Math.round(tankSpeed * Math.cos(Math.toRadians(angle)));
        moveYDirection = (int) Math.round(tankSpeed * Math.sin(Math.toRadians(angle)));
        x += moveXDirection;
        y += moveYDirection;
        checkBorder();
    }

    public double getBulletDelayTime(){

        if(health == 0)
        return bulletDelayTime = 100;
        else
            return bulletDelayTime = 500;
    }

    private void Shoot() {

        time = System.currentTimeMillis();
        if (time > lastAttack + getBulletDelayTime()) {

            BulletList.add(new Bullet(bulletImg, angle,getTankCenterX(),getTankCenterY()));
            lastAttack = time;
        }
    }

    public void saveCordinates() {

        saveX = x;
        saveY = y;
    }

    public void setCollides(boolean collides) {
        this.collides = collides;
    }

    public void handleCollision(){

        if (collides){
            this.x = saveX;
            this.y = saveY;
            collides = false;
        }
    }

    public void setHealth(int health){
        this.health = health;
    }

    public int getHealth() {
        return this.health;
    }

    public void Respawn() {

        if (health == -1) {

            x = respawnX;
            y = respawnY;
            angle = respawnAngle;

            health = 2;
        }
    }
    public void Respawn2(Tank tank) {

            tank.x = respawnX;
            tank.y = respawnY;
            angle = respawnAngle;
    }
    private void checkBorder() {

        if (x < 30) {
            x = 30;
        }
        if (x >= TankGameWorld.worldWidth - 88) {
            x = TankGameWorld.worldWidth - 88;
        }
        if (y < 40) {
            y = 40;
        }
        if (y >= TankGameWorld.worldHeight - 80) {
            y = TankGameWorld.worldHeight - 80;
        }
    }

    public int getTankCenterX() {

        return x + img.getWidth() / 2;
    }

    public int getTankCenterY() {

        return y + img.getWidth() / 2;
    }

    public  Rectangle getTankRectangle (){

        return new Rectangle(x, y, width, height);
    }

    public void powerUp() {

        if (health == 0) {
            bulletDelayTime = 100;
        }
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
