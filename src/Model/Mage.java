package Model;

import javax.swing.*;

public class Mage extends Character {
    private ImageIcon currentSprite;
    private String direction;

    public Mage() {
        this.lives = 3;
        this.speed = 7;
        this.direction = "down";
        currentSprite = new ImageIcon("src/imatges_joc_rol/images/wizard/wizard_down.gif");
    }

    @Override
    public ImageIcon getCurrentSprite() {
        return currentSprite;
    }

    public void setDirection(String direction) {
        this.direction = direction;
        switch (direction) {
            case "down":
                currentSprite = new ImageIcon("src/imatges_joc_rol/images/wizard/wizard_down.gif");
                break;
            case "up":
                currentSprite = new ImageIcon("src/imatges_joc_rol/images/wizard/wizard_up.gif");
                break;
            case "left":
                currentSprite = new ImageIcon("src/imatges_joc_rol/images/wizard/wizard_left.gif");
                break;
            case "right":
                currentSprite = new ImageIcon("src/imatges_joc_rol/images/wizard/wizard_right.gif");
                break;
        }
    }
}