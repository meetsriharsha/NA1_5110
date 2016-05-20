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
           username = clientSentence.split("-");
           System.out.println(clientSentence);
           System.out.println(username[0]);
           System.out.println(username[1].trim());
           if(username[0].trim().equals("Hello from Client")) {
        	   outToClient.writeBytes("Hello from Server " + username[1].trim());
        	   System.out.println("Client " + username[1].trim() + " connected!!!");
           }
           else {
        	   capitalizedSentence = clientSentence.toUpperCase() + '\n'; 
               outToClient.writeBytes("From server: " + capitalizedSentence); 
        	   
           }
           capitalizedSentence = clientSentence.toUpperCase() + '\n'; 

           outToClient.writeBytes(capitalizedSentence); 
        } 
    } 
} 
 
