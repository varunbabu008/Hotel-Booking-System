package Server.DB;
/**
 * Created by varunbabu on 1/4/17.
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Server.util.DBUtil;
import Server.Entity.User;

public class UserDataAccess {


    public boolean queryUserExistedFromTAJ(String username) {
        String sql = "select taj_username from taj_user where taj_username=? ";
        String columnName = "taj_username";
        return queryUserExistedFromServer(username, sql, columnName);
    }


    public boolean queryUserExistedFromBLU(String username) {
        String sql = "select blu_username from blu_user where blu_username=? ";
        String columnName = "blu_username";
        return queryUserExistedFromServer(username, sql, columnName);
    }


    public boolean queryUserExistedFromRAD(String username) {
        String sql = "select rad_username from rad_user where rad_username=? ";
        String columnName = "rad_username";
        return queryUserExistedFromServer(username, sql, columnName);
    }

    /**
     * public operation: query
     * @param username
     * @param sql
     * @return ture, if the user existed in the database
     */
    private boolean queryUserExistedFromServer(String username, String sql,String columnName) {
        DBUtil util = new DBUtil();
        Connection conn = util.getConn();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                String name = rs.getString(columnName);
                if(name!=null)
                    return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    public boolean insertUserToTAJ(User user) {
        String sql = "insert into taj_user values (?,?,?,?)";
        return insertNewUserToServer(user, sql);
    }
    public boolean insertUserToBLU(User user) {
        String sql = "insert into blu_user values (?,?,?,?)";
        return insertNewUserToServer(user, sql);
    }


    public boolean insertUserToRAD(User user) {
        String sql = "insert into rad_user values (?,?,?,?)";
        return insertNewUserToServer(user, sql);
    }


    private boolean insertNewUserToServer(User user, String sql) {
        DBUtil util = new DBUtil();
        Connection conn = util.getConn();

        try {
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername() );
            pstmt.setString(2, user.getPhone());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getCreditcard());
            int state = pstmt.executeUpdate();
            if(state==1)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }




}
