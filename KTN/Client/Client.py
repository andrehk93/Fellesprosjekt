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
        self.host = host
        self.server_port = server_port
        # Set up the socket connection to the server
        self.connection = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        messRec = MessageReceiver(self, self.connection)
        self.run()
        messRec.run()

    def run(self):
        # Initiate the connection to the server
        print "Connecting to " + self.host + ":" + str(self.server_port)
        self.connection.connect((self.host, self.server_port))

    def disconnect(self):
        self.disconnect
        pass

    def receive_message(self, message):
        # TODO: Handle incoming message
        message = messRec.receive_message()
        print(message)

    def send_payload(self, data):
        self.connection.sendall(data)


if __name__ == '__main__':
    """
    This is the main method and is executed when you type "python Client.py"
    in your terminal.

    No alterations is necessary
    """
    client = Client('localhost', 9998)
