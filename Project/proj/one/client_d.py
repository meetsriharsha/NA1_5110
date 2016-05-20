import socket, select, string, sys, time
 

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
     
    print "%r :: Connected to Chat server [%s,%s]. Sending message ::))" % (time.ctime(), host, port)
     
    msg = "Hello from client: India\n"
    s.send(msg)
    time.sleep(1)
    data = s.recv(RECV_BUF_SIZE)
    if not data :
       print "%r chat server is offline. Ending chat" % time.ctime()
       sys.exit()
    else :
       # print message
       sys.stdout.write(data)
    
    print "%r :: Sending disconnect message ::))" % (time.ctime()) 
    msg = "Bye from client: India\n"
    s.send(msg)
    time.sleep(1)
    data = s.recv(RECV_BUF_SIZE)
    if not data :
        print "%r chat server is offline. Ending chat" % time.ctime()
        sys.exit()
    else :
        # print message
        sys.stdout.write(data)
    
