package com.goindol.teamtalk.client.dao;

import com.goindol.teamtalk.client.DB.DBDAO;
import com.goindol.teamtalk.client.dao.query.ChatRoomParticipantsQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChatRoomParticipantsDAO {
    private static ChatRoomParticipantsDAO instance = null;

    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    private ChatRoomParticipantsDAO() {}

    public static ChatRoomParticipantsDAO getInstance() {
        if(instance == null)
            instance = new ChatRoomParticipantsDAO();
        return instance;
    }

    //채팅방에 친구 추가시 이미 채팅방에 존재하는 친구인지 확인
    public boolean isParticipants(int chatRoom_id, String nickName){
        boolean check = false;

        try {
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(ChatRoomParticipantsQuery.ISPARTICIPANTS.getValue());
            pstmt.setInt(1,chatRoom_id);
            pstmt.setString(2,nickName);
            rs = pstmt.executeQuery();

            if(rs.next()){
                check = true;
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            closeAll();
        }
        return check;
    }

    public ArrayList<String> getChatRoomParticipants(int chatRoom_id) {
        ArrayList<String> nickName = null;

        try {
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(ChatRoomParticipantsQuery.GETPARTICIPANTS.getValue());
            pstmt.setInt(1, chatRoom_id);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                nickName = new ArrayList<String>();
                do {
                    nickName.add(rs.getString("nickName"));

                }while(rs.next());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeAll();
        }
        return nickName;
    }

    public void exitCurrentRoom(int chatRoomId, String nickName) {
        try {
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(ChatRoomParticipantsQuery.EXIT_CURRENT_ROOM.getValue());
            pstmt.setInt(1, chatRoomId);
            pstmt.setString(2, nickName);
            pstmt.executeUpdate();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeAll();
        }
    }

    private void closeAll() {
        if(rs != null) try {rs.close();}catch(SQLException ex ) {}
        if(pstmt != null) try {pstmt.close();}catch(SQLException ex) {}
        if(conn != null) try {conn.close();}catch(SQLException ex) {}
    }


}