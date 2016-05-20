import socket, select
import time


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
 
 
    print "%r :: Chat server started on port %s" % (time.ctime(), PORT)
 
    sockfd, addr = server_socket.accept()
    print "%r :: Client %s connected" % (str(time.ctime()), addr)
                 
    while True:
               try:
                    # Handle "Connection reset by peer" exception
                    data = sockfd.recv(RECV_BUFFER_SIZE)
                    if data:
                           print "%r :: <%s>  %s" % (time.ctime(), str(sockfd.getpeername()), data)

                    elif not data:
                       print "%r :: Client %s is offline" % (time.ctime(), str(sockfd.getpeername()))
                       sockfd.close()
                       break

               except Exception, e:
                    print "%r :: Client %s  is offline " % (time.ctime(), str(sockfd.getpeername()))
                    sockfd.close()
                    break
     
    server_socket.close()
