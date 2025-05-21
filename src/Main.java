import Model.Enemies;
import Model.Mage;
import Model.Warrior;
import Model.Priest;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("2D Game");
        Model.Character character = new Warrior();
        GamePanel gamePanel = new GamePanel(character);
        frame.add(gamePanel);
        frame.setSize(900, 700);


        frame.setContentPane(gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
//juego
    }
}