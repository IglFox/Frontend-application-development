package lab4.example;
import java.io.*;// импорт пакета, содержащего классы для ввода/вывода
import java.net.*;// импорт пакета, содержащего классы для работы в сети Internet

public class server {
    public static void main(String[] arg)
    {
        ServerSocket serverSocket = null;
        Socket clientAccepted = null;
        ObjectInputStream sois = null;
        ObjectOutputStream soos = null;

        try {
            System.out.println("server starting....");
            serverSocket = new ServerSocket(2525);
            clientAccepted = serverSocket.accept();
            System.out.println("connection established....");

            sois = new ObjectInputStream(clientAccepted.getInputStream());
            soos = new ObjectOutputStream(clientAccepted.getOutputStream());

            String clientMessageRecieved = (String) sois.readObject();
            while (!clientMessageRecieved.equals("quite")) {
                System.out.println("message recieved: '" + clientMessageRecieved + "'");
                clientMessageRecieved = clientMessageRecieved.toUpperCase();

                soos.writeObject(clientMessageRecieved);
                clientMessageRecieved = (String) sois.readObject();
            }
        } catch(Exception e) {

        } finally {
            try {
                sois.close();
                soos.close();
                clientAccepted.close();
                serverSocket.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
