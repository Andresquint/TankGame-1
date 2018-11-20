import java.awt.*;

public abstract class GameObject {

    protected  int x;
    protected int y;
    protected int height;
    protected int width;
    protected Image img;

    public GameObject(Image img, int x, int y) {

        this.img = img;
        this.x = x;
        this.y = y;
        this.height = img.getHeight(null);
        this.width = img.getWidth(null);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}