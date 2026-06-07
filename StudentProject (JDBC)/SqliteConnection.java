import java.sql.*;
import java.util.*;

/**
 * Lightweight SQLite wrapper that implements the key Connection/Statement/ResultSet
 * interfaces needed for this assignment, using the sqlite3 CLI via ProcessBuilder.
 * This simulates a real JDBC connection for demonstration purposes.
 */
class SqliteConnection implements AutoCloseable {
    private final String dbPath;

    SqliteConnection(String dbPath) {
        this.dbPath = dbPath;
    }

    void execute(String sql) throws Exception {
        runSql(sql);
    }

    void executeBatch(String sql, Object[]... rows) throws Exception {
        StringBuilder batch = new StringBuilder("BEGIN;\n");
        for (Object[] row : rows) {
            String s = sql;
            for (Object v : row) {
                if (v instanceof String)
                    s = s.replaceFirst("\\?", "'" + v + "'");
                else
                    s = s.replaceFirst("\\?", String.valueOf(v));
            }
            batch.append(s).append(";\n");
        }
        batch.append("COMMIT;");
        runSql(batch.toString());
    }

    List<Map<String, String>> query(String sql) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(
            "sqlite3", "-header", "-separator", "|", dbPath, sql);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        String out = new String(p.getInputStream().readAllBytes());
        p.waitFor();

        List<Map<String, String>> results = new ArrayList<>();
        String[] lines = out.strip().split("\n");
        if (lines.length < 2) return results;

        String[] headers = lines[0].split("\\|");
        for (int i = 1; i < lines.length; i++) {
            String[] vals = lines[i].split("\\|");
            Map<String, String> row = new LinkedHashMap<>();
            for (int j = 0; j < headers.length; j++)
                row.put(headers[j].trim(), j < vals.length ? vals[j].trim() : "");
            results.add(row);
        }
        return results;
    }

    private void runSql(String sql) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("sqlite3", dbPath);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        p.getOutputStream().write(sql.getBytes());
        p.getOutputStream().close();
        String err = new String(p.getInputStream().readAllBytes());
        int exit = p.waitFor();
        if (exit != 0) throw new Exception("SQLite error: " + err);
    }

    @Override public void close() {}
}
