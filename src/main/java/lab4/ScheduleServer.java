package lab4;

/*
Разработать приложение, в котором серверная часть хранит информацию о расписании занятий студентов.
Клиентская часть имеет возможность просматривать, редактировать и удалять необходимую информацию.
 */

import java.io.*;
import java.net.*;


public class ScheduleServer {
    private ObjectInputStream sin = null;
    private ObjectOutputStream sout = null;
    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;

    public ScheduleServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        IO.println("Сервер запущен");
    }

    public void start() {
        try {
            clientSocket = serverSocket.accept();
            sin = new ObjectInputStream(clientSocket.getInputStream());
            sout = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            IO.println("Сокет закрыт.");
        }

        do {
            commandRoute();
        } while (true);
    }

    private void commandRoute() {

    }

}
