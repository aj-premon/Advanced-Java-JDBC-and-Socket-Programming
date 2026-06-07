import java.sql.*;
import java.util.HashMap;

public class StudentGradeSystem {

    // ── 1. Array to store student names ──
    static String[] studentNames = {
        "Alice Rahman",
        "Bob Hossain",
        "Carol Islam",
        "David Khan",
        "Eva Begum"
    };

    static int[] studentIDs = {101, 102, 103, 104, 105};

    // ── 2. HashMap<Integer, Integer[]> ──
    static HashMap<Integer, Integer[]> studentMarks = new HashMap<>();

    static {
        studentMarks.put(101, new Integer[]{85, 90, 78});
        studentMarks.put(102, new Integer[]{60, 55, 70});
        studentMarks.put(103, new Integer[]{95, 92, 97});
        studentMarks.put(104, new Integer[]{45, 50, 40});
        studentMarks.put(105, new Integer[]{72, 68, 80});
    }

    // ── 3a. Calculate total ──
    static int calculateTotal(Integer[] marks) {
        int total = 0;
        for (int mark : marks) total += mark;
        return total;
    }

    // ── 3b. Assign grade ──
    static String assignGrade(int total) {
        double avg = total / 3.0;
        if (avg >= 90) return "A+";
        if (avg >= 80) return "A";
        if (avg >= 70) return "B";
        if (avg >= 60) return "C";
        if (avg >= 50) return "D";
        return "F";
    }

    // ── MySQL JDBC connection ──
    static final String URL  = "jdbc:mysql://localhost:3306/student_result_system";
    static final String USER = "root";
    static final String PASS = "0000";

    // ── Create table ──
    static void createTable(Connection conn) throws SQLException {
        String sql =
            "CREATE TABLE IF NOT EXISTS students (" +
            "  id    INT PRIMARY KEY," +
            "  name  VARCHAR(100) NOT NULL," +
            "  mark1 INT NOT NULL," +
            "  mark2 INT NOT NULL," +
            "  mark3 INT NOT NULL," +
            "  total INT NOT NULL," +
            "  grade VARCHAR(5) NOT NULL" +
            ")";
        conn.createStatement().execute(sql);
        System.out.println("[DB] Table 'students' is ready.\n");
    }

    // ── 4. INSERT student data ──
    static void insertStudents(Connection conn) throws SQLException {
        String sql =
            "INSERT INTO students (id, name, mark1, mark2, mark3, total, grade) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE " +
            "name=VALUES(name), mark1=VALUES(mark1), mark2=VALUES(mark2), " +
            "mark3=VALUES(mark3), total=VALUES(total), grade=VALUES(grade)";

        PreparedStatement ps = conn.prepareStatement(sql);

        System.out.println("──────── INSERTING RECORDS ────────");
        for (int i = 0; i < studentNames.length; i++) {
            int       id    = studentIDs[i];
            String    name  = studentNames[i];
            Integer[] marks = studentMarks.get(id);
            int       total = calculateTotal(marks);
            String    grade = assignGrade(total);

            ps.setInt   (1, id);
            ps.setString(2, name);
            ps.setInt   (3, marks[0]);
            ps.setInt   (4, marks[1]);
            ps.setInt   (5, marks[2]);
            ps.setInt   (6, total);
            ps.setString(7, grade);
            ps.executeUpdate();

            System.out.printf(
                "  [INSERT] ID:%-3d | %-15s | Marks:%d,%d,%d | Total:%d | Grade:%s%n",
                id, name, marks[0], marks[1], marks[2], total, grade
            );
        }
        System.out.println("\n[DB] All records inserted successfully.\n");
    }

    // ── 5. Display all records from DB ──
    static void displayAllRecords(Connection conn) throws SQLException {
        String sql = "SELECT * FROM students ORDER BY id";
        ResultSet rs = conn.createStatement().executeQuery(sql);

        System.out.println("╔══════╦═══════════════════╦═══════╦═══════╦═══════╦═══════╦═══════╗");
        System.out.println("║  ID  ║       Name        ║  Sub1 ║  Sub2 ║  Sub3 ║ Total ║ Grade ║");
        System.out.println("╠══════╬═══════════════════╬═══════╬═══════╬═══════╬═══════╬═══════╣");

        while (rs.next()) {
            System.out.printf(
                "║ %4d ║ %-17s ║  %4d ║  %4d ║  %4d ║  %4d ║  %-5s ║%n",
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("mark1"),
                rs.getInt("mark2"),
                rs.getInt("mark3"),
                rs.getInt("total"),
                rs.getString("grade")
            );
        }
        System.out.println("╚══════╩═══════════════════╩═══════╩═══════╩═══════╩═══════╩═══════╝");
    }

    // ── Main ──
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   Student Grade Management System      ");
        System.out.println("        Developed by: Jemin             ");
        System.out.println("========================================\n");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            System.out.println("[DB] Connected to MySQL successfully!\n");
            createTable(conn);
            insertStudents(conn);
            System.out.println("──────── DATABASE RECORDS ────────\n");
            displayAllRecords(conn);
            System.out.println("\n[DONE] Program completed successfully.");
        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
            e.printStackTrace();
        }
    }
}