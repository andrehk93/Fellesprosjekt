# -*- coding: utf-8 -*-
import SocketServer
import json
import time
from datetime import datetime

users = []
history = []
clienthandlers = []

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
        clienthandlers.append(self)
        self.ip = self.client_address[0]
        self.port = self.client_address[1]
        self.connection = self.request
        self.recieved_string = ""
        self.ownusername = ""
        self.all_messages = []

        print "Client connected at " + self.ip + ":" + str(self.port)

        # Loop that listens for messages from the client
        while True:
            self.received_string = self.connection.recv(4096).strip()
            self.process(self.received_string)

    def send(self, data):
        print "send data", data
        self.connection.sendall(json.dumps(data))

    def handleUpdates(self, message):
        for ch in clienthandlers:
            if (ch is not self):
                ch.all_messages.append(message)
                ch.send_updates()

    def send_updates(self):
        for x in self.all_messages:
            self.send(x)
            self.all_messages.pop(0)

    def process(self, data):
        msg = json.loads(data)
        request = msg["request"]
        content = msg["content"]

        if(request == "login"):
            self.login(content)
        elif(request == "logout"):
            self.logout()
        elif(request == "msg"):
            self.msg(content)
        else:
            self.send({"timestamp" : datetime.now().__str__()[0:19], "sender" : "Server", "response" : "error", "content": "Invalid input"})

    def login(self, username):
        if(not username in users) and not self.loggedin:
            self.ownusername = username
            users.append(self.ownusername)
            self.loggedin = True
            self.send({"timestamp" : datetime.now().__str__()[0:19], "sender" : "Server", "response" : "info", "content": "Logged in as "+self.ownusername})
            self.handleUpdates({"timestamp" : datetime.now().__str__()[0:19], "sender" : self.ownusername, "response" : "info", "content": self.ownusername + " joined the chat"})
            if(history):
                self.history()
        elif (username in users):
            self.send({"timestamp" : datetime.now().__str__()[0:19], "sender" : "Server", "response" : "error", "content": "Username already taken"})
        else:
            self.send({"timestamp" : datetime.now().__str__()[0:19], "sender" : "Server", "response" : "error", "content": "Already logged in"})

    def logout(self):
        if(self.loggedin):
            users.pop(self.ownusername)
            self.send({"timestamp" : datetime.now().__str__()[0:19], "sender" : "Server", "response" : "info", "content": "Logged out"})
            self.handleUpdates({"timestamp" : datetime.now().__str__()[0:19], "sender" : "Server", "response" : "info", "content": self.ownusername + " left the chat"})
        else:
            self.send({"timestamp" : datetime.now().__str__()[0:19], "sender" : "Server", "response" : "error", "content": "Already logged out"})


    def msg(self, message):
        mes = {"timestamp" : datetime.now().__str__()[0:19], "sender" : self.ownusername, "response" : "message", "content": message}
        history.append(mes)
        self.handleUpdates(mes)

    def history(self):
        self.send({"timestamp" : datetime.now().__str__()[0:19], "sender" : "Server", "response" : "history", "content": "start"})
        for message in history:
            self.send(message)
        self.send({"timestamp" : datetime.now().__str__()[0:19], "sender" : "Server", "response" : "history", "content": "stop"})


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

    HOST, PORT = "localhost", 9998
    print 'Server running...'
    ch = ClientHandler
    # Set up and initiate the TCP server
    server = ThreadedTCPServer((HOST, PORT), ch)
    server.serve_forever()





