import socket, select, string, sys, time
 
##############################################################################
## API :display_prompt                                                       #
## Purpose : API to display prompt to user                                   #
##                                                                           #
## Input Parameters : None                                                   #
##                                                                           #
## Output Parameters : None                                                  #
## Return : None                                                             #
##############################################################################

def display_prompt() :
    sys.stdout.write(prompt_string)
    sys.stdout.flush()
 

# main function
if __name__ == "__main__":
     
    if(len(sys.argv) < 3) :
        print 'Usage : python client.py chatserver_ip chatserver_port'
        sys.exit()
     
    host = sys.argv[1]
    port = int(sys.argv[2])
    prompt_string = "<Me>"
   # host = "127.0.0.1" # Chat server IP
   # port = 55555       # chat server port
    TIMEOUT = 5     
    RECV_BUF_SIZE = 1024

    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.settimeout(TIMEOUT)
    
    # connect to remote host
    try :
          s.connect((host, port))
    except socket.timeout, f:
          print "%r :: Timeout of %d sec elapsed connecting to chat server." % (time.ctime(), TIMEOUT)
          sys.exit()
    except Exception, e:
          print '%r :: Unable to connect to Chat server %s.%s %s' % (time.ctime(), host, port, e)
          sys.exit()
     
    print "%r :: Connected to Chat server [%s,%s]. Start chat ::))" % (time.ctime(), host, port)
    display_prompt()
     
    while True:
        socket_list = [sys.stdin, s]
         
        # Get the list of fd's which are readable
        read_sockets, write_sockets, error_sockets = select.select(socket_list , [], [])
         
        for sock in read_sockets:
            # Got message from remote server
            if sock == s:
                data = sock.recv(RECV_BUF_SIZE)
                if not data :
                    print "%r chat server is offline. Ending chat" % time.ctime()
                    sys.exit()
                else :
                    # print message
                    sys.stdout.write(data)
                    display_prompt()
             
            # user entered a message
            else :
                msg = sys.stdin.readline()
                if(msg.startswith("quit")):
                   print "%r Exiting chat as user typed \"quit\"" % time.ctime()
                   sys.exit(1)
                s.send(msg)
                display_prompt()
