package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server
{
    private Vector<ClientHandler> clients;
    int count=0;
    private String nameClient= "Client";

    public Vector getClients()
    {
        return clients;
    }

    public Server() {
        clients = new Vector<>();
        ServerSocket server = null;
        Socket socket = null;



        try {
            server = new ServerSocket(8086);
            System.out.println("Сервер запущен!");

            while (true) {
                socket = server.accept();
                clients.add(new ClientHandler(this, socket, nameClient+count));

                System.out.println("Клиент " +clients.get(count).getNameClient()+ " подключился");
                count++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastMsg(String msg) {
        for (ClientHandler o: clients) {
            o.sendMsg(msg);
        }
    }
}
