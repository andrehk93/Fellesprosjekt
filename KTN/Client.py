# -*- coding: utf-8 -*-
import socket
import json

class Client:
    """
    This is the chat client class
    """

    loggedin = False

    def __init__(self, host, server_port):
        """
        This method is run when creating a new Client object
        """

        # Set up the socket connection to the server
        self.connection = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.run(host, server_port)

        # TODO: Finish init process with necessary code

    def run(self, host, port):
        # Initiate the connection to the server
        print "Connecting to " + host + ":" + str(port)
        self.connection.connect((host, port))

        input = ""
        response = ""

        while not self.loggedin:
            username = raw_input("Username: ")
            self.login(username)
            self.process(self.connection.recv(1024).strip())

        
        while input[0:7] != "/logout":
            input = raw_input("> ")
            self.sendMessage(input)
            response = self.process(self.connection.recv(1024).strip())
            print(response)
        


    def send(self, message):
        self.connection.sendall(json.dumps(message))

    def login(self, username):
        self.send({'request' : 'login', 'username': username})

    def disconnect(self):
        # TODO: Handle disconnection
        pass

    def process(self, data):
        msg = json.loads(data)

        if(msg['response'] == "login"):
            if(msg["username"]):
                self.loggedin = msg["username"]
            else:
                print msg['error']
        elif(msg['response'] == 'message'):
            print msg['message']


    def receive_message(self, message):
        # TODO: Handle incoming message
        pass

    def send_payload(self, data):
        # TODO: Handle sending of a payload
        pass

    def sendMessage(self, msg):
        self.send({'request' : 'message', 'message' : msg, 'username' : self.loggedin })


if __name__ == '__main__':
    """
    This is the main method and is executed when you type "python Client.py"
    in your terminal.

    No alterations is necessary
    """
    client = Client('localhost', 9998)
