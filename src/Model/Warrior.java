package Model;

import javax.swing.*;

public class Warrior extends Character {
    private ImageIcon currentSprite;
    private String direction;

    public Warrior() {
        this.lives = 5;
        this.speed = 3;
        this.direction = "down"; // Dirección inicial
        currentSprite = new ImageIcon("src/imatges_joc_rol/images/warrior/warrior_down.gif");
    }

    @Override
    public ImageIcon getCurrentSprite() {
        return currentSprite;
    }

    public void setDirection(String direction) {
        this.direction = direction;
        switch (direction) {
            case "down":
                currentSprite = new ImageIcon("src/imatges_joc_rol/images/warrior/warrior_down.gif");
                break;
            case "up":
                currentSprite = new ImageIcon("src/imatges_joc_rol/images/warrior/warrior_up.gif");
                break;
            case "left":
                currentSprite = new ImageIcon("src/imatges_joc_rol/images/warrior/warrior_left.gif");
                break;
            case "right":
                currentSprite = new ImageIcon("src/imatges_joc_rol/images/warrior/warrior_right.gif");
                break;
        }
    }
    @Override
    public boolean isWarrior() {
        return true;
    }

}