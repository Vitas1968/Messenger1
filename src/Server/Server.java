package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server
{
    private Vector<ClientHandler> clients;
    // создание executorService
    private ExecutorService executorService;

    public Server() {
        clients = new Vector<>();
        ServerSocket server = null;
        Socket socket = null;
        executorService= Executors.newFixedThreadPool(4);



        try {
            AuthService.connect();
            server = new ServerSocket(8086);
            System.out.println("Сервер запущен!");

            while (true) {
                socket = server.accept();
                // создаем хендлер и добавляем в пул
                executorService.execute( new ClientHandler(this, socket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e)
        {
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
            AuthService.disconnect();
            executorService.shutdown();
        }
    }
    //проверка есть ли такой клиент в чате
    public boolean isNickBusy(String nick) {
        for (ClientHandler o : clients) {
            if (o.getNick().equals(nick)) {
                return true;
            }
        }
        return false;
    }


    // рассылка сообщений всем участникам чата
    public void broadcastMsg(String msg) {
        for (ClientHandler o: clients) {
            if (msg.startsWith(o.getNick())) {
                // добавленный к строке пробел метка - свой
                String tmp=" "+msg;
                o.sendMsg(tmp);
                o.saveMsgStorage(msg);
                continue;
            }
            o.sendMsg(msg);
            o.saveMsgStorage(msg);
        }
    }


    // отправка личного сообщения указанному абоненту
    public void sendPersonalMsg(ClientHandler from, String nickTo, String msg) {
        for (ClientHandler o : clients) {
            if (o.getNick().equals(nickTo)) {
                o.sendMsg("from " + from.getNick() + ": " + msg);
                from.sendMsg("to " + nickTo + ": " + msg);
                return;
            }
        }
        from.sendMsg("Клиент с ником " + nickTo + " не найден в чате");
    }

    // добавление клиента в чат
    public void subscribe(ClientHandler client) {
        clients.add(client);
    }

    //удаление клиента из чата
    public void unsubscribe(ClientHandler client) {
        clients.remove(client);
    }
}
