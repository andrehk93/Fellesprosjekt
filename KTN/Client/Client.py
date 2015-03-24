# -*- coding: utf-8 -*-
import socket
from MessageReceiver import *

class Client:
    """
    This is the chat client class
    """
    
    def __init__(self, host, server_port):
        """
        This method is run when creating a new Client object
        """
        self.loggedin = False;
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
            time.sleep(1.0)
            string = username

        self.username = string
        
        while input[0:7] != "/logout":
            input = raw_input(self.username + ": ")
            self.sendMessage(input)
            time.sleep(0.2)

        self.disconnect

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
        message = {"request" : "message", "message" : msg}
        self.send_payload(json.dumps(message))

    def send_payload(self, data):
        self.connection.sendall(data)

    def process(self, data):
        msg = json.loads(data)

        if(msg["response"] == "login"):
            if(msg["username"]):
                self.loggedin = True
            else:
                print msg['error']
        elif(msg["response"] == "message" and msg["username"] != self.username):
            print msg["message"]
                


if __name__ == '__main__':
    """
    This is the main method and is executed when you type "python Client.py"
    in your terminal.

    No alterations is necessary
    """
    client = Client('78.91.30.205', 9998)
