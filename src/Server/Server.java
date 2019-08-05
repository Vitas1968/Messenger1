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
                hello("К нам присоединился ",clients.lastElement().getNameClient());
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

    public void hello(String msg, String nameClient) {
        String tmp = msg+ " -> "+nameClient;
        for (ClientHandler o: clients) {
            if (nameClient.equals(o.getNameClient())) continue;
            o.sendMsg(tmp);
        }
    }

    public void broadcastMsg(String msg, String nameClient) {
        String tmp = nameClient+ " -> "+msg;
        for (ClientHandler o: clients) {
            if (nameClient.equals(o.getNameClient()))
            {o.sendMsg(msg);
            continue;}
            o.sendMsg(tmp);
        }
    }

    public void subscribe(ClientHandler client) {
        clients.add(client);
    }

    public void unsubscribe(ClientHandler client) {
        clients.remove(client);
    }
}
