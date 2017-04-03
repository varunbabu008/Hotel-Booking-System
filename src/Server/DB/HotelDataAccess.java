package Server.DB;

/**
 * Created by varunbabu on 1/4/17.
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import Server.Entity.Hotel;
import Server.util.DBUtil;

public class HotelDataAccess {


    public boolean queryRoomsExistedFromTAJ(String hid) {
        String sql = "select taj_hid,rooms from taj_hotel where taj_hid=? ";
        return queryRoomsExistedFromServer(hid, sql);
    }


    public boolean queryRoomsExistedFromBLU(String hid) {
        String sql = "select blu_hid,rooms from blu_hotel where blu_hid=? ";
        return queryRoomsExistedFromServer(hid, sql);
    }


    public boolean queryRoomsExistedFromRAD(String hid) {
        String sql = "select rad_hid,rooms from rad_hotel where qan_fid=? ";
        return queryRoomsExistedFromServer(hid, sql);
    }


    public boolean updateRoomsToTAJ(String hid) {
        String sql = "update taj_hotel set rooms=rooms-1 where taj_hid = ? ";
        return updateRoomsToServer(hid, sql);
    }


    public boolean updateRoomsToBLU(String hid) {
        String sql = "update blu_hotel set rooms=rooms-1 where blu_hid = ? ";
        return updateRoomsToServer(hid, sql);
    }



    public boolean updateRoomsToRAD(String hid) {
        String sql = "update rad_hotel set rooms=rooms-1 where rad_hid = ? ";
        return updateRoomsToServer(hid, sql);
    }

    /**
     * public operation: query ticket existed from server
     *
     * @param hid
     * @param sql
     * @return
     */
    private boolean queryRoomsExistedFromServer(String hid, String sql) {
        DBUtil util = new DBUtil();
        Connection conn = util.getConn();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            //passing the parameter hid
            pstmt.setString(1, hid);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                int room = rs.getInt("rooms");
                if(room > 0)
                    return true;
            }
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

    /**
     * public operation: update tickets to server
     * @param hid
     * @param sql
     * @return
     */
    private boolean updateRoomsToServer(String hid, String sql) {
        DBUtil util = new DBUtil();
        Connection conn = util.getConn();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,hid);
            int status = pstmt.executeUpdate();
            if(status == 1)
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


    public boolean insertHotelToTAJ(Hotel hotel) {
        String sql = "insert into taj_hotel values (?,?,?,?,?,?,?)";
        return insertHotelToDB(hotel, sql);
    }


    public boolean insertHotelToBLU(Hotel hotel) {
        String sql = "insert into blu_hotel values (?,?,?,?,?,?,?)";
        return insertHotelToDB(hotel, sql);
    }

    public boolean insertHotelToRAD(Hotel hotel) {
        String sql = "insert into rad_hotel values (?,?,?,?,?,?,?)";
        return insertHotelToDB(hotel, sql);
    }

    private boolean insertHotelToDB(Hotel hotel, String sql) {
        DBUtil util = new DBUtil();
        Connection conn = util.getConn();

        try {
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setString(1, hotel.getHid());
            pstmt.setString(2, hotel.getHotelname());
            pstmt.setString(3, hotel.getHotelCity());
            pstmt.setInt(4, hotel.getPrice());
            pstmt.setInt(5, hotel.getRooms());
            pstmt.setDate(6, (java.sql.Date) hotel.getAvailableFrom());
            pstmt.setDate(7, (java.sql.Date) hotel.getAvailableTill());
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



    public List<Hotel> queryHotelListFromTAJ(String city,
                                               java.sql.Date availableFrom) {
        String sql = "select * from taj_hotel where hotelCity=? and availableFrom = ? ";
        String hid_column_name = "taj_hid";
        return queryHotelListFromServer(city,availableFrom, sql, hid_column_name);
    }


    public List<Hotel> queryHotelListFromBLU(String city, java.sql.Date availableFrom) {
        String sql = "select * from blu_hotel where hotelCity=? and availableFrom = ? ";
        String hid_column_name = "blu_hid";
        return queryHotelListFromServer(city, availableFrom, sql, hid_column_name);
    }


    public List<Hotel> queryHotelListfromRAD(String city, java.sql.Date availableFrom) {
        String sql = "select * from rad_hotel where hotelCity=? and availableFrom = ? ";
        String hid_column_name = "rad_hid";
        return queryHotelListFromServer(city,availableFrom, sql, hid_column_name);
    }

    private List<Hotel> queryHotelListFromServer(String city, java.sql.Date availableFrom, String sql, String hid_column_name) {
        DBUtil util = new DBUtil();
        Connection conn = util.getConn();

        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1,city);
            pstmt.setDate(2, availableFrom);


            ResultSet rs = pstmt.executeQuery();
            List<Hotel> list = new ArrayList<Hotel>();
            while (rs.next()) {
                String hid = rs.getString(hid_column_name);
                String hotelName = rs.getString("hotelName");
                String hotelCity = rs.getString("hotelCity");
                Date availableFromDate = rs.getDate("availableFrom");
                Date availableTillDate = rs.getDate("availableFrom");
                int rooms = rs.getInt("rooms");
                int price = rs.getInt("price");
                Hotel hotel = new Hotel();
                hotel.setHid(hid);
                hotel.setHotelname(hotelName);
                hotel.setHotelCity(hotelCity);
                hotel.setPrice(price);
                hotel.setRooms(rooms);
                hotel.setAvailableFrom(availableFromDate);
                hotel.setAvailableTill(availableTillDate);
                list.add(hotel);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}