package Model;

import javax.swing.*;
import java.awt.*;

public class GameObject {
    private String name;
    private ImageIcon icon;
    private int x, y;

    public GameObject(String name, ImageIcon icon, int x, int y) {
        this.name = name;
        this.icon = icon;
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g, Component observer) {
        g.drawImage(icon.getImage(), x, y, 32, 32, observer); // redimensiona a 32x32
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 32);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public String getName() { return name; }
}
