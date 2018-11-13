import java.awt.*;

public class Wall extends GameObject {

    private int height;
    private int width;
    private int x;
    private int y;
    private int wallType;


    public Wall(Image img, int x, int y, int type) {

        super(img, x, y);
        this.x = x;
        this.y = y;
        this.wallType = type;
        this.width = img.getWidth(null);
        this.height = img.getHeight(null);
    }

    public int getType() {
        return this.wallType;
    }

    public Rectangle getRectangle () {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g){
        g.drawImage(img, x, y, null);
    }
}


