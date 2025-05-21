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


    // Objetos a recoger
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

    // UI monedas
    private int coins = 0;
    private ImageIcon dollarIcon, mitraIcon, potionIcon, swordIcon;

    public GamePanel(Model.Character character) {
        this.character = character;
        this.playerX = 100;
        this.playerY = 100;

        setFocusable(true);
        addKeyListener(this);

        // Cargar imágenes y escalar a 32x32
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

        // Crear objetos en posiciones aleatorias
        objects = new ArrayList<>();
        objects.add(new GameObject(randomX(), randomY(), dollarIcon, "dollar"));
        objects.add(new GameObject(randomX(), randomY(), potionIcon, "potion"));
        objects.add(new GameObject(randomX(), randomY(), swordIcon, "sword"));
        objects.add(new GameObject(randomX(), randomY(), mitraIcon, "mitra"));

        // Crear enemigos
        enemies = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            enemies.add(new Enemies());
        }

        timer = new Timer(16, this);  // ~60 FPS
        timer.start();
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

        // Cargar tiles (solo la primera vez)
        if (tileImage == null || tileWall == null) {
            try {
                tileImage = ImageIO.read(new File("src/imatges_joc_rol/images/dungeon/tile001.png"));
                tileWall = ImageIO.read(new File("src/imatges_joc_rol/images/dungeon/tile004.png"));
            } catch (IOException | IllegalArgumentException e) {
                tileImage = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = tileImage.createGraphics();
                g2.setColor(Color.RED);
                g2.fillRect(0, 0, 32, 32);
                g2.dispose();

                tileWall = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
            }
        }

        int tileWidth = tileImage.getWidth();
        int tileHeight = tileImage.getHeight();
        int cols = (int) Math.ceil((double) getWidth() / tileWidth);
        int rows = (int) Math.ceil((double) getHeight() / tileHeight);

        int tileWallWidth = tileWall.getWidth();
        int tileWallHeight = tileWall.getHeight();
        int colsWall = (int) Math.ceil((double) getWidth() / tileWallWidth);
        int rowsWall = 1;

        // Dibujar fondo
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                g.drawImage(tileImage, x * tileWidth, y * tileHeight, null);
            }
        }

        // Dibujar muros arriba y abajo
        for (int x = 0; x < colsWall; x++) {
            g.drawImage(tileWall, x * tileWallWidth, 0, null);
            g.drawImage(tileWall, x * tileWallWidth, (rows - 1) * tileWallHeight, null);
        }

        // Dibujar jugador
        g.drawImage(character.getCurrentSprite().getImage(), playerX, playerY, this);

        // Dibujar enemigos
        for (Enemies enemy : enemies) {
            g.drawImage(enemy.getCurrentSprite().getImage(), enemy.getX(), enemy.getY(), this);
        }

        // Dibujar objetos si no han sido recogidos
        for (GameObject obj : objects) {
            if (!obj.collected) {
                g.drawImage(obj.icon.getImage(), obj.x, obj.y, this);
            }
        }

        // Dibujar UI (monedas y corazones) arriba a la derecha
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        int padding = 10;
        int iconSize = 32;

        // MONEDAS
        int coinX = getWidth() - 100;
        int coinY = padding + iconSize;
        g.drawString(coins + "x", coinX + iconSize + 5, coinY - 8);
        g.drawImage(dollarIcon.getImage(), coinX, padding, iconSize, iconSize, this);

        // VIDAS (corazones)
        int heartX = getWidth() - 100;
        int heartY = padding + iconSize + 10 + iconSize;
        g.drawString(character.getLives() + "x", heartX + iconSize + 5, heartY + 24 - 8);
        g.drawImage(heartIcon.getImage(), heartX, heartY, iconSize, iconSize, this);
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
                    if (obj.type.equals("dollar")) {
                        coins += 10;  // 10 coins por moneda
                    }
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                playerY -= character.getSpeed();
                character.setDirection("up");
                break;
            case KeyEvent.VK_DOWN:
                playerY += character.getSpeed();
                character.setDirection("down");
                break;
            case KeyEvent.VK_LEFT:
                playerX -= character.getSpeed();
                character.setDirection("left");
                break;
            case KeyEvent.VK_RIGHT:
                playerX += character.getSpeed();
                character.setDirection("right");
                break;
        }
        // Evitar que el jugador salga fuera del panel
        playerX = Math.max(0, Math.min(playerX, getWidth() - character.getCurrentSprite().getIconWidth()));
        playerY = Math.max(0, Math.min(playerY, getHeight() - character.getCurrentSprite().getIconHeight()));
    }
    private void checkEnemyCollision() {
        Rectangle playerRect = new Rectangle(playerX, playerY,
                character.getCurrentSprite().getIconWidth(), character.getCurrentSprite().getIconHeight());

        for (int i = 0; i < enemies.size(); i++) {
            Enemies enemy = enemies.get(i);
            Rectangle enemyRect = new Rectangle(enemy.getX(), enemy.getY(), 32, 32);

            if (playerRect.intersects(enemyRect)) {
                boolean hasPotion = isObjectCollected("potion");
                boolean hasSword = isObjectCollected("sword");
                boolean hasMitra = isObjectCollected("mitra");

                if (character instanceof Mage && hasPotion) {
                    character.addLife();
                    resetPlayerPosition();
                    removeCollected("potion");
                    break;
                } else if (character instanceof Warrior && hasSword) {
                    enemies.remove(i);
                    coins += 10;
                    removeCollected("sword");
                    break;
                } else if (character instanceof Priest && hasMitra) {
                    resetPlayerPosition();
                    removeCollected("mitra");
                    break;
                } else {
                    character.loseLife();
                    if (character.getLives() <= 0) {
                        restartGame();
                    } else {
                        resetPlayerPosition();
                    }
                    break;
                }
            }
        }
    }
    private void restartGame() {
        // Reiniciar posición
        playerX = 100;
        playerY = 100;

        // Reiniciar vidas según tipo
        if (character instanceof Mage) {
            character.setLives(3);
        } else if (character instanceof Warrior) {
            character.setLives(5);
        } else if (character instanceof Priest) {
            character.setLives(4);
        }

        // Reiniciar monedas
        coins = 0;

        // Reiniciar objetos
        objects.clear();
        objects.add(new GameObject(randomX(), randomY(), dollarIcon, "dollar"));
        objects.add(new GameObject(randomX(), randomY(), potionIcon, "potion"));
        objects.add(new GameObject(randomX(), randomY(), swordIcon, "sword"));
        objects.add(new GameObject(randomX(), randomY(), mitraIcon, "mitra"));

        // Reiniciar enemigos
        enemies.clear();
        for (int i = 0; i < 5; i++) {
            enemies.add(new Enemies());
        }

        // Dirección inicial
        character.setDirection("down");

        // Repaint inmediato
        repaint();
    }

    private void resetPlayerPosition() {
        playerX = 100;
        playerY = 100;
    }

    private boolean isObjectCollected(String type) {
        for (GameObject obj : objects) {
            if (obj.type.equals(type) && obj.collected) {
                return true;
            }
        }
        return false;
    }

    private void removeCollected(String type) {
        for (GameObject obj : objects) {
            if (obj.type.equals(type)) {
                obj.collected = false;
                obj.x = randomX();  // opcional: regenerar a una nova posició
                obj.y = randomY();
            }
        }
    }


    @Override
    public void keyReleased(KeyEvent e) { }
    @Override
    public void keyTyped(KeyEvent e) { }
}
