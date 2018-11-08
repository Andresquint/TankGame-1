import java.awt.*;

public abstract class GameObject {

    protected int x, y, speed, height, width;
    Rectangle bbox;
    protected boolean dead;
    protected Image[] imgs = new Image[3];
    protected Image img;


    public GameObject(Image img, int x, int y) {

        this.img = img;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.height = img.getHeight(null);
        this.width = img.getWidth(null);
    }
}