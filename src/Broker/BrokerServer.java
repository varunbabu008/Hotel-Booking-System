package Broker;

/**
 * Created by varunbabu on 31/3/17.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;


import Broker.BrokerServerHOPP;

public class BrokerServer {

    public static void main(String[] args) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(HotelBookingConstants.PORT);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Broker server is running.");
        while (true) {
            Socket incoming = null;
            try {
                incoming = server.accept();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            new BrokerSocketHandler(incoming).start();
        }
    }
}

class BrokerSocketHandler extends Thread {

    Socket incoming;
    BufferedReader reader;
    PrintStream writer;
    OutputStream outstream;
    // server-side HOPP
    protected BrokerServerHOPP bsHOPP = new BrokerServerHOPP();

    BrokerSocketHandler(Socket incoming) {
        this.incoming = incoming;
    }

    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(
                    incoming.getInputStream()));
            outstream = incoming.getOutputStream();
            writer = new PrintStream(outstream);
            while (true) {
                try {
                    //reading from HotelBookingClientHopp and pass on to brokerServerHopp
                    String line = reader.readLine();
                    if (line == null)
                        break;
                    System.out.println("Received request: " + line);
                    if (line.startsWith(HotelBookingConstants.QUERY))
                        queryResponse(losePrefix(line,
                                HotelBookingConstants.QUERY));
                    else if (line.startsWith(HotelBookingConstants.REG))
                        registerResponse(losePrefix(line, HotelBookingConstants.REG));
                    else if (line.startsWith(HotelBookingConstants.ORDER))
                        orderResponse(losePrefix(line, HotelBookingConstants.ORDER));
                    else if (line.startsWith(HotelBookingConstants.CHECK))
                        checkResponse(losePrefix(line, HotelBookingConstants.CHECK));
                    else if (line.startsWith(HotelBookingConstants.QUIT))
                        quit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            incoming.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void quit() {
        bsHOPP.quit();
    }

    private void checkResponse(String str) {
        String[] rs = bsHOPP.checkOrders(str);
        for (String line : rs) {
            System.out.println(line+"<<<---");
            writer.print(line + HotelBookingConstants.CR_LF);
        }
        writer.print(HotelBookingConstants.CR_LF);
    }

    private void orderResponse(String str) {
        boolean flag = bsHOPP.orderResp(str);
        if(flag)
            writer.print(HotelBookingConstants.SUCCEEDED+HotelBookingConstants.CR_LF);
        else
            writer.print(HotelBookingConstants.ERROR+HotelBookingConstants.CR_LF);
        writer.print(HotelBookingConstants.CR_LF);
    }

    private void registerResponse(String str) {
        boolean flag = bsHOPP.regResp(str);
        if(flag)
            writer.print(HotelBookingConstants.SUCCEEDED+HotelBookingConstants.CR_LF);
        else
            writer.print(HotelBookingConstants.ERROR+HotelBookingConstants.CR_LF);
        writer.print(HotelBookingConstants.CR_LF);
    }

    private void queryResponse(String str) {
        //gets the result from the brokerServerHopp
        String[] rs = bsHOPP.queryResp(str);
        for (String line : rs) {
            System.out.println(line);
            writer.print(line + HotelBookingConstants.CR_LF);
        }
        writer.print(HotelBookingConstants.CR_LF);
    }

    private String losePrefix(String str, String prefix) {
        int index = prefix.length();
        String ret = str.substring(index).trim();
        return ret;
    }
}
