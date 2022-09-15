package com.goindol.teamtalk.client.dto;

public class ChatRoomParticipantsDTO {
    private int chatRoomParticipants_id;
    private int chatRoom_id;
    private String nickName;
    private int isNoticeRead;

    public int getChatRoomParticipants_id() {
        return chatRoomParticipants_id;
    }

    public void setChatRoomParticipants_id(int chatRoomParticipants_id) {
        this.chatRoomParticipants_id = chatRoomParticipants_id;
    }

    public int getChatRoom_id() {
        return chatRoom_id;
    }

    public void setChatRoom_id(int chatRoom_id) {
        this.chatRoom_id = chatRoom_id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int isNoticeRead() {
        return isNoticeRead;
    }

    public void setNoticeRead(int noticeRead) {
        isNoticeRead = noticeRead;
    }
}
