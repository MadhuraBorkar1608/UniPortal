import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class TestRunScript {
    public static void main(String[] args) {
        try {
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:./uniportal_db;MODE=MySQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE", "sa", "");
            Statement stmt = conn.createStatement();
            stmt.execute("RUNSCRIPT FROM 'src/main/resources/init_db.sql'");
            System.out.println("Script executed successfully.");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
