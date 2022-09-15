package com.goindol.teamtalk.client.dto;


public class VoteVarDTO {

    int voteVar_id;

    int vote_id;
    String content;

    public VoteVarDTO() {}
    public VoteVarDTO(int voteVar_id, int vote_id, String content) {
        this.voteVar_id = voteVar_id;
        this.vote_id = vote_id;
        this.content = content;
    }

    public int getVoteVar_id() {
        return voteVar_id;
    }

    public void setVoteVar_id(int voteVar_id) {
        this.voteVar_id = voteVar_id;
    }



    public VoteVarDTO(int vote_id, String content) {
        this.vote_id = vote_id;
        this.content = content;
    }

    public int getVote_id() {
        return vote_id;
    }

    public void setVote_id(int vote_id) {
        this.vote_id = vote_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }
}