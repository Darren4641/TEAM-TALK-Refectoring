package com.goindol.teamtalk.client.dto;

public class ChatLogDTO {
    private int chatLog_id;
    private String nickName;
    private int chatRoom_id;
    private String content;
    private String regDate;

    public int getChatLog_id() {
        return chatLog_id;
    }

    public void setChatLog_id(int chatLog_id) {
        this.chatLog_id = chatLog_id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getChatRoom_id() {
        return chatRoom_id;
    }

    public void setChatRoom_id(int chatRoom_id) {
        this.chatRoom_id = chatRoom_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }
}
