 
import socket, select
import time

##############################################################################
## API :broadcast_MessageTo_Clients                                          #
## Purpose : API to broadcast chat messages to all connected clients         #
##           not to send the message to main  socket(listening socket)       #
## Input Parameters : socket ID: listening ID or client Accept ID            #
##        message : ASCII message to be displayed                            #
## Output Parameters : None                                                  #
## Return : None                                                             #
##############################################################################

def broadcast_MessageTo_Clients (sock, message):
    
    len_of_msg = len(message)
    msg = message[0:len_of_msg]
    #sent_data_len = 0
    
    for socket in ALL_CONNECTION_LIST:
      sent_data_len = 0
      while (sent_data_len != len_of_msg):
        if socket != server_socket and socket != sock:
            try :
                temp = socket.send(msg)
                sent_data_len = sent_data_len + temp
                if(sent_data_len != len_of_msg):
                   msg = message[sent_data_len:len_of_msg]
                else:
                  break
                
            except Exception, e:
                # broken socket connection may be, chat client pressed ctrl+c for example
                print"{{%s}}" % e
                socket.close()
                ALL_CONNECTION_LIST.remove(socket)
                break
     
        else:
           break 
      


if __name__ == "__main__":
     
    # List to keep track of socket fd's
    ALL_CONNECTION_LIST = []
    RECV_BUFFER_SIZE = 1024
    PORT = 55555 # Some Dummy port
    HOST = "" # INADDR_ANY
     
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server_socket.bind((HOST, PORT)) 
    server_socket.listen(10) # Backlog of 10
 
    # Add server socket to the list of readable connections
    ALL_CONNECTION_LIST.append(server_socket)
 
    print "%r :: Chat server started on port %s" % (time.ctime(), PORT)
 
    while True:
        # Get the list sockets which are readable
        read_sockets = write_sockets = error_sockets = ""
        read_sockets,write_sockets,error_sockets = select.select(ALL_CONNECTION_LIST,[],[])
        for sock in read_sockets:
            # New connection request from client
            if sock == server_socket:
                sockfd, addr = server_socket.accept()
                ALL_CONNECTION_LIST.append(sockfd)
                print "%r :: Client %s connected" % (str(time.ctime()), addr)
                 
             
            # Data received from old connected client
            else:
                try:
                    # Handle "Connection reset by peer" exception
                    data = sock.recv(RECV_BUFFER_SIZE)
                    if data:
                        print "%r :: <%s>  %s" % (time.ctime(), str(sock.getpeername()), data)
                    elif not data:
                       print "%r :: Client %s is offline" % (time.ctime(), str(sock.getpeername()))
                       sock.close()
                       ALL_CONNECTION_LIST.remove(sock)
                       continue

                       
                 
                except :
                    print "%r :: Client %s is offline" % (time.ctime(), str(sock.getpeername()))
                    sock.close()
                    ALL_CONNECTION_LIST.remove(sock)
                    continue
     
    server_socket.close()
