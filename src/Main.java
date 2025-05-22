import javax.swing.*;
import Model.Character;
import Model.Mage;
import Model.Warrior;
import Model.Priest;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Mostrar selección de personaje
            String[] options = {"Mage", "Warrior", "Priest"};
            int choice = JOptionPane.showOptionDialog(null,
                    "Elige tu personaje:",
                    "Seleccionar Personaje",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            // Crear personaje según elección
            Character selectedCharacter = null;
            switch (choice) {
                case 0:
                    selectedCharacter = new Mage();
                    break;
                case 1:
                    selectedCharacter = new Warrior();
                    break;
                case 2:
                    selectedCharacter = new Priest();
                    break;
                default:
                    System.exit(0);
            }

            // Crear ventana y panel del juego
            JFrame frame = new JFrame("Mi Juego de Rol");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 700);
            frame.setLocationRelativeTo(null);
            frame.add(new GamePanel(selectedCharacter));
            frame.setVisible(true);
        });
    }
}
