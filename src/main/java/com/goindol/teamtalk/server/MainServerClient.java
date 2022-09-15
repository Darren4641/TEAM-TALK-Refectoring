package com.goindol.teamtalk.server;

import com.goindol.teamtalk.client.dao.ChatRoomParticipantsDAO;
import com.goindol.teamtalk.client.dao.FriendDAO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

public class MainServerClient {
    FriendDAO friendDAO = FriendDAO.getInstance();
    ChatRoomParticipantsDAO chatRoomParticipantsDAO = ChatRoomParticipantsDAO.getInstance();
    ServerSocket serverSocket;
    Socket socket;
    String key;


    public MainServerClient(Socket socket) {
        this.socket = socket;
    }

    public void receiveData() {
        try {
            InputStream in = socket.getInputStream();
            byte[] buffer = new byte[512];
            int length = in.read(buffer);
            while (length == -1) throw new IOException();
            key = new String(buffer, 0, length, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            try {
                System.out.println("Error : " + socket.getRemoteSocketAddress() + " - " + Thread.currentThread().getName());
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }


    public void receive() {

        Runnable thread = new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        InputStream in = socket.getInputStream();
                        byte[] buffer = new byte[512];
                        int length = in.read(buffer);
                        while(length == -1) throw new NullPointerException();
                        System.out.println("Success!! : " + socket.getRemoteSocketAddress() + " - " + Thread.currentThread().getName());
                        String message = new String(buffer, 0, length, "UTF-8");
                        String[] data = message.split("/");
                        String code = data[0];
                        String roomId = data[1];
                        String value = data[2];
                        System.out.println("value : " + value);
                        System.out.println("code : " + code);
                        if(code.equals("login")) {
                            ArrayList<String> sendUser = friendDAO.getFriendNickNameList(value);
                            if(roomId.equals("logout")) {
                                realTimeSend(sendUser, code);
                            }
                            else {
                                if(sendUser != null) {
                                    sendUser.add(value);
                                    realTimeSend(sendUser, code);
                                }else {
                                    sendUser = new ArrayList<String>();
                                    sendUser.add(value);
                                    //ss
                                    realTimeSend(sendUser, code);
                                }
                            }

                        }else if(code.equals("chatRoom")) {
                            ArrayList<String> sendUser = chatRoomParticipantsDAO.getChatRoomParticipants(Integer.parseInt(roomId));
                            realTimeSend(sendUser, code);
                        }else if(code.equals("notice")) {
                            ArrayList<String> sendUser = chatRoomParticipantsDAO.getChatRoomParticipants(Integer.parseInt(roomId));
                            realTimeSend(sendUser, code);
                        }else if(code.equals("vote")) {
                            ArrayList<String> sendUser = chatRoomParticipantsDAO.getChatRoomParticipants(Integer.parseInt(roomId));
                            realTimeSend(sendUser, code);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        System.out.println("Error : " + socket.getRemoteSocketAddress() + " - " + Thread. currentThread().getName());
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        };
        //쓰레드 안전적으로 관리
        MainServer.threadPool.submit(thread);
    }

    private void realTimeSend(ArrayList<String> sendUser, String code) {
        for(Map.Entry<String, MainServerClient> entry : MainServer.clients.entrySet()) {
            for(String user : sendUser) {
                if(entry.getKey().equals(user)) {
                    entry.getValue().send(code);
                }
            }
        }
    }

    public void send(String message) {
        Runnable thread = new Runnable() {
            @Override
            public void run() {
                try {
                    OutputStream out = socket.getOutputStream();
                    byte[] buffer = message.getBytes("UTF-8");
                    out.write(buffer);
                    out.flush();
                } catch (Exception e) {
                    try {
                        System.out.println("Error : " + socket.getRemoteSocketAddress() + " - " + Thread. currentThread().getName());
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        };
        MainServer.threadPool.submit(thread);
    }

}
