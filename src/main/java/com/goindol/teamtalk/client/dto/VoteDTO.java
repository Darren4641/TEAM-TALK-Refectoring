package com.goindol.teamtalk.client.dto;


public class VoteDTO {

    int vote_id;
    private int chatRoom_id;
    private String title;
    private boolean isAnonoymous;
    private boolean isOverLap;

    public VoteDTO(String title, boolean isAnonoymous, boolean isOverLap) {
        this.title = title;
        this.isAnonoymous = isAnonoymous;
        this.isOverLap = isOverLap;
    }

    public int getVote_id() {
        return vote_id;
    }

    public void setVote_id(int vote_id) {
        this.vote_id = vote_id;
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

    public boolean isAnonoymous() {
        return isAnonoymous;
    }

    public void setAnonoymous(boolean anonoymous) {
        isAnonoymous = anonoymous;
    }

    public boolean isOverLap() {
        return isOverLap;
    }

    public void setOverLap(boolean overLap) {
        isOverLap = overLap;
    }
}