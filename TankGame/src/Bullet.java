import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class Bullet extends GameObject{

    private double angle, moveXDirection, moveYDirection, bulletSpeed;

    public Bullet(Image img, double angle, double x, double y) {

        super(img, x, y);
        this.angle = angle;
        bulletSpeed = 8;
        moveXDirection = (int) Math.round(bulletSpeed * Math.cos(Math.toRadians(angle)));
        moveYDirection = (int) Math.round(bulletSpeed * Math.sin(Math.toRadians(angle)));
    }

    public void update() {

        x += moveXDirection;
        y += moveYDirection;
    }

    public Rectangle2D.Double getRectangle () {
        return new Rectangle2D.Double(x, y, width, height);
    }

    public void draw(Graphics2D g) {

        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), 0, 0);
        g.drawImage(this.img, rotation, null);
    }
}
