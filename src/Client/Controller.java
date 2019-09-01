package Client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller
{


    @FXML
    Button btn1;
    @FXML
    TextField textField;
    @FXML
    HBox bottomPanel;
    @FXML
    HBox upperPanel;
    @FXML
    ListView<VBox>listView;

    @FXML
    TextField loginfield;

    @FXML
    PasswordField passwordfiled;
    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    private boolean isAuthorized;
    private boolean flag=false;
    final String IP_ADRESS = "localhost";
    final int PORT = 8086;


    public void setAuthorized(boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
        if (!isAuthorized) {
            upperPanel.setVisible(true);
            upperPanel.setManaged(true);
            bottomPanel.setVisible(false);
            bottomPanel.setManaged(false);
        } else {
            upperPanel.setVisible(false);
            upperPanel.setManaged(false);
            bottomPanel.setVisible(true);
            bottomPanel.setManaged(true);
        }
    }

    public void connect() {
        try {
            socket = new Socket(IP_ADRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String str = in.readUTF();
                            if (str.startsWith("/authok")) {
                                setAuthorized(true);
                                break;
                            } else {
                                outMsg(str,flag);
                            }
                        }
                        while (true)
                        {
                            String str = in.readUTF();
                            if (str.equals("/serverClosed"))
                            {
                                setAuthorized(false);
                             System.out.println("Клиент отключился");
                                break;
                            }
                            if(str.startsWith(" "))
                            {
                                //  размещение своих сообщений справа
                                str.trim();
                                flag=true;
                            } else { //   размещение остальных сообщений слева
                                str.trim();
                                 }
                                 outMsg(str,flag);
                            System.out.println(str);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
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
        System.out.println("Клиент создан");
    }

    public EventHandler<WindowEvent> getCloseEventHandler(){
        return closeEventHandler;
    }

    public void sendMsg() {
        try {
            out.writeUTF(textField.getText());
            textField.clear();
            textField.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tryToAuth()
    {
        if (socket == null || socket.isClosed()) {
            connect();
        }

        try {
            out.writeUTF("/auth " + loginfield.getText() + " " + passwordfiled.getText());
            loginfield.clear();
            passwordfiled.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // вывод сообщения
    void outMsg(String msg, boolean flag)
    {
        Platform.runLater(new Runnable() {
            @Override
            public void run()
            {
                Label label = new Label(msg + "\n");
                VBox vBox = new VBox(label);

                if (!flag)
                {
                    vBox.setAlignment(Pos.TOP_LEFT);
                } else
                {
                    vBox.setAlignment(Pos.TOP_RIGHT);
                }
                listView.getItems().add(vBox);
                Controller.this.flag = (Controller.this.flag) ? false : Controller.this.flag;
            }
        });

    }


    private EventHandler<WindowEvent> closeEventHandler = new EventHandler<WindowEvent>()
    {
        @Override
        public void handle(WindowEvent event) {
            try
            {
                out.writeUTF("/end");
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    };



    // вывод сообщения образец
//    public void setMsg(String str) {
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                Label message = new Label(str);
//                VBox messageBox = new VBox(message);
//                if(nick != "") {
//                    String[] mass = str.split(":");
//                    if(nick.equalsIgnoreCase(mass[0])) {
//                        messageBox.setAlignment(Pos.CENTER_RIGHT);
//                    }
//                }
//                messagesView.getItems().add(messageBox);
//            }
//        });
//    }







}
