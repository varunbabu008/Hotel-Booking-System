package Broker;

import java.io.IOException;
/**
 * Created by varunbabu on 31/3/17.
 */
public class BrokerServerHOPP {
    protected BrokerClientHOPP bcHOPPtoTAJ, bcHOPPtoBLU, bcHOPPtoRAD;

    public BrokerServerHOPP() {
        String server = "localhost";
        bcHOPPtoTAJ = null;
        bcHOPPtoBLU = null;
        bcHOPPtoRAD = null;
        try {
            bcHOPPtoTAJ = new BrokerClientHOPP(server,HotelBookingConstants.PORT_TAJ);
            bcHOPPtoBLU = new BrokerClientHOPP(server,HotelBookingConstants.PORT_BLU);
            bcHOPPtoRAD = new BrokerClientHOPP(server,HotelBookingConstants.PORT_RAD);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String[] queryResp(String str) {
        String[] tajResp = bcHOPPtoTAJ.queryReq(str);
        String[] bluResp = bcHOPPtoBLU.queryReq(str);
        String[] radResp = bcHOPPtoRAD.queryReq(str);
        String[] result = new String[tajResp.length+bluResp.length+radResp.length];
        System.arraycopy(tajResp, 0, result, 0, tajResp.length);
        System.arraycopy(bluResp, 0, result, tajResp.length, bluResp.length);
        System.arraycopy(radResp, 0, result, tajResp.length+bluResp.length, radResp.length);
        return result;
    }


    public boolean regResp(String str) {
        boolean flag = false;
        if(str.toUpperCase().startsWith(HotelBookingConstants.TAJ))
            flag = bcHOPPtoTAJ.regReq(str);
        else if(str.toUpperCase().startsWith(HotelBookingConstants.BLU))
            flag = bcHOPPtoBLU.regReq(str);
        else if(str.toUpperCase().startsWith(HotelBookingConstants.RAD))
            flag = bcHOPPtoRAD.regReq(str);
        else{
            System.out.println("Unrecognized Hotel!");
        }
        return flag;
    }


    public boolean orderResp(String str) {
        boolean flag = false;
        if(str.toUpperCase().startsWith(HotelBookingConstants.TAJ))
            flag = bcHOPPtoTAJ.orderReq(str);
        else if(str.toUpperCase().startsWith(HotelBookingConstants.BLU))
            flag = bcHOPPtoBLU.orderReq(str);
        else if(str.toUpperCase().startsWith(HotelBookingConstants.RAD))
            flag = bcHOPPtoRAD.orderReq(str);
        else{
            System.out.println("Unrecognized HID!");
        }
        return flag;
    }


    public String[] checkOrders(String str) {
        String[] result = null;
        if(str.toUpperCase().startsWith(HotelBookingConstants.TAJ))
            result = bcHOPPtoTAJ.checkReq(str);
        else if(str.toUpperCase().startsWith(HotelBookingConstants.BLU))
            result = bcHOPPtoBLU.checkReq(str);
        else if(str.toUpperCase().startsWith(HotelBookingConstants.RAD))
            result = bcHOPPtoRAD.checkReq(str);
        else
            System.out.println("Unrecognized Hotel! >>>"+str+"<<<");
        return result;
    }


    public void quit() {
        bcHOPPtoTAJ.quit();
        bcHOPPtoBLU.quit();
        bcHOPPtoRAD.quit();
    }

}