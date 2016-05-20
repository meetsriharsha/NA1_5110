import java.io.*;
import java.net.*;

class FileClient
 {

	private static final String serverIP = "152.54.14.18";
    private static final int receivePort = 42481;
    private static final int sendPort = 32481;
    private static final String fileToSend = "sampleFile.txt";
    private static final String fileToReceive = "sampleFileNew.txt";

    public static void main(String args[]) {

        while (true) {
            ServerSocket welcomeSocket = null;
            Socket connectionSocket = null;
            BufferedOutputStream outToClient = null;

            try {
                welcomeSocket = new ServerSocket(sendPort);
                connectionSocket = welcomeSocket.accept();
                outToClient = new BufferedOutputStream(connectionSocket.getOutputStream());
            } catch (IOException ex) {
                // Do exception handling
            }

            if (outToClient != null) {
                File myFile = new File( fileToSend);
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
                    
                } catch (IOException ex) {
                    // Do exception handling
                }
            }
            
            
            byte[] aByte = new byte[1];
            int bytesRead;

            Socket clientSocket = null;
            InputStream is = null;

            try {
                clientSocket = new Socket( serverIP , receivePort );
                is = clientSocket.getInputStream();
            } catch (IOException ex) {
                // Do exception handling
            }
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            
            if (is != null) {
            	System.out.println("Getting updated file from the server....");
            	
            	FileOutputStream fos = null;
                BufferedOutputStream bos = null;
                try {
                    fos = new FileOutputStream( fileToReceive );
                    bos = new BufferedOutputStream(fos);
                    bytesRead = is.read(aByte, 0, aByte.length);

                    do {
                            baos.write(aByte);
                            bytesRead = is.read(aByte);
                    } while (bytesRead != -1);

                    bos.write(baos.toByteArray());
                    bos.flush();
                    bos.close();
                    try (BufferedReader br = new BufferedReader(new FileReader(fileToReceive))) {
                    		System.out.println("Printing the file contents to screen: \n");
                    	   String line = null;
                    	   while ((line = br.readLine()) != null) {
                    	       System.out.println(line);
                    	   }
                    	}
                   
                    clientSocket.close();
                } catch (IOException ex) {
                    // Do exception handling
                }
            
            }
        }
        
        
        
        
    }
}
