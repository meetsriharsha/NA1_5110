import java.net.*;
import java.io.*;

public class Server
{  
   private DataInputStream DIS =  null;
   private Socket         S   = null;
   private ServerSocket    Ser   = null;
   

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
        	   String Input = DIS.readUTF();
               System.out.println(Input);
               bQuit = Input.equalsIgnoreCase("quit");
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