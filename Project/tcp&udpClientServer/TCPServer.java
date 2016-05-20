import java.io.*; 
import java.net.*; 

class TCPServer { 

  public static void main(String argv[]) throws Exception 
    { 
      String clientSentence; 
	  String username[];
      String capitalizedSentence; 

      ServerSocket welcomeSocket = new ServerSocket(6789); 
  
      while(true) { 
  
            Socket connectionSocket = welcomeSocket.accept(); 
			System.out.println("Listening on : " + connectionSocket); 
           BufferedReader inFromClient = 
              new BufferedReader(new
              InputStreamReader(connectionSocket.getInputStream()));
           DataOutputStream  outToClient = 
             new DataOutputStream(connectionSocket.getOutputStream()); 

           clientSentence = inFromClient.readLine(); 
			username[] = clientSentence.
           capitalizedSentence = clientSentence.toUpperCase() + '\n'; 

           outToClient.writeBytes(capitalizedSentence); 
        } 
    } 
} 
 
