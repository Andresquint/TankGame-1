import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Tank extends GameObject {

    private double moveXDirection, moveYDirection, angle;
    private double tankSpeed, rotationSpeed;
    private Image bulletImg, bulletImg2, bulletImg3;
    private ArrayList<Bullet> BulletList;
    private int type;
    private boolean UpPressed, DownPressed, RightPressed, LeftPressed, ShootPressed;
    private double time, lastAttack, bulletDelayTime;
    private double saveX, saveY, respawnX, respawnY, respawnAngle;
    private boolean collides;
    private int health, lives;
    private  boolean bulletPowerUp = false;

    public Tank(BufferedImage img, double x, double y, double angle, int type) {

        super(img, x, y);
        this.angle = angle;
        this.type = type;
        this.BulletList = new ArrayList<>();
        this.tankSpeed = 1.5;
        this.rotationSpeed = 1.2;
        this.health = 5;
        this.lives = 2;
        this.lastAttack = 0;
        respawnX = x;
        respawnY = y;
        respawnAngle = angle;

        try {
            bulletImg = ImageIO.read(new File("Resources/Shell.gif"));
            bulletImg2 = ImageIO.read(new File("Resources/Shell2.gif"));
            bulletImg3 = ImageIO.read(new File("Resources/Shell3.gif"));
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void toggleUpPressed() {
        this.UpPressed = true;
    }

    public void toggleDownPressed() {
        this.DownPressed = true;
    }

    public void toggleRightPressed() {
        this.RightPressed = true;
    }

    public void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    public void toggleShootPressed() {
        this.ShootPressed = true;
    }

    public void unToggleUpPressed() {
        this.UpPressed = false;
    }

    public void unToggleDownPressed() {
        this.DownPressed = false;
    }

    public void unToggleRightPressed() {
        this.RightPressed = false;
    }

    public void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    public void untoggleShootPressed() {
        this.ShootPressed = false;
    }

    public ArrayList<Bullet> getBulletList(){
       return this.BulletList;
    }

    public void update() {

       if (!collides) {
           saveCordinatesX();
           saveCordinatesY();
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

        moveXDirection = (tankSpeed * Math.cos(Math.toRadians(angle)));
        moveYDirection = (tankSpeed * Math.sin(Math.toRadians(angle)));
        x -= moveXDirection;
        y -= moveYDirection;
        checkBorder();
    }

    private void moveForwards() {

        moveXDirection = (tankSpeed * Math.cos(Math.toRadians(angle)));
        moveYDirection = (tankSpeed * Math.sin(Math.toRadians(angle)));
        x += moveXDirection;
        y += moveYDirection;
        checkBorder();
    }

    public void setBulletDelay(boolean bullet) {
        bulletPowerUp = bullet;
    }

    public boolean getBulletDelay() {
        return bulletPowerUp;
    }

    public double getBulletDelayTime(){


        if (bulletPowerUp == true) {
            return bulletDelayTime = 200;
        }
        else
            return bulletDelayTime = 400;
    }

    // Method that shoots the bullets. A delay is needed for the bullets to not shoot too fast
    private void Shoot() {

        time = System.currentTimeMillis();
        if (time > lastAttack + getBulletDelayTime()) {
            if (bulletPowerUp == false){
                    BulletList.add(new Bullet(bulletImg, angle, getTankCenterX(), getTankCenterY()));
            }
            if (bulletPowerUp == true){
                if (type == 1){
                    BulletList.add(new Bullet(bulletImg2, angle, getTankCenterX(), getTankCenterY()));
                }
                if (type == 2){
                    BulletList.add(new Bullet(bulletImg3, angle, getTankCenterX(), getTankCenterY()));
                }
            }
            lastAttack = time;
        }
    }

    public void saveCordinatesX() {
        saveX = x;
    }

    public void saveCordinatesY() {
        saveY = y;
    }

    public void setCollides(boolean collides) {
        this.collides = collides;
    }

    // handles the collision of the Tanks when it collides with a wall or the other Tank
    public void handleCollision() {

        if (collides){

            this.x = saveX;
            this.y = saveY;
            collides = false;
        }
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return this.health;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLives() {
        return this.lives;
    }

    public void Respawn(int health) {

            x = respawnX;
            y = respawnY;
            angle = respawnAngle;
            this.health = health;
    }
    // Makes sure the Tanks don't go beyond the border of the game
    private void checkBorder() {

        if (x < 33) {
            x = 33;
        }
        if (x >= TankGameWorld.worldWidth - 85) {
            x = TankGameWorld.worldWidth - 85;
        }
        if (y < 35) {
            y = 35;
        }
        if (y >= TankGameWorld.worldHeight - 80) {
            y = TankGameWorld.worldHeight - 80;
        }
    }

    public double getTankCenterX() {
        return x + (width/2);
    }

    public double getTankCenterY() {
        return y + (height/2);
    }

    public Rectangle2D.Double getRectangle () {
        return new Rectangle2D.Double(x, y, width, height);
    }

    // Draws the Tanks
    public void draw(Graphics2D g) {

        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), width/2, height/2);
        g.drawImage(this.img, rotation, null);
    }
}
