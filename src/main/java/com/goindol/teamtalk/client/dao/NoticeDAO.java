package com.goindol.teamtalk.client.dao;

import com.goindol.teamtalk.client.DB.DBDAO;
import com.goindol.teamtalk.client.dao.query.NoticeQuery;
import com.goindol.teamtalk.client.dto.NoticeDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class NoticeDAO {

    private static NoticeDAO instance = null;

    private Connection conn = null;

    private PreparedStatement pstmt = null;

    private ResultSet rs = null;

    public static NoticeDAO getInstance(){
        if(instance==null){
            instance = new NoticeDAO();
        }
        return instance;
    }

    //공지 생성버튼을 누르고 공지 제목과 내용을 받은 페이지
    public void createNotice(String nickName, int chatRoom_id,String title,String content){

        try {

            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(NoticeQuery.CREATENOTICE.getValue());
            pstmt.setString(1, nickName);
            pstmt.setInt(2, chatRoom_id);
            pstmt.setString(3, title);
            pstmt.setString(4, content);
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement(NoticeQuery.NOTICE_INIT.getValue());
            pstmt.setInt(1, chatRoom_id);
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement(NoticeQuery.NOTICE_INIT_WRITER.getValue());
            pstmt.setString(1,nickName);
            pstmt.setInt(2, chatRoom_id);
            pstmt.executeUpdate();

        } catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeAll();
        }
    }

    //공지 삭제
    public void deleteNotice(int chatRoom_id){
        try {
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(NoticeQuery.DELETE_NOTICE.getValue());
            pstmt.setInt(1,chatRoom_id);
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement(NoticeQuery.DELETE_UPDATE.getValue());
            pstmt.setInt(1, chatRoom_id);
            pstmt.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeAll();
        }

    }


    // 해당 채팅 방에 공지가 있는지
    public boolean hasNotice(int chatRoom_id){

        try {
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(NoticeQuery.ISNOTICE.getValue());
            pstmt.setInt(1,chatRoom_id);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                return true;
            }

        }catch(Exception e) {
            //시스템이 오류 메시지 출력
            e.printStackTrace();
        }finally {
            closeAll();
        }

        return false;
    }


    //공지 내용을 보여줌
    public NoticeDTO showNoticeContent(int chatRoom_id, String nickName){
        NoticeDTO noticeDTO = null;

        try {
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(NoticeQuery.GETNOTICE.getValue());
            pstmt.setInt(1,chatRoom_id);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                pstmt = conn.prepareStatement(NoticeQuery.NOTICE_READ.getValue());
                pstmt.setString(1, nickName);
                pstmt.setInt(2, chatRoom_id);
                pstmt.executeUpdate();

                pstmt = conn.prepareStatement(NoticeQuery.GETNOTICE.getValue());
                pstmt.setInt(1, chatRoom_id);
                rs = pstmt.executeQuery();
                if(rs.next()) {
                    noticeDTO = new NoticeDTO();
                    noticeDTO.setNotice_id(rs.getInt("notice_id"));
                    noticeDTO.setNickName(rs.getString("nickName"));
                    noticeDTO.setChatRoom_id(rs.getInt("chatRoom_id"));
                    noticeDTO.setTitle(rs.getString("title"));
                    noticeDTO.setContent(rs.getString("content"));
                }
            }

        }catch(Exception e) {
            //시스템이 오류 메시지 출력
            e.printStackTrace();
        }finally {
            closeAll();
        }

        return noticeDTO;
    }


    //공지 읽은 사람 리스트
    public ArrayList<String> readNoticeUserList(int chatRoom_id){

        ArrayList<String> chatPeople = new ArrayList<>();
        String query =
                "SELECT * FROM `db_team`.`chatRoomParticipants` WHERE chatRoom_id =? and isNoticeRead=?";

        try {
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(NoticeQuery.NOTICE_NON_READ.getValue());
            pstmt.setInt(1,chatRoom_id);
            pstmt.setInt(2,2);
            rs = pstmt.executeQuery();
            while (rs.next()){
                String nickName = rs.getString("nickName");
                chatPeople.add(nickName);
            }

        }catch(Exception e) {
            //시스템이 오류 메시지 출력
            e.printStackTrace();
        }finally {
            closeAll();
        }
        return chatPeople;
    }

    // 공지를 전부 읽었는지 확인
    public boolean readNoticeAllParticipants(int chatRoom_id){

        try {
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(NoticeQuery.NOTICE_READ_COUNT.getValue());
            pstmt.setInt(1,chatRoom_id);
            rs = pstmt.executeQuery();

            if(rs.next()) {
                pstmt = conn.prepareStatement(NoticeQuery.NOTICE_READ_ALL_COUNT.getValue());
                pstmt.setInt(1, 2);
                pstmt.setInt(2, chatRoom_id);
                ResultSet rs1 = pstmt.executeQuery();

                if(rs1.next()){
                    int cnt = rs.getInt(1);
                    int cnt1 = rs1.getInt(1);
                    if(cnt==cnt1){
                        return true;
                    }else{
                        return false;
                    }
                }
            }
        } catch(Exception e) {
            //시스템이 오류 메시지 출력
            e.printStackTrace();
        } finally {
            closeAll();
        }
        return false;
    }

    private void closeAll() {
        if(rs != null) try {rs.close();}catch(SQLException ex ) {}
        if(pstmt != null) try {pstmt.close();}catch(SQLException ex) {}
        if(conn != null) try {conn.close();}catch(SQLException ex) {}
    }

}