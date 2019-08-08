package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler
{
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private Server server;
    private String nameClient;




    public ClientHandler(Server server, Socket socket, String nameClient) {
        try {
            this.socket = socket;
            this.server = server;
            this.nameClient=nameClient;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String str = in.readUTF();
                           /* System.out.println("Client "
                                    + server.getClients().get(server.getClients().indexOf(this))
                                    + " пишет "
                                    + str);*/

                           // System.out.println("Клиентов подключено-> "+server.getClients().size());
                            if (str.equals("/end")) {
                                out.writeUTF("/serverClosed");
                                delClient();
                                server.broadcastMsg(ClientHandler.this.getNameClient()+" отключился от чата");

                               // System.out.println("Индекс клиента - "+ server.getClients().indexOf(this));
                                break;
                            }
                            server.broadcastMsg(str);
                            System.out.println("Клиент "+ ClientHandler.this.getNameClient()+ " пишет "+str);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("ClientHandler создан");
    }

    void delClient()
    {
        System.out.println("Клиент "+ClientHandler.this.getNameClient()+ " отключился ");
        server.getClients().remove(ClientHandler.this);
        System.out.println("Клиентов после /end подключено-> "+server.getClients().size());
    }

    public String getNameClient()
    {
        return nameClient;
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
