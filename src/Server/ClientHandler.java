package Server;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class ClientHandler
{
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private Server server;
    private String nick;
    // путь к папке хранилищу сообщений клиента
    private static final String path =
            "D:\\GeekBrains\\Education_Projects\\Messenger1\\src\\Storage";
    //файл хранилище для клиента
    private File storage;





    public ClientHandler(Server server, Socket socket) {


        try {
            this.socket = socket;
            this.server = server;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String str = in.readUTF();
                            if (str.startsWith("/auth")) {
                                String[] tokens = str.split(" ");
                                String newNick = AuthService.getNickByLoginAndPass(tokens[1], tokens[2]);
                                if (newNick != null) {
                                    if (!server.isNickBusy(newNick)) {
                                    sendMsg("/authok");
                                    nick = newNick;
                                    server.subscribe(ClientHandler.this);
                                    break;
                                    } else {
                                        sendMsg("Учетная запись уже используется");
                                    }
                                } else {
                                    sendMsg("Неверный логин/пароль!");
                                }
                            }
                        }
                        // создание файла хранилиша
                        storage=createStorage(path);
                        while (true) {
                            String str = in.readUTF();
                            System.out.println("Client " + str);

                            // реализация личного сообщения
                            if (str.startsWith("/w"))
                            {
                                String[] tokens = str.split(" ",3);
                                server.sendPersonalMsg(ClientHandler.this, tokens[1], tokens[2]);
                            }
                            if (str.equals("/end")) {
                                out.writeUTF("/serverClosed");
                                break;
                            }
                            server.broadcastMsg(nick + " : " + str);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e)
                    {
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
                        server.unsubscribe(ClientHandler.this);
                        server.broadcastMsg(ClientHandler.this.getNick()+" "+"покинул чат");
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // запись сообщения в файл
    public void saveMsgStorage(String msg)
    {
        if (storage!=null)
        {
            try (FileWriter writer = new FileWriter(storage, true))
            {
                // запись всей строки

                writer.write(msg + "\r\n");
                writer.flush();
            } catch (IOException ex)
            {

                System.out.println(ex.getMessage());
            }
        } else throw new NullPointerException();

    }

    // создание хранилища сообщений для клиента
    private File createStorage(String path)
    { File newFile =new File(path,"Storage_"+nick+".txt");

        if(newFile.exists()) return newFile;
        else
        {
            boolean create=false;
            try
            {
                create = newFile.createNewFile();

            } catch (IOException e)
            {
                e.printStackTrace();
            }

            if (create) return newFile;
            else return null;
        }
    }

    public String getNick()
    {
        return nick;
    }


    public void sendMsg(String msg) {

        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
