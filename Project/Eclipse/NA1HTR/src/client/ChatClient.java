package client;

import java.io.*;
import java.net.*;
public class ChatClient
{
  public static void main(String[] args) throws Exception
  {
     Socket sock = new Socket("152.54.14.17", 3000);
                               // reading from keyboard (keyRead object)
     BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
                              // sending to client (pwrite object)
     OutputStream ostream = sock.getOutputStream(); 
     PrintWriter pwrite = new PrintWriter(ostream, true);
     
                              // receiving from server ( receiveRead  object)
     InputStream istream = sock.getInputStream();
     BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));

     System.out.println("Type and press Enter key");

     String receiveMessage, sendMessage;               
     while(true)
     {
        sendMessage = keyRead.readLine();  // keyboard reading
       // String msgToClient = ostream.toString();
        if(sendMessage.toLowerCase().contains("bye from client")) 
        {
      	  System.out.println("Exiting the application");
      	  System.exit(0);
        } 
        pwrite.println(sendMessage);       // sending to server
        pwrite.flush();                    // flush the data
        if((receiveMessage = receiveRead.readLine()) != null) //receive from server
        {
            System.out.println("From Server: " + receiveMessage); // displaying at DOS prompt
            if(receiveMessage.toLowerCase().contains("bye from server")) {
            	System.out.println("Server is exiting, hence exiting the client application");
            	System.exit(0);
            }
        }         
      }               
    }                    
}                        
