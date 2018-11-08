import java.awt.*;

public class Wall extends GameObject {

    public Wall(Image img, int x, int y) {

        super(img, x, y);
    }

    public void draw(Graphics2D g){

        g.drawImage(img, x, y, null);
        //update();
    }
}


