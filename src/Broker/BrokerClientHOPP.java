package Broker;

/**
 * Created by varunbabu on 31/3/17.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
public class BrokerClientHOPP {
    protected Socket socket;
    protected BufferedReader reader;
    protected PrintStream writer;

    public BrokerClientHOPP(String server,int PORT) throws IOException {
        InetAddress address = InetAddress.getByName(server);
        socket = null;
        InputStream inStream = null;
        OutputStream outStream = null;

        socket = new Socket(address, PORT);
        inStream = socket.getInputStream();
        outStream = socket.getOutputStream();

        reader = new BufferedReader(new InputStreamReader(inStream));
        writer = new PrintStream(outStream);
    }

    public String[] queryReq(String input) {
        writer.print(HotelBookingConstants.QUERY+" "+input+HotelBookingConstants.CR_LF);
        List<String> list = new ArrayList<String>();
        String response = null;
        try {
            response = reader.readLine();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if(response!=null){
            String line = null;
            while(true){
                try {
                    line = reader.readLine();
                    //line = response;
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }if(line.equals(""))
                    break;
                list.add(line);
            }
        }
        String[] hotelList = new String[list.size()];
        list.toArray(hotelList);
        return hotelList;
    }

    public String[] checkReq(String input) {
        writer.print(HotelBookingConstants.CHECK+" "+input+HotelBookingConstants.CR_LF);
        List<String> list = new ArrayList<String>();
        String line = null;

        while(true){
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }if(line.equals(""))
                break;
            if(line.toUpperCase().startsWith(HotelBookingConstants.TAJ)
                    ||line.toUpperCase().startsWith(HotelBookingConstants.BLU)
                    ||line.toUpperCase().startsWith(HotelBookingConstants.RAD)){
                System.out.println(">>>>>"+line);
                list.add(line);
            }
        }
        String[] orderList = new String[list.size()];
        list.toArray(orderList);
        return orderList;
    }

    public boolean regReq(String input) {
        writer.print(HotelBookingConstants.REG+" "+input+HotelBookingConstants.CR_LF);
        boolean flag = false;
        String line;
        try {
            line = reader.readLine();
            if(line.equals(HotelBookingConstants.ERROR))
                flag = false;
            else if(line.equals(HotelBookingConstants.SUCCEEDED))
                flag = true;
            else
                System.out.println("Unrecognized result! >>>>"+line+"<<<<");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean orderReq(String str) {
        writer.print(HotelBookingConstants.ORDER+" "+str+HotelBookingConstants.CR_LF);
        String response=null;
        boolean flag = false;

        while(true){
            try {
                response = reader.readLine();
                System.out.println(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(response.equals(HotelBookingConstants.SUCCEEDED)){
                return true;
            }
            else if(response.equals(HotelBookingConstants.ERROR))
                System.out.println("ERROR");
            else if(response.equals(""))
                break;
            else
                System.out.println(response);
        }

        return flag;
    }

    public void quit() {
        writer.print(HotelBookingConstants.QUIT+HotelBookingConstants.CR_LF);
    }

}