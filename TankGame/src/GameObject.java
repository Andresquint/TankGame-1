import java.awt.*;

public abstract class GameObject {

    protected  int x, y, height, width;
    protected Image img;

    public GameObject(Image img, int x, int y) {

        this.img = img;
        this.x = x;
        this.y = y;
        this.height = img.getHeight(null);
        this.width = img.getWidth(null);
    }
}