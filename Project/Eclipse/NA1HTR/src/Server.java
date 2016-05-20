import java.net.*;
import java.io.*;

public class Server
{  
   private DataInputStream DIS =  null;
   private Socket         S   = null;
   private ServerSocket    Ser   = null;
   DataOutputStream  outToClient = null;
   String username[];
   String capitalizedSentence;
           
   

   public Server(int port)
   {  try
      {  
		 System.out.println("Port Bind will happen");
		 System.out.println("Port Number is:"+port);
         Ser = new ServerSocket(port);  
         System.out.println("Server started: " + Ser);
         S = Ser.accept();
         System.out.println("Connection Acceptance at the S" + S);
         OpenCon();
		 System.out.println("Connection Opened");
         boolean bQuit = false;
         while (!bQuit)
         {  try
            {  
        	   String input = DIS.readUTF();
        	   username = input.split("-");
               System.out.println(input);
               System.out.println(username[0]);
               System.out.println(username[1].trim());
               if(username[0].trim().equals("Hello from Client")) {
            	   outToClient.writeBytes("Hello from Server " + username[1].trim());
            	   System.out.println("Client " + username[1].trim() + " connected!!!");
               }
               else {
            	   capitalizedSentence = input.toUpperCase() + '\n'; 
                   outToClient.writeBytes("From Server: " + capitalizedSentence); 
            	   
               }
               System.out.println(input);
               bQuit = input.equalsIgnoreCase("quit");
            }
            catch(IOException ioe)
            {  bQuit = true;
            }
         }
         CloseCon();
      }
      catch(IOException ioe)
      {  System.out.println("Exception is :"+ioe); 
      }
   }
   public void OpenCon() throws IOException
   {  
	   DIS = new DataInputStream(new BufferedInputStream(S.getInputStream()));
	   outToClient = new DataOutputStream(S.getOutputStream());
   }
   public void CloseCon() throws IOException
   {  if (S != null)    
		S.close();
      if (DIS != null)  
		DIS.close();
      System.out.println("Connection Closed at the server side");
   }
   public static void main(String args[])
   {  
	  Server serverobj = null;
      if (args.length != 1)
         System.out.println("Enter Port number");
      else
         serverobj = new Server(Integer.parseInt(args[0]));
   }
}