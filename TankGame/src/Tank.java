import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Tank extends GameObject {

    private int moveXDirection, moveYDirection, angle;
    private int tankSpeed, rotationSpeed;
    private BufferedImage img;
    private Image bulletImg;
    private ArrayList<Bullet> BulletList;
    private boolean UpPressed, DownPressed, RightPressed, LeftPressed, ShootPressed;
    private double time, lastAttack, bulletDelayTime;
    private int saveX, saveY, respawnX, respawnY, respawnAngle;
    private boolean collides;
    private int health, lives;

    public Tank(BufferedImage img, int x, int y, int angle) {

        super(img, x, y);
        this.img = img;
        this.angle = angle;
        this.BulletList = new ArrayList<>();
        this.tankSpeed = 2;
        this.rotationSpeed = 2;
        this.health = 5;
        this.lives = 2;
        this.lastAttack = 0;
        respawnX = x;
        respawnY = y;
        respawnAngle = angle;

        try {
            bulletImg = ImageIO.read(new File("Resources/Shell.gif"));
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

        if(health == 1)
            return bulletDelayTime = 200;
        else
            return bulletDelayTime = 400;
    }

    // Method that shoots the bullets. A delay is needed for the bullets to not shoot too fast
    private void Shoot() {

        time = System.currentTimeMillis();
        if (time > lastAttack + getBulletDelayTime()) {

            BulletList.add(new Bullet(bulletImg, angle,getTankCenterX(),getTankCenterY()));
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
        if (x >= TankGameWorld.worldWidth - 91) {
            x = TankGameWorld.worldWidth - 91;
        }
        if (y < 33) {
            y = 33;
        }
        if (y >= TankGameWorld.worldHeight - 91) {
            y = TankGameWorld.worldHeight - 91;
        }
    }

    public int getTankCenterX() {
        return x + (img.getWidth()/2);
    }

    public int getTankCenterY() {
        return y + (img.getWidth()/2);
    }

    public  Rectangle getTankRectangle () {
        return new Rectangle(x, y, width, height);
    }

    // Draws the Tanks
    public void draw(Graphics2D g) {

        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() /2.0, this.img.getHeight() /2.0);
        g.drawImage(this.img, rotation, null);
    }
}
