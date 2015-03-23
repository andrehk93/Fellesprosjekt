# -*- coding: utf-8 -*-
import SocketServer
import json
import time


users = []
all_messages = []

class ClientHandler(SocketServer.BaseRequestHandler):
    """
    This is the ClientHandler class. Everytime a new client connects to the
    server, a new ClientHandler object will be created. This class represents
    only connected clients, and not the server itself. If you want to write
    logic for the server, you must write it outside this class
    """
    sentMsgs = 0
    loggedin = False

    def handle(self):
        """
        This method handles the connection between a client and the server.
        """
        self.ip = self.client_address[0]
        self.port = self.client_address[1]
        self.connection = self.request
        self.recieved_string = ""

        print "Client connected at " + self.ip + ":" + str(self.port)

        # Loop that listens for messages from the client
        while True:
            self.received_string = self.connection.recv(4096).strip()
            print self.received_string
            self.process(self.received_string)

    def login(self, username):
        global users
        if(not username in users):
            users.append(username)
            self.send({"response" : "login", "username" : username})
            self.loggedin = True
            self.broadcast(username + " joined the chat")
            self.send_updates()
        else:
            self.send({"response" : "login", "error": "Username already taken"})

    def send(self, data):
        self.request.sendall(json.dumps(data))

    def broadcast(self, msg):
        all_messages.append(msg)

    def send_updates(self):
        while True:
            if self.sentMsgs < len(all_messages) and self.loggedin:
                #couldnt send a list directly, will fix with json later
                for x in range(self.sentMsgs, len(all_messages)):
                    self.send({"response":"message", "message":all_messages[x]})
                    print all_messages[x]
                    self.sentMsgs += 1
            #Have to sleep it, else it will try to drain the cpu
            time.sleep(0.2) #0.2 seconds

    def process(self, data):
        msg = json.loads(data)

        if(msg["request"] == "login"):
            self.login(msg["username"])
        elif(msg["request"] == "message"):
            print("messaaaaaaaaage")
            self.broadcast(msg["username"] + ": " + msg["message"])


class ThreadedTCPServer(SocketServer.ThreadingMixIn, SocketServer.TCPServer):
    """
    This class is present so that each client connected will be ran as a own
    thread. In that way, all clients will be served by the server.

    No alterations is necessary
    """
    allow_reuse_address = True

if __name__ == "__main__":
    """
    This is the main method and is executed when you type "python Server.py"
    in your terminal.

    No alterations is necessary
    """
    HOST, PORT = 'localhost', 9998
    print 'Server running...'

    # Set up and initiate the TCP server
    server = ThreadedTCPServer((HOST, PORT), ClientHandler)
    server.serve_forever()
