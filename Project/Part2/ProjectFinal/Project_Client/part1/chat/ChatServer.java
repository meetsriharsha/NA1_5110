import java.io.*;
import java.net.*;
public class ChatServer
{
  public static void main(String[] args) throws Exception
  {
      ServerSocket sersock = new ServerSocket(12345, 1);
      System.out.println("Started listening on port 12345");
      Socket sock = sersock.accept( );
      sersock.close();
System.out.println("Socket: " + sock);
                              // reading from keyboard (keyRead object)
      BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
	                      // sending to client (pwrite object)
      OutputStream ostream = sock.getOutputStream(); 
      PrintWriter pwrite = new PrintWriter(ostream, true);
                            // receiving from server ( receiveRead  object)
      
      InputStream istream = sock.getInputStream();
      BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));

      String receiveMessage, sendMessage, username[];
      Boolean found = false;
      while(true)
      {
        if((receiveMessage = receiveRead.readLine()) != null)  
        {
        	found = receiveMessage.contains("-");
        	if(found)
        	{
        	username = receiveMessage.split("-");
            //System.out.println(receiveMessage);
//            System.out.println(username[0]);
//            System.out.println(username[1].trim());
            if(username[0].trim().equals("Hello from Client")) {
         	   System.out.println("Client " + username[1].trim() + " connected!!!");
         	  System.out.println("From Client: " + receiveMessage);
            }
        	}
            else {
         	   System.out.println("From Client: " + receiveMessage);
            }
        	
                    
        }         
        sendMessage = keyRead.readLine(); 
        //String msgToClient = ostream.toString();
        if(sendMessage.toLowerCase().contains("bye from server")) 
        {
      	  System.out.println("Exiting the application");
      	 pwrite.println(sendMessage);
      	  System.exit(0);
        }  
        pwrite.println(sendMessage);             
        pwrite.flush();
      }               
    }                    
}                        
