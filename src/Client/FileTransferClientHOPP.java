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

public class FileTransferClientHOPP {

    protected Socket sock;
    protected BufferedReader reader;
    protected PrintStream writer;

    public FileTransferClientHOPP(String server)
            throws UnknownHostException, IOException {

        InetAddress address = InetAddress.getByName(server);

        sock = null;
        InputStream in = null;
        OutputStream out = null;

        sock = new Socket(address, FileTransferTextConstants.PORT);
        in = sock.getInputStream();
        out = sock.getOutputStream();

        reader = new BufferedReader(new InputStreamReader(in));
        writer = new PrintStream(out);
    }


    public String[] queryReq(String input) {
        writer.print(FileTransferTextConstants.QUERY + " " + input
                + FileTransferTextConstants.CR_LF);
        return readLinesFromServer();
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
            if(line.toUpperCase().startsWith(FileTransferTextConstants.TAJServer)
                    ||line.toUpperCase().startsWith(FileTransferTextConstants.RadissonServer)
                    ||line.toUpperCase().startsWith(FileTransferTextConstants.BluemountServer)){
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
        if(arr.length !=0 ){
            String city = arr[0];
            String Date = arr[1];
            return true;
        }
       return false;

    }

    public void quit() {
        try {
            writer.print(FileTransferTextConstants.QUIT +
                    FileTransferTextConstants.CR_LF);
            reader.close();
            writer.close();
            sock.close();
        } catch(Exception e) {
            // ignore
        }
    }

    public String[] dir() {
        // stub
        return null;
    }

    public boolean chdir(String dir) {
        writer.print(FileTransferTextConstants.CD + " " + dir +
                FileTransferTextConstants.CR_LF);

        String response = null;
        try {
            response = reader.readLine();
        } catch(IOException e) {
            return false;
        }
        if (response.startsWith(FileTransferTextConstants.SUCCEEDED)) {
            return true;
        } else {
            return false;
        }
    }

    public String get(String filename) throws IOException {
        // stub
        return "";
    }
} // Client.FileTransferClientHOPP