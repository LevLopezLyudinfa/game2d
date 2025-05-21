package Model;

import javax.swing.*;

public abstract class Character {
    protected int lives;
    protected int speed;
    protected int x;
    protected int y;
    protected String name;

    public Character() {
        this.x = 0;
        this.y = 0;
    }

    public int getSpeed() {
        return speed;
    }

    public abstract ImageIcon getCurrentSprite();

    public void setDirection(String direction) {
    }

    public void move(int deltaX, int deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void addLife() {
        this.lives++;
    }

    public void loseLife() {
        this.lives--;
    }
    public boolean isWarrior() {
        return false;
    }


}