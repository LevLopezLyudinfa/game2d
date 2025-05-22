import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/game2D";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void insertarPartida(String nom, String tipo, int nVides, int tiempo) {
        String sql = "INSERT INTO Identificacion (nom, tipo, n_vides, tiempo) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nom);
            stmt.setString(2, tipo);
            stmt.setInt(3, nVides);
            stmt.setInt(4, tiempo);

            stmt.executeUpdate();
            System.out.println("Datos insertados correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
