import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import Model.Mage;
import Model.Warrior;
import Model.Priest;
import Model.Enemies;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
    private Timer timer;
    private Model.Character character;
    private int playerX, playerY;
    private ArrayList<Enemies> enemies;
    private ImageIcon heartIcon;
    private long startTime;

    private class GameObject {
        int x, y;
        ImageIcon icon;
        boolean collected;
        String type;

        public GameObject(int x, int y, ImageIcon icon, String type) {
            this.x = x;
            this.y = y;
            this.icon = icon;
            this.type = type;
            this.collected = false;
        }
    }

    private ArrayList<GameObject> objects;
    BufferedImage tileImage;
    BufferedImage tileWall;
    private int coins = 0;
    private ImageIcon dollarIcon, mitraIcon, potionIcon, swordIcon;

    public GamePanel(Model.Character character) {
        this.character = character;
        this.playerX = 100;
        this.playerY = 100;
        this.startTime = System.currentTimeMillis();

        setFocusable(true);
        addKeyListener(this);

        dollarIcon = new ImageIcon("src/imatges_joc_rol/images/dungeon/dollar.png");
        mitraIcon = new ImageIcon("src/imatges_joc_rol/images/dungeon/mitra.png");
        potionIcon = new ImageIcon("src/imatges_joc_rol/images/dungeon/potion.png");
        swordIcon = new ImageIcon("src/imatges_joc_rol/images/dungeon/sword.png");
        heartIcon = new ImageIcon("src/imatges_joc_rol/images/dungeon/heart.png");

        dollarIcon = new ImageIcon(dollarIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        mitraIcon = new ImageIcon(mitraIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        potionIcon = new ImageIcon(potionIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        swordIcon = new ImageIcon(swordIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        heartIcon = new ImageIcon(heartIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));

        initObjects();

        enemies = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            enemies.add(new Enemies());
        }

        timer = new Timer(16, this);
        timer.start();
    }

    private void initObjects() {
        objects = new ArrayList<>();
        int coinCount = 5;
        if (character instanceof Warrior) coinCount = 0;
        for (int i = 0; i < coinCount; i++) {
            objects.add(new GameObject(randomX(), randomY(), dollarIcon, "dollar"));
        }

        if (character instanceof Mage) {
            objects.add(new GameObject(randomX(), randomY(), potionIcon, "potion"));
        } else if (character instanceof Warrior) {
            objects.add(new GameObject(randomX(), randomY(), swordIcon, "sword"));
        } else if (character instanceof Priest) {
            objects.add(new GameObject(randomX(), randomY(), mitraIcon, "mitra"));
        }
    }

    private int randomX() {
        int margin = 50;
        int maxX = 800;
        return margin + (int)(Math.random() * (maxX - margin));
    }

    private int randomY() {
        int margin = 50;
        int maxY = 600;
        return margin + (int)(Math.random() * (maxY - margin));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);

        try {
            if (tileImage == null) tileImage = ImageIO.read(new File("src/imatges_joc_rol/images/dungeon/tile001.png"));
            if (tileWall == null) tileWall = ImageIO.read(new File("src/imatges_joc_rol/images/dungeon/tile004.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int tileWidth = tileImage.getWidth();
        int tileHeight = tileImage.getHeight();
        int cols = (int) Math.ceil((double) getWidth() / tileWidth);
        int rows = (int) Math.ceil((double) getHeight() / tileHeight);

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                g.drawImage(tileImage, x * tileWidth, y * tileHeight, null);
            }
        }

        g.drawImage(character.getCurrentSprite().getImage(), playerX, playerY, this);

        for (Enemies enemy : enemies) {
            g.drawImage(enemy.getCurrentSprite().getImage(), enemy.getX(), enemy.getY(), this);
        }

        for (GameObject obj : objects) {
            if (!obj.collected) {
                g.drawImage(obj.icon.getImage(), obj.x, obj.y, this);
            }
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Coins: " + coins, 20, 30);
        g.drawString("Lives: " + character.getLives(), 20, 60);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        checkObjectCollision();
        checkEnemyCollision();
        repaint();
    }

    private void checkObjectCollision() {
        Rectangle playerRect = new Rectangle(playerX, playerY, character.getCurrentSprite().getIconWidth(), character.getCurrentSprite().getIconHeight());
        for (GameObject obj : objects) {
            if (!obj.collected) {
                Rectangle objRect = new Rectangle(obj.x, obj.y, obj.icon.getIconWidth(), obj.icon.getIconHeight());
                if (playerRect.intersects(objRect)) {
                    obj.collected = true;
                    switch (obj.type) {
                        case "dollar":
                            coins += 10;
                            if ((character instanceof Mage || character instanceof Priest) && coins >= 50) {
                                guardarDatosYReiniciar(true);
                                return;
                            }
                            break;
                        case "potion":
                            if (character instanceof Mage) character.addLife();
                            break;
                        case "mitra":
                            if (character instanceof Priest) {
                                enemies.clear();
                                repaint();
                            }
                            break;
                    }
                }
            }
        }
    }

    private void checkEnemyCollision() {
        Rectangle playerRect = new Rectangle(playerX, playerY, character.getCurrentSprite().getIconWidth(), character.getCurrentSprite().getIconHeight());
        for (int i = 0; i < enemies.size(); i++) {
            Enemies enemy = enemies.get(i);
            Rectangle enemyRect = new Rectangle(enemy.getX(), enemy.getY(), 32, 32);
            if (playerRect.intersects(enemyRect)) {
                boolean hasSword = isObjectCollected("sword");
                boolean hasMitra = isObjectCollected("mitra");

                if (character instanceof Warrior && hasSword) {
                    enemies.remove(i);
                    coins += 10;
                    removeCollected("sword");
                    if (enemies.isEmpty()) {
                        guardarDatosYReiniciar(true);
                        return;
                    }
                } else if (character instanceof Priest && hasMitra) {
                    resetPlayerPosition();
                } else {
                    character.loseLife();
                    if (character.getLives() <= 0) {
                        guardarDatosYReiniciar(false);
                        return;
                    } else {
                        resetPlayerPosition();
                    }
                }
            }
        }
    }

    private void guardarDatosYReiniciar(boolean victoria) {
        long endTime = System.currentTimeMillis();
        int duration = (int)((endTime - startTime) / 1000);

        String nombre = JOptionPane.showInputDialog(this, "Introduce tu nombre:");
        if (nombre == null || nombre.trim().isEmpty()) nombre = "Desconocido";

        String tipo = character.getClass().getSimpleName();
        int vidasRestantes = character.getLives();

        DatabaseManager.insertarPartida(nombre, tipo, vidasRestantes, duration);

        String msg = victoria ? "\u00a1Has ganado!" : "\u00a1Has perdido!";
        JOptionPane.showMessageDialog(this, msg, "Fin de la partida", JOptionPane.INFORMATION_MESSAGE);
        restartGame();
    }

    private boolean isObjectCollected(String type) {
        for (GameObject obj : objects) {
            if (obj.type.equals(type) && obj.collected) return true;
        }
        return false;
    }

    private void removeCollected(String type) {
        for (GameObject obj : objects) {
            if (obj.type.equals(type)) {
                obj.collected = false;
                obj.x = randomX();
                obj.y = randomY();
            }
        }
    }

    private void restartGame() {
        playerX = 100;
        playerY = 100;
        coins = 0;
        startTime = System.currentTimeMillis();

        if (character instanceof Mage) character.setLives(3);
        else if (character instanceof Priest) character.setLives(4);
        else if (character instanceof Warrior) character.setLives(5);

        initObjects();

        enemies.clear();
        for (int i = 0; i < 5; i++) {
            enemies.add(new Enemies());
        }
        repaint();
    }

    private void resetPlayerPosition() {
        playerX = 100;
        playerY = 100;
    }

    @Override public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> { playerY -= character.getSpeed(); character.setDirection("up"); }
            case KeyEvent.VK_DOWN -> { playerY += character.getSpeed(); character.setDirection("down"); }
            case KeyEvent.VK_LEFT -> { playerX -= character.getSpeed(); character.setDirection("left"); }
            case KeyEvent.VK_RIGHT -> { playerX += character.getSpeed(); character.setDirection("right"); }
        }
        playerX = Math.max(0, Math.min(playerX, getWidth() - character.getCurrentSprite().getIconWidth()));
        playerY = Math.max(0, Math.min(playerY, getHeight() - character.getCurrentSprite().getIconHeight()));
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
