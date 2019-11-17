import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class Wall extends GameObject {

    private int wallType;

    public Wall(Image img, double x, double y, int type) {

        super(img, x, y);
        this.wallType = type;
    }

    public int getType() {

        return this.wallType;
    }

    public Rectangle2D.Double getRectangle () {

        return new Rectangle2D.Double(x, y, width, height);
    }

    public void draw(Graphics2D g) {

        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        g.drawImage(this.img, rotation, null);
    }
}


