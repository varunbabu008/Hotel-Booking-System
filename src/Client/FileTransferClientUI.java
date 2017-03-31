package Client;

import java.io.*;

public class FileTransferClientUI {

    private static String DIR = "dir";
    private static String QUIT = "quit";
    private static String CD = "cd";
    private static String GET ="get";

    protected BufferedReader console;
    protected FileTransferClientHOPP clientHOPP;

    public static void main(String[] args){

        if (args.length != 1) {
            System.err.println("Usage: Client address");
            System.exit(1);
        }
        FileTransferClientUI ui = new FileTransferClientUI(args[0]);
        ui.loop();
    }

    public FileTransferClientUI(String server) {

        clientHOPP = null;
        try {
            clientHOPP = new FileTransferClientHOPP(server);
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
                System.out.print("Enter request: ");
                line = console.readLine();
                System.out.println("Request was " + line);
            } catch(IOException e) {
                clientHOPP.quit();
                e.printStackTrace();
                System.exit(1);
            }

            if (line.equals(DIR)) {
                dir();
            } else if (line.startsWith(CD)) {
                chdir(losePrefix(line, CD));
            } else if (line.startsWith(GET)) {
                get(losePrefix(line, GET));
            } else if (line.equals("QUIT")) {
                clientHOPP.quit();
                System.exit(0);
            }
            else if (line.toUpperCase().startsWith(FileTransferTextConstants.QUERY)){
                query(losePrefix(line, FileTransferTextConstants.QUERY));
            }
            else {
                System.out.println("Unrecognised command");
            }
        }
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

    protected void dir() {
        String[] dirList = clientHOPP.dir();
        if (dirList == null) {
            System.out.println("No dir list available");
        } else {
            for (int n = 0; n < dirList.length; n++) {
                System.out.println(dirList[n]);
            }
        }
    }

    public void chdir(String dir) {
        if (clientHOPP.chdir(dir)) {
            System.out.println("Chdir okay");
        } else {
            System.out.println("Chdir failed");
        }
    }

    public void get(String filename) {
        // omitted
    }


    private void query(String input) {
        String[] list = clientHOPP.queryReq(input);
        listOutput(list);
    }

    private void listOutput(String[] list) {
        if(list.length == 0){
            System.out.println("No Hotels available");
        } else {
            System.out.println("The available flights are: ");
            for (String str : list) {
                System.out.println(str);
            }
        }
    }

} // Client.FileTransferClientUI