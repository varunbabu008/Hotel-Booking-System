package Server.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import Server.Entity.Order;
import Server.util.DBUtil;

public class OrderDataAccess  {

    public boolean saveOrderToTAJ(Order order) {
        String sql = "insert into taj_order values (?,?,?) ";
        return saveOrderToServer(order, sql);
    }



    public boolean saveOrderToBLU(Order order) {
        String sql = "insert into blu_order values (?,?,?) ";
        return saveOrderToServer(order, sql);
    }


    public boolean saveOrderToRAD(Order order) {
        String sql = "insert into rad_order values (?,?,?) ";
        return saveOrderToServer(order, sql);
    }

    /**
     * public operation: saveOrderToServer
     * @param order
     * @param sql
     * @return
     */
    private boolean saveOrderToServer(Order order, String sql) {
        DBUtil util = new DBUtil();
        Connection conn = util.getConn();

        try {
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setInt(1, order.getOid());
            pstmt.setString(2, order.getHid());
            pstmt.setString(3, order.getUsername());
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



    public List<Order> checkOrderFromTAJ(String username) {
        String sql = "select * from taj_order where taj_username=?";
        String column_hid = "taj_hid";
        return checkOrderFromServer(username, sql, column_hid);
    }


    public List<Order> checkOrderFromBLU(String username) {
        String sql = "select * from blu_order where blu_username=?";
        String column_hid = "blu_hid";
        return checkOrderFromServer(username, sql, column_hid);
    }



    public List<Order> checkOrderFromRAD(String username) {
        String sql = "select * from rad_order where rad_username=?";
        String column_hid = "rad_hid";
        return checkOrderFromServer(username, sql, column_hid);
    }

    private List<Order> checkOrderFromServer(String username, String sql,
                                             String column_fid) {
        DBUtil util =new DBUtil();
        Connection conn = util.getConn();

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            List<Order> list = new ArrayList<Order>();
            while(rs.next()){
                Order order = new Order();
                int oid = rs.getInt("oid");
                String fid = rs.getString(column_fid);
                order.setOid(oid);
                order.setHid(fid);
                order.setUsername(username);
                list.add(order);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public boolean orderExistedFromTAJ(String hid,String username) {
        String sql = "select taj_hid from taj_order where taj_hid=? and taj_username=? ";
        String columnName = "taj_hid";
        return orderExistedFromServer(hid,username, sql, columnName);
    }



    public boolean orderExistedFromBLU(String hid,String username) {
        String sql = "select blu_hid from blu_order where blu_hid=? and blu_username=? ";
        String columnName = "blu_hid";
        return orderExistedFromServer(hid,username, sql, columnName);
    }



    public boolean orderExistedFromRAD(String hid,String username) {
        String sql = "select rad_hid from rad_order where rad_hid=? and rad_username=? ";
        String columnName = "rad_hid";
        return orderExistedFromServer(hid, username, sql, columnName);
    }

    private boolean orderExistedFromServer(String hid, String username, String sql,String columnName) {
        DBUtil util = new DBUtil();
        Connection conn = util.getConn();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, hid);
            pstmt.setString(2, username);
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
}
