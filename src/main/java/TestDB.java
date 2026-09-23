import java.sql.*;
public class TestDB {
    public static void main(String[] args) throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:./uniportal_db;AUTO_SERVER=TRUE", "sa", "");
        
        String q1 = "SELECT t.*, s.subject_name, f.full_name as faculty_name, d.dept_name " +
                       "FROM college_timetable t " +
                       "JOIN subjects s ON t.subject_id = s.id " +
                       "JOIN courses c ON s.course_id = c.id " +
                       "JOIN departments d ON c.dept_id = d.id " +
                       "LEFT JOIN faculty f ON t.faculty_id = f.faculty_id";
        
        ResultSet rs1 = conn.createStatement().executeQuery(q1);
        int count1 = 0;
        while(rs1.next()) count1++;
        System.out.println("Query 1 count: " + count1);
        
        String q2 = "SELECT t.*, s.subject_name, f.full_name as faculty_name " +
                       "FROM college_timetable t " +
                       "JOIN subjects s ON t.subject_id = s.id " +
                       "JOIN courses c ON s.course_id = c.id " +
                       "LEFT JOIN faculty f ON t.faculty_id = f.faculty_id " +
                       "WHERE t.division = 'A' AND c.dept_id = 1";
        ResultSet rs2 = conn.createStatement().executeQuery(q2);
        int count2 = 0;
        while(rs2.next()) count2++;
        System.out.println("Query 2 count: " + count2);
        
        conn.close();
    }
}
