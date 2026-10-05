package lab4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ScheduleClient {
    private final String host;
    private final int port;

    public ScheduleClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        try (
            Socket socket = new Socket(host, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Подключено к серверу " + host + ":" + port);
            System.out.println("Команды: groups | show <гр> [день] | add <гр> <день> <время> <предмет> | del <гр> <id> | exit\n");

            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) continue;

                out.println(input);

                String responseLine;
                while ((responseLine = in.readLine()) != null) {
                    if ("---END---".equals(responseLine)) {
                        break;
                    }
                    System.out.println(responseLine);
                }

                if ("exit".equalsIgnoreCase(input)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка соединения: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ScheduleClient("localhost", 8080).run();
    }
}