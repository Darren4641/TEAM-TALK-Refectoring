package com.goindol.teamtalk.client.dao;


import com.goindol.teamtalk.client.DB.DBDAO;
import com.goindol.teamtalk.client.dao.query.ChatLogQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatLogDAO {
    private static ChatLogDAO instance = null;

    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    private ChatLogDAO() {}

    public static ChatLogDAO getInstance() {
        if(instance == null)
            instance = new ChatLogDAO();
        return instance;
    }


    //채팅 로그 기록
    public void writeLog(int chatRoom_id, String content) {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(ChatLogQuery.WRITE_LOG.getValue());
            pstmt.setInt(1, chatRoom_id);
            pstmt.setString(2, content);
            pstmt.setString(3, sdf.format(now));
            pstmt.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeAll();

        }
    }

    //채팅 로그 보여줌
    public List<String> showChatLog(int chatRoomId) {
        List<String> content = null;
        try {
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(ChatLogQuery.GETCHATLOG.getValue());
            pstmt.setInt(1, chatRoomId);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                content = new ArrayList<String>();
                do {
                    content.add(rs.getString("content"));
                }while(rs.next());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeAll();
        }
        return content;
    }

    private void closeAll() {
        if(rs != null) try {rs.close();}catch(SQLException ex ) {}
        if(pstmt != null) try {pstmt.close();}catch(SQLException ex) {}
        if(conn != null) try {conn.close();}catch(SQLException ex) {}
    }

}