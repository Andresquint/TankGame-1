import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class GameObject {

    protected double x, y, height, width;
    protected Image img;

    public GameObject(Image img, double x, double y) {

        this.img = img;
        this.x = x;
        this.y = y;
        this.height = img.getHeight(null);
        this.width = img.getWidth(null);
    }

    public abstract Rectangle2D.Double getRectangle ();
}