package Client; /**
 * FileTransferTextClient.java
 *
 *
 *
 */
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class HotelBookingClientHOPP {

    protected Socket sock;
    protected BufferedReader reader;
    protected PrintStream writer;

    public HotelBookingClientHOPP(String server)
            throws UnknownHostException, IOException {

        InetAddress address = InetAddress.getByName(server);

        sock = null;
        InputStream in = null;
        OutputStream out = null;

        sock = new Socket(address, HotelBookingConstants.PORT);
        in = sock.getInputStream();
        out = sock.getOutputStream();

        reader = new BufferedReader(new InputStreamReader(in));
        writer = new PrintStream(out);
    }


    public String[] queryReq(String input) {
        //Broker Server listens on this
        writer.print(HotelBookingConstants.QUERY + " " + input
                + HotelBookingConstants.CR_LF);
        return readLinesFromServer();
    }

    public boolean regReq(String input) {
        writer.print(HotelBookingConstants.REG + " " + input
                + HotelBookingConstants.CR_LF);
        String response = null;
        boolean flag = false;
        while(true){
            try {
                response = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response.equals(HotelBookingConstants.SUCCEEDED))
                flag = true;
            else if (response.equals(HotelBookingConstants.ERROR))
                System.out.println("Output:ERROR");
            else if (response.equals(""))
                break;
            else {
                System.out.println("Unrecogized error!----->reg");
            }

        }
        return flag;
    }


    // order fid1 fid2 username
    // order fid username
    // client hopp writes it to the stream. Broker
    public boolean orderReq(String str) {
        writer.print(HotelBookingConstants.ORDER + " " + str
                + HotelBookingConstants.CR_LF);
        String response = null;
        Boolean flag = false;
        while(true){
            try {
                response = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response.equals(HotelBookingConstants.SUCCEEDED))
                flag = true;
            else if (response.equals(HotelBookingConstants.ERROR))
                flag = false;
            else if (response.equals("")){
                break;
            }
        }
        return flag;
    }
    /**
     * Public operation: read lines from server
     * Common codes for: query and check
     * @return string array
     */
    private String[] readLinesFromServer() {
        List<String> list = new ArrayList<String>();
        String line = null;
        while (true) {
            try {
                line = reader.readLine();
            } catch (IOException e) {
                break;
            }
            if (line.equals("")) {
                break;
            }
            if(line.toUpperCase().startsWith(HotelBookingConstants.TAJ)
                    ||line.toUpperCase().startsWith(HotelBookingConstants.RAD)
                    ||line.toUpperCase().startsWith(HotelBookingConstants.BLU)){
                list.add(line);
            }
            //list.add(line);
        }
        String[] resultList = new String[list.size()];
        list.toArray(resultList);
        return resultList;
    }

    public boolean checkInputOfQuery(String input) {
        String[] arr = input.split(" ");
        if(arr.length ==2 ){
            String city = arr[0];
            String Date = arr[1];
            return true;
        }
       return false;

    }

    public boolean checkInputOfCheck(String input) {
        //check <hotel, username>
        boolean flag = false;
        String[] arr = input.split(" ");
        String hotel = arr[0];
        String username = arr[1];
        String airRE = "(taj|blu|rad)";
        String nameRE = "^[A-Za-z]+$";
        if(arr.length == 2){
            if(hotel.matches(airRE))
                if(username.matches(nameRE))
                    flag = true;
                else
                    System.out.println("Username has Illegal characters!");
            else{
                System.out.println("Only taj|rad|blu are supported!");
            }
        }

        return flag;
    }

    public boolean checkInputOfReg(String input) {
        // reg <hotel,username,phone,email,creditcard>
        String[] arr = input.split(" ");
        String hotel = arr[0];
        String username = arr[1];
        String phone = arr[2];
        String email = arr[3];
        String creditcard = arr[4];

        String airRE = "(taj|blu|rad)";
        String nameRE = "^[A-Za-z]+$";
        String phoneRE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        String mailRE = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        String creditcardRE = "[0-9]{16}";

        if(arr.length == 5){
            if(hotel.matches(airRE))
                if(username.matches(nameRE))
                    if(phone.matches(phoneRE))
                        if(email.matches(mailRE))
                            if(creditcard.matches(creditcardRE))
                                return true;
                            else
                                System.out.println("Illegal creditcard number, 16bits!");
                        else
                            System.out.println("Illegal email address!");
                    else
                        System.out.println("Illegal phone number");
                else
                    System.out.println("Illegal name!");
            else
                System.out.println("Only support: taj|blu|rad");
        }else{
            System.out.println("Format: REG <airline,username,phone,email,creditcard>");
        }
        return false;
    }

    public void quit() {
        try {
            writer.print(HotelBookingConstants.QUIT
                    + HotelBookingConstants.CR_LF);
            reader.close();
            writer.close();
            sock.close();
            System.out.println("You have quit the system successfully!");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] checkReq(String input) {
        writer.print(HotelBookingConstants.CHECK + " " + input
                + HotelBookingConstants.CR_LF);
        return readLinesFromServer();
    }

// for order hotels
    public boolean checkInputOfOrder(String input) {

        //order cea030110 varun
        // in the format hotelID , username

        String[] arr = input.split(" ");

         if(arr.length == 2){
            String hid = arr[1];
            String username = arr[3];
            String hidRE = "(taj|blu|rad)0[1-4]0[1-4][0-3][0-9]";
            String nameRE = "^[A-Za-z]+$";
            if(hid.matches(hidRE))
                if(username.matches(nameRE))
                    return true;
                else
                    System.out.println("Illegal name");
            else
                System.out.println("Illegal HID number");
        }
        return false;
    }

    public String get(String filename) throws IOException {
        // stub
        return "";
    }
} // Client.HotelBookingClientHOPP