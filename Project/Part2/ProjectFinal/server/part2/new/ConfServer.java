
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;

//Public broadcast server
public class ConfServer{

  //Server Socket assigned to null
  private static ServerSocket serverSocket = null;
  //Client socket assigned to null
  private static Socket cSocket = null;

  // Client count can accept maximum 10
  private static final int maxClientsCount = 10;
  private static final clientThread[] threads = new clientThread[maxClientsCount];

  public static void main(String args[]) {

    // Native port number
    int portNumber = 2356;
    if (args.length < 1) {
      System.out.println("ConfServer\n"
          + "Port number=" + portNumber);
    } else {
      portNumber = Integer.valueOf(args[0]).intValue();
    }

    //connection accept port
    try {
      serverSocket = new ServerSocket(portNumber);
    } catch (IOException e) {
      System.out.println(e);
    }

    //connection socket for each client
    while (true) {
      try {
        cSocket = serverSocket.accept();
        int i = 0;
        for (i = 0; i < maxClientsCount; i++) {
          if (threads[i] == null) {
            (threads[i] = new clientThread(cSocket, threads)).start();
            break;
          }
        }
        if (i == maxClientsCount) {
          PrintStream output = new PrintStream(cSocket.getOutputStream());
          output.println("Server busy.");
          output.close();
          cSocket.close();
        }
      } catch (IOException e) {
        System.out.println(e);
      }
    }
  }
}

//Threads to accept the clients
class clientThread extends Thread {

  private String clientName = null;
  private DataInputStream inputStream = null;
  private PrintStream outStream = null;
  private Socket cSocket = null;
  private final clientThread[] threads;
  private int maxClientsCount;

  public clientThread(Socket cSocket, clientThread[] threads) {
    this.cSocket = cSocket;
    this.threads = threads;
    maxClientsCount = threads.length;
  }

  @SuppressWarnings("deprecation")
public void run() {
    int maxClientsCount = this.maxClientsCount;
    clientThread[] threads = this.threads;

    try {
    	inputStream = new DataInputStream(cSocket.getInputStream());
      outStream = new PrintStream(cSocket.getOutputStream());
      String name;
      while (true) {
        outStream.println("Enter your name.");
        BufferedReader d
        = new BufferedReader(new InputStreamReader(inputStream));
        name = d.readLine().trim();
        if (name.indexOf('@') == -1) {
          break;
        } else {
          outStream.println("The name should not contain '@' character.");
        }
      }

      outStream.println("Welcome " + name
          + " to our chat room.\nTo leave enter \'/quit\' in a new line.");
      synchronized (this) {
        for (int i = 0; i < maxClientsCount; i++) {
          if (threads[i] != null && threads[i] == this) {
            clientName = "Whisper" + name;
            break;
          }
        }
        for (int i = 0; i < maxClientsCount; i++) {
          if (threads[i] != null && threads[i] != this) {
            threads[i].outStream.println("New User " + name
                + " entered");
          }
        }
      }
      while (true) {
    	  
    	  BufferedReader d
          = new BufferedReader(new InputStreamReader(inputStream));
    	  
        String line = d.readLine();
		System.out.println("<" + name + "> " + line);
        if (line.startsWith("/quit")) {
          break;
        }
        if (line.startsWith("Whisper")) {
          String[] words = line.split("\\s", 2);
          if (words.length > 1 && words[1] != null) {
            words[1] = words[1].trim();
            if (!words[1].isEmpty()) {
              synchronized (this) {
                for (int i = 0; i < maxClientsCount; i++) {
                  if (threads[i] != null && threads[i] != this
                      && threads[i].clientName != null
                      && threads[i].clientName.equals(words[0])) {
                    threads[i].outStream.println("<" + name + "> " + words[1]);
                    this.outStream.println(">" + name + "> " + words[1]);
                    break;
                  }
                }
              }
            }
          }
        } else {
          synchronized (this) {
            for (int i = 0; i < maxClientsCount; i++) {
              if (threads[i] != null && threads[i].clientName != null) {
                threads[i].outStream.println("<" + name + "> " + line);
              }
            }
          }
        }
      }
      synchronized (this) {
        for (int i = 0; i < maxClientsCount; i++) {
          if (threads[i] != null && threads[i] != this
              && threads[i].clientName != null) {
         //   threads[i].outStream.println("User " + name
          //      + " left");
		System.out.println("User " + name + " left");
          }
        }
      }
      outStream.println( name + "Exited");
      synchronized (this) {
        for (int i = 0; i < maxClientsCount; i++) {
          if (threads[i] == this) {
            threads[i] = null;
          }
        }
      }
      inputStream.close();
      outStream.close();
      cSocket.close();
    } catch (IOException e) {
    }
  }
}
