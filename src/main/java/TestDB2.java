import java.sql.*;
public class TestDB2 {
    public static void main(String[] args) throws Exception {
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:./uniportal_db;MODE=MySQL;AUTO_SERVER=TRUE", "sa", "");
            conn.createStatement().executeQuery("SELECT FIELD('A', 'B', 'A')");
            System.out.println("FIELD worked!");
            conn.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
