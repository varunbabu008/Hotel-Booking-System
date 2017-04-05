package Server;


import Server.Entity.Hotel;
import Server.Entity.Order;
import java.lang.Thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Created by varunbabu on 31/3/17.
 */
public class HotelServer {

    public static void main(String[] args){

        ServerSocket s = null;
        String hotel = null;
        if(args.length != 1){
            System.err.println("Usage: hotel name");
            return;
        }

        try{
            if(args[0].equalsIgnoreCase(HotelBookingConstants.BLU)){
                s = new ServerSocket(HotelBookingConstants.PORT_BLU);
                hotel = "blu";
                System.out.println("Bluemount Hotel Server is running");
            }
            else if(args[0].equalsIgnoreCase(HotelBookingConstants.TAJ)){
                s = new ServerSocket(HotelBookingConstants.PORT_TAJ);
                hotel = "taj";
                System.out.println("Taj Hotel Server is running");
            }
            else if(args[0].equalsIgnoreCase(HotelBookingConstants.RAD)){
                s = new ServerSocket(HotelBookingConstants.PORT_RAD);
                hotel = "rad";
                System.out.println("Radisson Hotel Server is running");
            }
            else
                System.out.println("Only Bluemount, Taj and Radisson Hotel servers are supported at the moment");
        }
        catch (IOException e){
            e.printStackTrace();
            return;
        }

        while (true) {
            Socket incoming = null;
            try {
                incoming = s.accept();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            new HotelHandler(incoming, hotel).start();
        }

    }

}

class HotelHandler extends Thread {
    Socket incoming;
    BufferedReader reader;
    PrintStream writer;
    HotelServerHOPP hopp = new HotelServerHOPP();
    String hotel;

    public HotelHandler(Socket incoming,String hotel){
        super();
        this.incoming = incoming;
        this.hotel = hotel;
    }

    public void run(){
        try {
            reader = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
            writer = new PrintStream(incoming.getOutputStream());
            writer.print("Hotel server send response " + Thread.currentThread().getName() + HotelBookingConstants.CR_LF);
            while (true) {

                String line = reader.readLine();
                if (line == null)
                    break;
                System.out.println("Received request: " + line);

                if (line.startsWith(HotelBookingConstants.QUERY))

                    queryResponse(losePrefix(line, HotelBookingConstants.QUERY), hotel);

                else if (line.startsWith(HotelBookingConstants.REG))

                    regResponse(losePrefix(line, HotelBookingConstants.REG));

                else if (line.startsWith(HotelBookingConstants.ORDER))

                    orderResponse(losePrefix(line, HotelBookingConstants.ORDER));

                else if (line.startsWith(HotelBookingConstants.CHECK))

                    checkResponse(losePrefix(line, HotelBookingConstants.CHECK));

                else if (line.startsWith(HotelBookingConstants.QUIT)) {
                    quit();
                    break;
                } else {
                    writer.println(HotelBookingConstants.ERROR + HotelBookingConstants.CR_LF);
                }

            }
            incoming.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void quit(){

    }
    //Synchronized Only one thread gets access at a time
    private synchronized void checkResponse(String str) {
        List<Order> list = hopp.checkOrders(str);
        if (list == null){
            writer.print(HotelBookingConstants.ERROR
                    + HotelBookingConstants.CR_LF);
        } else {
            String msg;
            for (Order order : list) {
                msg = order.getHid();
                System.out.println(msg);
                writer.print(msg+HotelBookingConstants.CR_LF);
            }
            writer.print(HotelBookingConstants.CR_LF);
        }
    }

    private synchronized void regResponse(String str) {
        if(hopp.regResp(str)){
            writer.print(HotelBookingConstants.SUCCEEDED+HotelBookingConstants.CR_LF);
        }else{
            writer.print(HotelBookingConstants.ERROR+HotelBookingConstants.CR_LF);
        }
        writer.print(HotelBookingConstants.CR_LF);
    }

    private synchronized void orderResponse(String str) {
        if(hopp.orderResp(str)){
            //communicates via hotelServerHopp
            //System.out.println(hopp.orderResp(str));
            writer.print(HotelBookingConstants.SUCCEEDED+HotelBookingConstants.CR_LF);
        }else{
            System.out.println("----->ERROR");
            writer.print(HotelBookingConstants.ERROR+HotelBookingConstants.CR_LF);
        }
        System.out.println("--->crlf");
        writer.print(HotelBookingConstants.CR_LF);
    }

    private synchronized void queryResponse(String query,String hotel) {
        List<Hotel> list = hopp.queryResp(query,hotel);
        if (list == null){
            writer.print(HotelBookingConstants.ERROR
                    + HotelBookingConstants.CR_LF);
        } else {
            for (Hotel h : list) {
                String msg = h.getHid()+" [AvailableFrom] "+h.getAvailableFrom()+ "[AvailableTill] "+h.getAvailableTill()+" [Rooms] "+h.getRooms()+" [price] "+h.getPrice();
                System.out.println(msg);
                writer.print(msg + HotelBookingConstants.CR_LF);
            }
        }
        writer.print(HotelBookingConstants.CR_LF);
    }

    private String losePrefix(String str, String prefix) {
        int index = prefix.length();
        String ret = str.substring(index).trim();
        return ret;
    }


}
