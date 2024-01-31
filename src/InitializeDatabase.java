import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class InitializeDatabase {
    String url = "jdbc:mysql://localhost:3306/Transport";
    String username = "root";
    String password = "root";

    public void getDatabase() {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}