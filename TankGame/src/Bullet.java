import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;

public class Bullet extends GameObject{

    private int damage;
    private boolean show;
    private int x, y, vx, vy, bulletSpeed;
    private Image img;
    private int angle;
    private Tank tank;

    public Bullet(Image img, int angle, int x, int y){

        super(img, x, y);
        this.img = img;
        this.x = x;
        this.y = y;
        this.angle = angle;
        bulletSpeed = 10;
        vx = (int) Math.round(bulletSpeed * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(bulletSpeed * Math.sin(Math.toRadians(angle)));
    }

    public boolean update(){

        x += vx;
        y += vy;

        if(x < -bulletSpeed || x > TankGameWorld.width + bulletSpeed ||
        y < -bulletSpeed || y > TankGameWorld.height + bulletSpeed){
            return true;
        }
        return false;
        }

    public  Rectangle getRectangle1 (){

        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g, TankGameWorld tankGameWorld) {

        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), 0, 0);
        g.drawImage(this.img, rotation, null);

    }
}
