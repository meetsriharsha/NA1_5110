package client;

import java.io.*;
import java.net.*;

class FileClient {

    private static final String serverIP = "152.54.14.17";
    private static final int serverPort = 3248;
    private static final int sendPort = 4248;
    private static final String fileOutput = "/home/sc9v9/na1htr/source/client/sampleFile.txt";
    private static final String fileSend = "/home/sc9v9/na1htr/source/client/sampleFile.txt";

    public static void main(String args[]) {
        byte[] aByte = new byte[1];
        int bytesRead;

        Socket clientSocket = null;
        InputStream is = null;

        try {
            clientSocket = new Socket( serverIP , serverPort );
            is = clientSocket.getInputStream();
        } catch (IOException ex) {
            // Do exception handling
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if (is != null) {

            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            try {
                fos = new FileOutputStream( fileOutput );
                bos = new BufferedOutputStream(fos);
                bytesRead = is.read(aByte, 0, aByte.length);

                do {
                        baos.write(aByte);
                        bytesRead = is.read(aByte);
                } while (bytesRead != -1);

                bos.write(baos.toByteArray());
                bos.flush();
                bos.close();
                try (BufferedReader br = new BufferedReader(new FileReader(fileOutput))) {
                		System.out.println("Printing the file contents to screen: \n");
                	   String line = null;
                	   while ((line = br.readLine()) != null) {
                	       System.out.println(line);
                	   }
                	}
                System.out.println("Appending the text to the file");
                Writer writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(fileOutput, true), "UTF-8"));
                writer.write("\r\nThis line is added by the Server");
                writer.close();
                clientSocket.close();
            } catch (IOException ex) {
                // Do exception handling
            }
        }
        
        
        
        while (true) {
        	
            ServerSocket sendSocket = null;
            Socket connectionSocket = null;
            BufferedOutputStream outToClient = null;

            try {
            	sendSocket = new ServerSocket(sendPort);
                connectionSocket = sendSocket.accept();
                System.out.println("Sending file back to client");
                outToClient = new BufferedOutputStream(connectionSocket.getOutputStream());
            } catch (IOException ex) {
                // Do exception handling
            }
        
        if (outToClient != null) {
            File myFile = new File(fileSend);
            byte[] mybytearray = new byte[(int) myFile.length()];

            FileInputStream fis = null;

            try {
                fis = new FileInputStream(myFile);
            } catch (FileNotFoundException ex) {
                // Do exception handling
            }
            BufferedInputStream bis = new BufferedInputStream(fis);

            try {
                bis.read(mybytearray, 0, mybytearray.length);
                outToClient.write(mybytearray, 0, mybytearray.length);
                outToClient.flush();
                outToClient.close();
                connectionSocket.close();

                // File sent, exit the main method
                return;
            } catch (IOException ex) {
                // Do exception handling
            }
        }
        }
    }
}
