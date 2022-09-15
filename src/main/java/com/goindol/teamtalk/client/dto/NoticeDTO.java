package com.goindol.teamtalk.client.dto;

public class NoticeDTO {
    private int notice_id;
    private String nickName;


    private int chatRoom_id;
    private String title;
    private String content;

    public NoticeDTO() {}

    public NoticeDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public int getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(int notice_id) {
        this.notice_id = notice_id;
    }


    public int getChatRoom_id() {
        return chatRoom_id;
    }

    public void setChatRoom_id(int chatRoom_id) {
        this.chatRoom_id = chatRoom_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}