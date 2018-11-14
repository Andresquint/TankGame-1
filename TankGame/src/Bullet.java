import java.awt.*;
import java.awt.geom.AffineTransform;

public class Bullet extends GameObject{

    private int x, y, moveXDirection, moveYDirection, bulletSpeed;
    private Image img;
    private int angle;

    public Bullet(Image img, int angle, int x, int y){

        super(img, x, y);
        this.img = img;
        this.x = x;
        this.y = y;
        this.angle = angle;
        bulletSpeed = 8;
        moveXDirection = (int) Math.round(bulletSpeed * Math.cos(Math.toRadians(angle)));
        moveYDirection = (int) Math.round(bulletSpeed * Math.sin(Math.toRadians(angle)));
    }

    public boolean update(){

        x += moveXDirection;
        y += moveYDirection;

        if(x < -bulletSpeed || x > TankGameWorld.worldWidth + bulletSpeed ||
        y < -bulletSpeed || y > TankGameWorld.worldHeight + bulletSpeed){
            return true;
        }
        return false;
        }

    public  Rectangle getBulletRectangle (){

        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g, TankGameWorld tankGameWorld) {

        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), 0, 0);
        g.drawImage(this.img, rotation, null);

    }
}
