package Model;

import javax.swing.*;
import java.awt.*;

public class Enemies extends Character {
    private ImageIcon currentSprite;
    private int direction;
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final int IntervalTime = 50;

    // Sprites del esqueleto
    ImageIcon skeleton_down = new ImageIcon(new ImageIcon("src/imatges_joc_rol/images/skeleton/skeleton_down.gif").getImage().getScaledInstance(32,32, Image.SCALE_DEFAULT));
    ImageIcon skeleton_left = new ImageIcon(new ImageIcon("src/imatges_joc_rol/images/skeleton/skeleton_left.gif").getImage().getScaledInstance(32,32, Image.SCALE_DEFAULT));
    ImageIcon skeleton_right = new ImageIcon(new ImageIcon("src/imatges_joc_rol/images/skeleton/skeleton_right.gif").getImage().getScaledInstance(32,32, Image.SCALE_DEFAULT));
    ImageIcon skeleton_up = new ImageIcon(new ImageIcon("src/imatges_joc_rol/images/skeleton/skeleton_up.gif").getImage().getScaledInstance(32,32, Image.SCALE_DEFAULT));

    public Enemies() {
        this.speed = 5;
        this.lives = 1;
        this.name = "Skeleton";
        this.x = (int) (Math.random() * 600);
        this.y = (int) (Math.random() * 400);
        this.direction = (int) (Math.random() * 4); // 0=derecha, 1=abajo, 2=izquierda, 3=arriba
        currentSprite = skeleton_down;

        // Timer que se repite cada 50 milisegundos
        Timer timer = new Timer(IntervalTime, e -> move());
        timer.start();
    }

    @Override
    public ImageIcon getCurrentSprite() {
        return currentSprite;
    }

    @Override
    public void setDirection(String direction) {
        // No usamos esto para enemigos, pero está obligado por el abstract
    }

    private void move() {
        switch (direction) {
            case 0: // derecha
                x += speed;
                currentSprite = skeleton_right;
                if (x > WIDTH) {
                    x = WIDTH;
                    direction = 2; // cambia a izquierda
                }
                break;
            case 1: // abajo
                y += speed;
                currentSprite = skeleton_down;
                if (y > HEIGHT) {
                    y = HEIGHT;
                    direction = 3; // cambia a arriba
                }
                break;
            case 2: // izquierda
                x -= speed;
                currentSprite = skeleton_left;
                if (x < 0) {
                    x = 0;
                    direction = 0; // cambia a derecha
                }
                break;
            case 3: // arriba
                y -= speed;
                currentSprite = skeleton_up;
                if (y < 0) {
                    y = 0;
                    direction = 1; // cambia a abajo
                }
                break;
        }
    }

    // Getter y setter para la posición
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
