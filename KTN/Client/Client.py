# -*- coding: utf-8 -*-
import socket
from MessageReceiver import *

class Client:
    """
    This is the chat client class
    """
    loggedin = False;
    def __init__(self, host, server_port):
        """
        This method is run when creating a new Client object
        """

        input = ""
        response = ""
        self.host = host
        self.server_port = server_port
        # Set up the socket connection to the server
        self.connection = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        messRec = MessageReceiver(self, self.connection)
        self.run()
        messRec.start()
        while not self.loggedin:
            username = raw_input("Username: ")
            self.login(username)

        
        while input[0:7] != "/logout":
            input = raw_input("> ")
            self.sendMessage(input)

    def run(self):
        # Initiate the connection to the server
        print "Connecting to " + self.host + ":" + str(self.server_port)
        self.connection.connect((self.host, self.server_port))

    def disconnect(self):
        self.disconnect
        pass

    def login(self, username):
        message = {"request" : "login", "username": username}
        self.send_payload(json.dumps(message))

    def sendMessage(self, msg):
        message = {"request" : "message", "message" : msg, "username" : self.loggedin }
        self.send_payload(json.dumps(message))

    def send_payload(self, data):
        self.connection.sendall(data)

    def process(self, data):
        print "mottar: ", data
        string = ""
        for x in data:
            if (x != "}"):
                string += x
            else:
                string += x
                msg = json.loads(string)
                if(msg["response"] == "login"):
                    if(msg["username"]):
                        self.loggedin = msg['username']
                    else:
                        print msg['error']
                elif(msg["response"] == "message"):
                    print msg["message"]
                string = ""


if __name__ == '__main__':
    """
    This is the main method and is executed when you type "python Client.py"
    in your terminal.

    No alterations is necessary
    """
    client = Client('78.91.30.205', 9998)
