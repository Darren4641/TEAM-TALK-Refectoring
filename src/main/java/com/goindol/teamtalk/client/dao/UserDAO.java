package com.goindol.teamtalk.client.dao;

import com.goindol.teamtalk.client.DB.DBDAO;
import com.goindol.teamtalk.client.dao.query.UserQuery;
import com.goindol.teamtalk.client.dto.UserDTO;

import java.sql.*;

public class UserDAO {
    private static UserDAO instance = null;

    private static DBDAO DB = DBDAO.getInstance();

    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    //싱글톤 패턴(객체를 단 1개만 생성)
    public static UserDAO getInstance() {
        if (instance == null)
            instance = new UserDAO();
        return instance;
    }

    public synchronized void signUp(String userId, String userPassword, String nickName) {
        try {
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(UserQuery.SIGNUP.getValue());
            pstmt.setString(1, userId);
            pstmt.setString(2, userPassword);
            pstmt.setString(3, nickName);
            pstmt.setBoolean(4, false);

            pstmt.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            System.out.println("중복된 아이디입니다.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
    }


    public synchronized int validateSignUp(String userId, String nickName) {
        int status = 0;

        try {
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(UserQuery.VALIDATESIGNUP_NICKNAME.getValue());
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                status = 1;
                return status;
            }

            pstmt = conn.prepareStatement(UserQuery.VALIDATESIGNUP_ID.getValue());
            pstmt.setString(1, nickName);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                status = 2;
                return status;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
        return status;
    }


    public synchronized int login(String userId, String userPassword) {
        int status = 0;

        try {
            conn = DBDAO.getConnection();


            pstmt = conn.prepareStatement(UserQuery.LOGIN.getValue());
            pstmt.setString(1, userId);
            pstmt.setString(2, userPassword);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                pstmt = conn.prepareStatement(UserQuery.LOGIN_OVERLAP.getValue());
                pstmt.setString(1, userId);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    if (!rs.getBoolean("status")) {
                        pstmt = conn.prepareStatement(UserQuery.LOGIN_STATUS.getValue());
                        pstmt.setString(1, userId);
                        pstmt.executeUpdate();

                        pstmt = conn.prepareStatement(UserQuery.LOGIN_FRIEND.getValue());
                        pstmt.setString(1, rs.getString("nickName"));
                        pstmt.executeUpdate();

                        status = 3;
                    } else {
                        status = 2;
                    }
                }
            } else
                return 1;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
        return status;
    }


    public synchronized UserDTO getUser(String userId, String userPassword) {
        UserDTO userDTO = null;
        try {
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(UserQuery.GETUSER.getValue());
            pstmt.setString(1, userId);
            pstmt.setString(2, userPassword);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                userDTO = new UserDTO();
                userDTO.setUserId(rs.getString("userId"));
                userDTO.setUserPassword(rs.getString("userPassword"));
                userDTO.setNickName(rs.getString("nickName"));
                userDTO.setStatus(rs.getBoolean("status"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
        return userDTO;
    }


    public synchronized void logout(String userId, String nickName) {

        try {
            conn = DBDAO.getConnection();
            pstmt = conn.prepareStatement(UserQuery.LOGOUT.getValue());
            pstmt.setString(1, userId);
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement(UserQuery.LOGOUT_FRIEND.getValue());
            pstmt.setString(1, nickName);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
    }

    private void closeAll() {
        if(rs != null) try {rs.close();}catch(SQLException ex ) {}
        if(pstmt != null) try {pstmt.close();}catch(SQLException ex) {}
        if(conn != null) try {conn.close();}catch(SQLException ex) {}
    }

}