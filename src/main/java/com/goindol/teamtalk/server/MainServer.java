package com.goindol.teamtalk.server;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainServer extends Application {
    public static ExecutorService threadPool;
    ServerSocket serverSocket;

    public static Map<String, MainServerClient> clients = new HashMap<String, MainServerClient>();

    public void startServer(int port) {
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            e.printStackTrace();
            if(!serverSocket.isClosed()) {
                stopServer();
            }
            return;
        }
        Runnable thread = new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        System.out.println("MAINSERVER START");
                        Socket socket = serverSocket.accept();

                        System.out.println("Connect Complete");
                        MainServerClient client = new MainServerClient(socket);
                        client.receiveData();
                        MainServer.clients.put(client.key, client);
                        synchronized (this) {
                            client.receive();
                        }
                        System.out.println("Enrty User --> " + client.key);
                        System.out.println("accept Client : " + socket.getRemoteSocketAddress() + " - " + Thread.currentThread().getName());
                    } catch (Exception e) {
                        if(!serverSocket.isClosed()) {
                            stopServer();
                        }
                        break;
                    }
                }
            }
        };

        threadPool = Executors.newCachedThreadPool(); //초기화
        threadPool.submit(thread);
    }

    public void stopServer() {
        try {
            Iterator it = clients.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry<Integer, MainServerClient> entry = (Map.Entry)it.next();
                entry.getValue().socket.close();
                it.remove();
            }
            if(serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            if(threadPool != null && !threadPool.isShutdown()) {
                threadPool.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(5));

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        root.setCenter(textArea);
        Button toggleButton = new Button("시작하기");
        toggleButton.setMaxWidth(Double.MAX_VALUE);
        BorderPane.setMargin(toggleButton, new Insets(1, 0, 0, 0));
        root.setBottom(toggleButton);

        int port = 9600;

        toggleButton.setOnAction(event-> {
            if(toggleButton.getText().equals("시작하기")) {
                startServer(port);
                Platform.runLater(()-> {
                    String message = String.format("[Server started...]\n", port);
                    textArea.appendText(message);
                    toggleButton.setText("종료하기");
                });
            }else {
                stopServer();
                Platform.runLater(()->{
                    String message = String.format("[Server closed...]\n", port);
                    textArea.appendText(message);
                    toggleButton.setText("시작하기");
                });
            }
        });
        Scene scene = new Scene(root, 200, 200);
        primaryStage.setTitle("MainServer Controller");
        primaryStage.setOnCloseRequest(event -> stopServer());
        primaryStage.setScene(scene);
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }
}