package Model;

import javax.swing.*;

public class Priest extends Character {
    private ImageIcon currentSprite;
    private String direction;

    public Priest() {
        this.lives = 4;
        this.speed = 5;
        this.direction = "down"; // Dirección inicial
        currentSprite = new ImageIcon("src/imatges_joc_rol/images/priest/priest_down.gif");
    }

    @Override
    public ImageIcon getCurrentSprite() {
        return currentSprite;
    }

    public void setDirection(String direction) {
        this.direction = direction;
        switch (direction) {
            case "down":
                currentSprite = new ImageIcon("src/imatges_joc_rol/images/priest/priest_down.gif");
                break;
            case "up":
                currentSprite = new ImageIcon("src/imatges_joc_rol/images/priest/priest_up.gif");
                break;
            case "left":
                currentSprite = new ImageIcon("src/imatges_joc_rol/images/priest/priest_left.gif");
                break;
            case "right":
                currentSprite = new ImageIcon("src/imatges_joc_rol/images/priest/priest_right.gif");
                break;
        }
    }
}