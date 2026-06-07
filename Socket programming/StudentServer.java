import java.io.*;
import java.net.*;
import java.util.*;

public class StudentServer {

    private static final int PORT = 5000;

    // HashMap<StudentID, Marks[]> — stores marks as Integer array [mark1, mark2]
    private static HashMap<Integer, Integer[]> studentMarks = new HashMap<>();

    // Array to store student names (parallel structure, matching order of insertion)
    private static ArrayList<String> studentNames = new ArrayList<>();

    // Keep insertion order of IDs for display
    private static ArrayList<Integer> studentIds = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   Student Info Transfer System - Server");
        System.out.println("========================================");
        System.out.println("[Server] Listening on port " + PORT + "...\n");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[Connected] Client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Handle the client in the same thread (single-client design for this assignment)
                handleClient(clientSocket);

                System.out.println("\n[Disconnected] Client disconnected.\n");
                System.out.println("========================================");
                System.out.println("         All Student Records");
                System.out.println("========================================");
                displayAllStudents();
                System.out.println("========================================");
                System.out.println("[Server] Waiting for next client...\n");
            }

        } catch (IOException e) {
            System.out.println("[Error] Server error: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String line;
            while ((line = in.readLine()) != null) {

                if (line.equalsIgnoreCase("EXIT")) {
                    System.out.println("[Client] Client requested disconnect.");
                    break;
                }

                // Expected format: ID,Name,Mark1,Mark2
                String[] parts = line.split(",", 4);
                if (parts.length < 4) {
                    out.println("[ACK] ERROR: Invalid data format. Expected: ID,Name,Mark1,Mark2");
                    continue;
                }

                try {
                    int id      = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    int mark1   = Integer.parseInt(parts[2].trim());
                    int mark2   = Integer.parseInt(parts[3].trim());

                    // Store in collections
                    if (!studentMarks.containsKey(id)) {
                        studentIds.add(id);
                        studentNames.add(name);
                    } else {
                        // Update name in the parallel list if ID already exists
                        int index = studentIds.indexOf(id);
                        studentNames.set(index, name);
                    }
                    studentMarks.put(id, new Integer[]{mark1, mark2});

                    System.out.printf("[Received] ID: %d | Name: %s | Marks: %d, %d%n", id, name, mark1, mark2);
                    out.println("[ACK] Student record stored successfully. ID=" + id);

                } catch (NumberFormatException e) {
                    out.println("[ACK] ERROR: ID and Marks must be integers.");
                }
            }

        } catch (IOException e) {
            System.out.println("[Error] Error handling client: " + e.getMessage());
        }
    }

    private static void displayAllStudents() {
        if (studentIds.isEmpty()) {
            System.out.println("  No student records found.");
            return;
        }

        System.out.printf("%-10s %-20s %-10s %-10s %-10s%n",
                "ID", "Name", "Mark 1", "Mark 2", "Average");
        System.out.println("----------------------------------------------------------");

        for (int i = 0; i < studentIds.size(); i++) {
            int id         = studentIds.get(i);
            String name    = studentNames.get(i);
            Integer[] marks = studentMarks.get(id);
            double avg     = (marks[0] + marks[1]) / 2.0;

            System.out.printf("%-10d %-20s %-10d %-10d %-10.1f%n",
                    id, name, marks[0], marks[1], avg);
        }
    }
}
