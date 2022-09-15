package com.goindol.teamtalk.client.dao;

import com.goindol.teamtalk.client.DB.DBDAO;
import com.goindol.teamtalk.client.dao.query.VoteQuery;
import com.goindol.teamtalk.client.dto.VoteDTO;
import com.goindol.teamtalk.client.dto.VoteResultDTO;
import com.goindol.teamtalk.client.dto.VoteVarDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VoteDAO {

    private static VoteDAO instance = null;

    private Connection conn = null;

    private PreparedStatement pstmt = null;

    private ResultSet rs = null;

    public static VoteDAO getInstance(){
        if(instance==null){
            instance = new VoteDAO();
        }
        return instance;
    }

    //투표 생성 (투표 리스트 다 담아서 만듦)
    public void creatVote(int chatRoom_id,String title,boolean isAnonymous,boolean isOverLap){
        try{
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(VoteQuery.CREATE_VOTE.getValue());
            pstmt.setInt(1,chatRoom_id);
            pstmt.setString(2,title);
            pstmt.setBoolean(3,isAnonymous);
            pstmt.setBoolean(4, isOverLap);
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement(VoteQuery.VOTE_INIT.getValue());
            pstmt.setInt(1, chatRoom_id);
            pstmt.executeUpdate();

        } catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeAll();
        }
    }

    public void createVoteVar(String content,int vote_id){
        try {
            conn = DBDAO.getConnection();


            pstmt = conn.prepareStatement(VoteQuery.CREATE_VOTEVAR.getValue());
            pstmt.setInt(1, vote_id);
            pstmt.setString(2, content);
            pstmt.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            closeAll();
        }
    }

    //투표를 생성할 때 먼저 Vote 테이블에 투표를 만들고 Vote_Var에 투표 리스트들을 다 넣어주기 위해서 Vote테이블에서
    // Vote_id를 가져오는 메소드
    public int getVoteId(int chatRoomId) {
        int voteId = 0;
        try {
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(VoteQuery.GETVOTEID.getValue());
            pstmt.setInt(1, chatRoomId);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                voteId = rs.getInt("vote_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeAll();
        }
        return voteId;
    }

    //투표가 생성되고 투표 리스트들을 확인하는 메소드
    public List<VoteVarDTO> readVoteVar(int vote_id){
        ArrayList<VoteVarDTO> v = null;

        try{
            conn = DBDAO.getConnection();
            pstmt= conn.prepareStatement(VoteQuery.READVOTEVAR.getValue());
            pstmt.setInt(1, vote_id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()){
                v = new ArrayList<VoteVarDTO>();
                do {
                    VoteVarDTO voteVarDTO = new VoteVarDTO();
                    voteVarDTO.setVoteVar_id(rs.getInt("voteVar_id"));
                    voteVarDTO.setVote_id(rs.getInt("vote_id"));
                    voteVarDTO.setContent(rs.getString("content"));
                    v.add(voteVarDTO);
                }while(rs.next());
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeAll();
        }
        return v;
    }

    public void deleteVote(int vote_id){
        try {
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(VoteQuery.DELETE_VOTE.getValue());
            pstmt.setInt(1,vote_id);
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement(VoteQuery.DELETE_VOTEVAR.getValue());
            pstmt.setInt(1,vote_id);
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement(VoteQuery.DELETE_VOTERESULT.getValue());
            pstmt.setInt(1,vote_id);
            pstmt.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeAll();
        }

    }


    //채팅방에서 해당 채팅 인원이 투표방에서 투표를 선택
    public void choiceVote(int vote_id, int chatRoom_id, String content,String nickName){
        try{

            conn = DBDAO.getConnection();

            pstmt = conn.prepareStatement(VoteQuery.CHOICEVOTE_VALIDATE.getValue());
            pstmt.setInt(1, vote_id);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                pstmt = conn.prepareStatement(VoteQuery.CHOICEVOTE_UPDATE.getValue());
                pstmt.setString(1, nickName);
                pstmt.setInt(2, chatRoom_id);
                pstmt.executeUpdate();

                boolean isOverLap = rs.getBoolean("isOverLap");
                pstmt = conn.prepareStatement(VoteQuery.CHOICEVOTE.getValue());
                pstmt.setInt(1,vote_id);
                pstmt.setString(2,content);
                pstmt.setString(3,nickName);
                pstmt.executeUpdate();
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeAll();
        }
    }

    //vote_id로 VoteDTO 조회
    public VoteDTO findVoteByVoteId(int vote_id, int chatRoom_id){
        VoteDTO voteDTO = null;

        try {
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(VoteQuery.FINDVOTEBYVOTEID.getValue());
            pstmt.setInt(1, vote_id);
            pstmt.setInt(2,chatRoom_id);
            rs = pstmt.executeQuery();
            if(rs.next()){
                String title = rs.getString("title");
                boolean isAnonymous = rs.getBoolean("isAnonymous");
                boolean isOverLap = rs.getBoolean("isOverLap");

                voteDTO = new VoteDTO(title, isAnonymous, isOverLap);
                voteDTO.setVote_id(vote_id);
                return voteDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeAll();
        }
        return voteDTO;
    }

    //투표가 있는지 체크
    public boolean checkVote(int chatRoom_id){
        boolean status=false;

        try {
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(VoteQuery.CHECKVOTE.getValue());
            pstmt.setInt(1, chatRoom_id);
            rs = pstmt.executeQuery();

            if(rs.next()){
                status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeAll();
        }
        return status;
    }

    //해당 유저가 투표를 했는지
    public boolean checkAlreadyVote(int voteId, String nickName) {
        boolean status = false;
        try {
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(VoteQuery.ISVOTE.getValue());
            pstmt.setInt(1, voteId);
            pstmt.setString(2, nickName);
            rs = pstmt.executeQuery();
            if(rs.next())
                status = true;
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            closeAll();
        }
        return status;
    }


    //투표 인원 체크
    public boolean isReadAllParticipants(int chatRoom_id) {

        try {
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(VoteQuery.ISALLVOTED_CHATROOMID.getValue());
            pstmt.setInt(1,chatRoom_id);
            rs = pstmt.executeQuery();

            if(rs.next()) {
                pstmt = conn.prepareStatement(VoteQuery.ISALLVOTED.getValue());
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


    //각 투표 리스트 별로 투표한 사람들 리스트 조회
    public List<String> getVoterByVoteVar(int vote_id, String content){
        List<String> result = null;

        try{
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(VoteQuery.GETVOTER.getValue());
            pstmt.setInt(1,vote_id);
            pstmt.setString(2,content);
            rs=pstmt.executeQuery();

            if (rs.next()){
                result = new ArrayList<String>();
                do {
                    String nickName = rs.getString("nickName");
                    result.add(nickName);
                }while(rs.next());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeAll();
        }
        return result;
    }

    //투표한 투표 리스트 보여주기
    public List<VoteResultDTO> showVoteList(int vote_id){

        ArrayList<VoteResultDTO> arr = null;

        try{
            conn = DBDAO.getConnection();
            pstmt= conn.prepareStatement(VoteQuery.GETVOTERESULT.getValue());
            pstmt.setInt(1,vote_id);
            rs = pstmt.executeQuery();

            if(rs.next()){
                arr = new ArrayList<>();
                do{
                    VoteResultDTO voteResultDTO = new VoteResultDTO();
                    voteResultDTO.setCount(rs.getInt("count"));
                    voteResultDTO.setContent(rs.getString("content"));
                    arr.add(voteResultDTO);
                }while(rs.next());
            }
            return arr;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeAll();
        }
        return arr;
    }

    private void closeAll() {
        if(rs != null) try {rs.close();}catch(SQLException ex ) {}
        if(pstmt != null) try {pstmt.close();}catch(SQLException ex) {}
        if(conn != null) try {conn.close();}catch(SQLException ex) {}
    }

}