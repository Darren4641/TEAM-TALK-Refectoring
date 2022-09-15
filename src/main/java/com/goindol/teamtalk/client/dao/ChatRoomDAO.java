package com.goindol.teamtalk.client.dao;

import com.goindol.teamtalk.client.DB.DBDAO;
import com.goindol.teamtalk.client.dao.query.ChatRoomQuery;
import com.goindol.teamtalk.client.dto.ChatRoomDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChatRoomDAO {
    private static ChatRoomDAO instance = null;

    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    private ChatRoomDAO() { }

    public static ChatRoomDAO getInstance() {
        if(instance == null)
            instance = new ChatRoomDAO();
        return instance;
    }

    //채팅 생성
    public void createChatRoom(String chatRoomName, String nickName) {
        try {
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(ChatRoomQuery.CREATE_CHATROOM.getValue());
            pstmt.setString(1, chatRoomName);
            pstmt.setString(2, nickName);
            pstmt.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeAll();
        }
    }

    // 채팅방을 만들고 자기 자신을 초대할 때 채팅방 id 필요
    public int getChatRoomId(String chatRoomName, String nickName) {
        int cnt = 0;
        try {
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(ChatRoomQuery.GETCHATROOMID.getValue());
            pstmt.setString(1, chatRoomName);
            pstmt.setString(2, nickName);
            rs = pstmt.executeQuery();
            if(rs.next())
                cnt = rs.getInt(1);
        } catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeAll();
        }
        return cnt;
    }


    //최초 방 생성시 자기 자신 아이디 정보 입력
    //여러명 초대시 해당 매개변수 리스트에 담아서 반복 실행
    public int inviteChatRoom(int chatRoom_id, String nickName) {

        try {
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(ChatRoomQuery.NOTICE_INIT.getValue());
            pstmt.setInt(1, chatRoom_id);
            rs = pstmt.executeQuery();

            pstmt = conn.prepareStatement(ChatRoomQuery.VOTE_INIT.getValue());
            pstmt.setInt(1, chatRoom_id);
            ResultSet check = pstmt.executeQuery();

            if(check.next()) {
                pstmt = conn.prepareStatement(ChatRoomQuery.GETVOTEID.getValue());
                pstmt.setInt(1, chatRoom_id);
                ResultSet rs1 = pstmt.executeQuery();
                if(rs1.next()) {
                    pstmt = conn.prepareStatement(ChatRoomQuery.VOTECHECK.getValue());
                    pstmt.setString(1, nickName);
                    pstmt.setInt(2, rs1.getInt("vote_id"));
                    ResultSet rs2 = pstmt.executeQuery();

                    pstmt = conn.prepareStatement(ChatRoomQuery.INVITE_CHATROOM.getValue());
                    pstmt.setInt(1, chatRoom_id);
                    pstmt.setString(2, nickName);
                    if(rs.next())
                        pstmt.setInt(3, 1);
                    else
                        pstmt.setInt(3, 0);
                    if(rs2.next())
                        pstmt.setInt(4, 0);
                    else
                        pstmt.setInt(4, 1);
                    pstmt.executeUpdate();
                }
            }else {
                pstmt = conn.prepareStatement(ChatRoomQuery.INVITE_CHATROOM.getValue());
                pstmt.setInt(1, chatRoom_id);
                pstmt.setString(2, nickName);
                if(rs.next())
                    pstmt.setInt(3, 1);
                else
                    pstmt.setInt(3, 0);
                pstmt.setInt(4, 0);
                pstmt.executeUpdate();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeAll();
        }
        return 1;
    }


    //현재 채팅방 이름을 가져옴
    public String getCurrentChatRoomName(int chatRoomId) {
        String query = "SELECT chatRoomName FROM db_team.chatRoom WHERE chatRoom_id = ?";
        String title = null;
        try {
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(ChatRoomQuery.GETCURRENT_CHATROOM_NAME.getValue());
            pstmt.setInt(1, chatRoomId);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                title = rs.getString("chatRoomName");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeAll();
        }
        return title;
    }

    public ArrayList<ChatRoomDTO> getChatRoomNameList(String nickName) {
        ArrayList<ChatRoomDTO> roomName = null;
        try {
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(ChatRoomQuery.GETCHATROOMNAME_LIST.getValue());
            pstmt.setString(1, nickName);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                roomName = new ArrayList<ChatRoomDTO>();
                do{
                    ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
                    chatRoomDTO.setChatRoom_id(rs.getInt("chatRoom_id"));
                    chatRoomDTO.setChatRoomName(rs.getString("chatRoomName"));
                    chatRoomDTO.setNoticeRead(rs.getInt("isNoticeRead"));
                    chatRoomDTO.setVoted(rs.getInt("isVoted"));
                    roomName.add(chatRoomDTO);
                }while(rs.next());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeAll();
        }
        return roomName;
    }

    private void closeAll() {
        if(rs != null) try {rs.close();}catch(SQLException ex ) {}
        if(pstmt != null) try {pstmt.close();}catch(SQLException ex) {}
        if(conn != null) try {conn.close();}catch(SQLException ex) {}
    }

}