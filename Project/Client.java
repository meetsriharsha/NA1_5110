import java.io.*; 
import java.net.*;

public class Client
{  private Socket S = null;
   //private DataInputStream  DIS   = null;
   private BufferedReader BRin = null;
   //= new BufferedReader(new InputStreamReader(in));
   private DataOutputStream DOS = null;

   public Client(String serverName, int serverPort)
   {  
	  System.out.println("Contacting Server");
      try
      {  S = new Socket(serverName, serverPort);
         System.out.println("Connected: " + S);
         startCon();
      }
      catch(UnknownHostException uhe)
      {  System.out.println("Host unknown: " + uhe.getMessage());
      }
      catch(IOException ioe)
      {  System.out.println("Unexpected exception: " + ioe.getMessage());
      }
      String Input = "";
      while (!Input.equals("quit"))
      {  try
         {  Input = BRin.readLine();
            DOS.writeUTF(Input);
            DOS.flush();
         }
         catch(IOException ioe)
         {  System.out.println("Sending error: " + ioe.getMessage());
         }
      }
      stopCon();
   }
   public void startCon() throws IOException
   { // BRin   = new DataInputStream(System.in);
      BRin = new BufferedReader(new InputStreamReader(System.in));
	  DOS = new DataOutputStream(S.getOutputStream()); 

   }
   public void stopCon()
   {  try
      {  if (BRin!= null)  
			BRin.close();
         if (DOS!= null)  
			DOS.close();
         if (S!= null)  
			S.close();
         System.out.println("Connection Closed at the client place");
      }
      catch(IOException ioe)
      {  
		System.out.println("Exception Caught while closing connection");
      }
   }
   public static void main(String args[])
   {  
	   Client clientobj = null;
      if (args.length != 2)
         System.out.println("Enter IP adress and port numbers");
      else
         clientobj = new Client(args[0], Integer.parseInt(args[1]));
      //clientobj = new Client("192.168.1.4", 5060);
      
   }
}