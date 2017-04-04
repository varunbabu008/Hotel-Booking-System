package Client;

import java.io.*;

public class HotelBookingClient {


    protected BufferedReader console;
    protected HotelBookingClientHOPP clientHOPP;

    public static void main(String[] args){

        if (args.length != 1) {
            System.err.println("Usage: Client address");
            System.exit(1);
        }
        HotelBookingClient ui = new HotelBookingClient(args[0]);
        ui.loop();
    }

    public HotelBookingClient(String server) {

        clientHOPP = null;
        try {
            clientHOPP = new HotelBookingClientHOPP(server);
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        console = new BufferedReader(new InputStreamReader(System.in));
    }

    public void loop() {
        while (true) {
            String line = null;
            try {

                System.out.println("************ Welcome to Hotel Booking System ************");
                System.out.println("Hotel Booking request options: "
                        + Server.HotelBookingConstants.QUERY + " | "
                        + Server.HotelBookingConstants.REG + " | "
                        + Server.HotelBookingConstants.ORDER + " | "
                        + Server.HotelBookingConstants.CHECK + " | Enter "
                        + Server.HotelBookingConstants.QUIT + " to exit.");
                System.out.print("Enter request: ");
                line = console.readLine();
                System.out.println("Request was " + line);
            } catch(IOException e) {
                clientHOPP.quit();
                e.printStackTrace();
                System.exit(1);
            }
            // To find the available hotels

            if (line.toUpperCase().startsWith(HotelBookingConstants.QUERY)){

                query(losePrefix(line, HotelBookingConstants.QUERY));

                //Register a new user
            }else if (line.toUpperCase().startsWith(Server.HotelBookingConstants.REG)) {
                if(clientHOPP.checkInputOfReg(losePrefix(line, HotelBookingConstants.REG)))
                    register(losePrefix(line, HotelBookingConstants.REG));

                //Order a new Room
            } else if (line.toUpperCase().startsWith(HotelBookingConstants.ORDER)) {

                order(losePrefix(line, HotelBookingConstants.ORDER));

                //Check for ordered rooms in hotel
            } else if (line.toUpperCase().startsWith(HotelBookingConstants.CHECK)) {

                if(clientHOPP.checkInputOfCheck(losePrefix(line, HotelBookingConstants.CHECK)))

                    check(losePrefix(line,HotelBookingConstants.CHECK));

            } else if (line.equalsIgnoreCase(HotelBookingConstants.QUIT)) {
                quit();
            } else {
            }

        }
    }

    private void check(String input) {
        String[] list = clientHOPP.checkReq(input);
        for (String str : list) {
            System.out.println(str);
        }
    }

    private void register(String input) {
        if(clientHOPP.regReq(input)){
            System.out.println("New user registered!");
        }else
            System.out.println("Failed");;
    }

    private void order(String input) {
        if(clientHOPP.orderReq(input))
            System.out.println("ORDER successful");
        else
            System.out.println("Failed");
    }

    private void quit() {
        clientHOPP.quit();
        System.exit(0);
        //System.out.println("You have quit the system successfully!");
    }
    /**
     * Given that the string starts with the prefix,
     * get rid of the prefix and any whitespace
     */
    public String losePrefix(String str, String prefix) {
        int index = prefix.length();
        String ret = str.substring(index).trim();
        return ret;
    }



    private void query(String input) {
        String[] list = clientHOPP.queryReq(input);
        listOutput(list);
    }

    private void listOutput(String[] list) {
        if(list.length == 0){
            System.out.println("No Hotels available");
        } else {
            System.out.println("The available Hotels are: ");
            for (String str : list) {
                System.out.println(str);
            }
        }
    }

} // Client.HotelBookingClient