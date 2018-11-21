import java.awt.*;

public class Wall extends GameObject {

    private int wallType;

    public Wall(Image img, int x, int y, int type) {

        super(img, x, y);
        this.wallType = type;
    }

    public int getType() {
        return this.wallType;
    }

    public Rectangle getRectangle () {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g) {
        g.drawImage(this.img, x, y, null);
    }
}


