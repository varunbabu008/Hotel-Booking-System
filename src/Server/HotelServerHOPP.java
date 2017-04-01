package Server;


import Server.DB.OrderDataAccess;
import Server.DB.UserDataAccess;
import Server.Entity.Hotel;

import java.util.ArrayList;
import java.util.List;

import Server.DB.HotelDataAccess;
import Server.Entity.Order;
import Server.Entity.User;
/**
 * Created by varunbabu on 31/3/17.
 */
public class HotelServerHOPP {

    public List<Hotel> queryResp(String input, String hotel){
        String[] arr = input.split(" ");
        List<Hotel> list1 = new ArrayList<>();
        //List<Hotel> list2 = new ArrayList<>();
        if(arr.length == 2){
            HotelDataAccess dao = new HotelDataAccess();
            java.sql.Date availableFrom = java.sql.Date.valueOf(arr[1]);

            if (hotel.equalsIgnoreCase(HotelBookingConstants.TAJ)) {
                list1 = dao.queryHotelListFromTAJ(arr[0],availableFrom);
            } else if (hotel.equalsIgnoreCase(HotelBookingConstants.BLU)) {
                list1 = dao.queryHotelListFromBLU(arr[0],availableFrom);
            } else if (hotel.equalsIgnoreCase(HotelBookingConstants.RAD)) {
                list1 = dao.queryHotelListfromRAD(arr[0], availableFrom);
            } else
                return null;
        }

        //if (list1 != null) {
          //  for (Hotel f : list1) {
            //    list1.add(f);
            //}
        //}
        return list1;
    }



    public boolean orderResp(String str) {

        boolean res = false;

        String[] arr = str.split(" ");
        HotelDataAccess hotelDao;
        OrderDataAccess orderDao;
        UserDataAccess userDao;

        /**
         * booking a room
         */
        if (arr.length == 2) {
            String hid = arr[0];
            String username = arr[1];

            hotelDao = new HotelDataAccess();
            orderDao = new OrderDataAccess();
            userDao = new UserDataAccess();

            if (hid.toUpperCase().startsWith(HotelBookingConstants.TAJ)) {
                if (hotelDao.queryRoomsExistedFromTAJ(hid) && userDao.queryUserExistedFromTAJ(username)) {
                    if(orderDao.orderExistedFromTAJ(hid, username))
                        System.out.println("Oder existed!");
                    else{
                        if (hotelDao.updateRoomsToTAJ(hid)) {
                            Order order = new Order();
                            order.setHid(hid);
                            order.setUsername(username);
                            boolean flag = orderDao.saveOrderToTAJ(order);
                            if (flag)
                                res = true;
                        }
                    }
                }
            } else if (hid.toUpperCase().startsWith(HotelBookingConstants.TAJ)) {
                if (hotelDao.queryRoomsExistedFromBLU(hid) && userDao.queryUserExistedFromBLU(username)) {
                    if(orderDao.orderExistedFromBLU(hid, username))
                        System.out.println("Order existed!");
                    else{
                        if (hotelDao.updateRoomsToBLU(hid)) {
                            Order order = new Order();
                            order.setHid(hid);
                            order.setUsername(username);
                            boolean flag = orderDao.saveOrderToBLU(order);
                            if (flag)
                                res = true;
                        }
                    }
                }
            } else if (hid.toUpperCase().startsWith(HotelBookingConstants.RAD)) {
                if (hotelDao.queryRoomsExistedFromRAD(hid) && userDao.queryUserExistedFromRAD(username)) {
                    if(orderDao.orderExistedFromRAD(hid, username))
                        System.out.println("Order existed!");
                    else{
                        if (hotelDao.updateRoomsToRAD(hid)) {
                            Order order = new Order();
                            order.setHid(hid);
                            order.setUsername(username);
                            boolean flag = orderDao.saveOrderToRAD(order);
                            if (flag)
                                res = true;
                        }
                    }
                }
            }
        }
        return res;
    }

    public boolean regResp(String str) {
        boolean flag = false;
        String[] arr = str.split(" ");
        if (arr.length == 5) {
            String hotel = arr[0];
            String username = arr[1];
            String phone = arr[2];
            String mail = arr[3];
            String credit = arr[4];

            User user = new User();
            user.setUsername(username);
            user.setPhone(phone);
            user.setEmail(mail);
            user.setCreditcard(credit);
            UserDataAccess dao = new UserDataAccess();
            if (hotel.equalsIgnoreCase(HotelBookingConstants.TAJ)) {
                if (!dao.queryUserExistedFromTAJ(username)) {
                    if (dao.insertUserToTAJ(user)) {
                        System.out
                                .println("----insert a new user successfully------");
                        flag = true;
                    }
                }
            } else if (hotel.equalsIgnoreCase(HotelBookingConstants.BLU)) {
                if (!dao.queryUserExistedFromBLU(username)) {
                    if (dao.insertUserToBLU(user)) {
                        System.out
                                .println("----insert a new user successfully------");
                        flag = true;
                    }
                }
            } else if (hotel.equalsIgnoreCase(HotelBookingConstants.RAD)) {
                if (!dao.queryUserExistedFromRAD(username))
                    if (dao.insertUserToRAD(user)) {
                        System.out
                                .println("----insert a new user successfully------");
                        flag = true;
                    }
            }
        }
        return flag;
    }

    public List<Order> checkOrders(String str) {
        String[] arr = str.split(" ");
        if (arr.length == 2) {
            String airline = arr[0];
            String username = arr[1];
            List<Order> list = new ArrayList<Order>();
            OrderDataAccess dao = new OrderDataAccess();
            UserDataAccess userDao = new UserDataAccess();
            if (airline.equalsIgnoreCase(HotelBookingConstants.TAJ)) {
                if (userDao.queryUserExistedFromTAJ(username))
                    list = dao.checkOrderFromTAJ(username);
                else
                    System.out.println("Cannot found this user!");
            } else if (airline.equalsIgnoreCase(HotelBookingConstants.BLU)) {
                if (userDao.queryUserExistedFromBLU(username))
                    list = dao.checkOrderFromBLU(username);
                else
                    System.out.println("Cannot found this user!");
            } else if (airline.equalsIgnoreCase(HotelBookingConstants.RAD)) {
                if (userDao.queryUserExistedFromRAD(username))
                    list = dao.checkOrderFromRAD(username);
                else
                    System.out.println("Cannot found this user!");
            } else
                list = null;
            return list;
        } else {
            return null;
        }
    }

}
