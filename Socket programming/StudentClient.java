import java.io.*;
import java.net.*;
import java.util.Scanner;

public class StudentClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("========================================");
        System.out.println("   Student Info Transfer System - Client");
        System.out.println("========================================");

        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("[Connected] Connected to server at " + SERVER_HOST + ":" + SERVER_PORT);

            boolean running = true;

            while (running) {
                System.out.println("\n--- Enter Student Details ---");

                // Get Student ID
                int studentId = 0;
                while (true) {
                    System.out.print("Enter Student ID (integer): ");
                    if (scanner.hasNextInt()) {
                        studentId = scanner.nextInt();
                        scanner.nextLine(); // consume leftover newline
                        break;
                    } else {
                        System.out.println("[Error] Invalid ID. Please enter a valid integer.");
                        scanner.nextLine();
                    }
                }

                // Get Student Name
                System.out.print("Enter Student Name: ");
                String studentName = scanner.nextLine().trim();

                // Get Marks (two subjects)
                int mark1 = 0, mark2 = 0;
                while (true) {
                    System.out.print("Enter Mark 1 (0-100): ");
                    if (scanner.hasNextInt()) {
                        mark1 = scanner.nextInt();
                        scanner.nextLine();
                        if (mark1 >= 0 && mark1 <= 100) break;
                        System.out.println("[Error] Mark must be between 0 and 100.");
                    } else {
                        System.out.println("[Error] Invalid mark. Please enter an integer.");
                        scanner.nextLine();
                    }
                }
                while (true) {
                    System.out.print("Enter Mark 2 (0-100): ");
                    if (scanner.hasNextInt()) {
                        mark2 = scanner.nextInt();
                        scanner.nextLine();
                        if (mark2 >= 0 && mark2 <= 100) break;
                        System.out.println("[Error] Mark must be between 0 and 100.");
                    } else {
                        System.out.println("[Error] Invalid mark. Please enter an integer.");
                        scanner.nextLine();
                    }
                }

                // Format: ID,Name,Mark1,Mark2
                String data = studentId + "," + studentName + "," + mark1 + "," + mark2;
                out.println(data);
                System.out.println("[Sent] Data sent to server: " + data);

                // Read server acknowledgment
                String serverResponse = in.readLine();
                System.out.println("[Server] " + serverResponse);

                // Ask to send another student
                System.out.print("\nDo you want to add another student? (yes/no): ");
                String choice = scanner.nextLine().trim().toLowerCase();
                if (!choice.equals("yes") && !choice.equals("y")) {
                    out.println("EXIT");
                    running = false;
                    System.out.println("[Disconnected] Closing connection. Goodbye!");
                }
            }

        } catch (ConnectException e) {
            System.out.println("[Error] Could not connect to server. Make sure the server is running first.");
        } catch (IOException e) {
            System.out.println("[Error] Connection error: " + e.getMessage());
        }

        scanner.close();
    }
}
