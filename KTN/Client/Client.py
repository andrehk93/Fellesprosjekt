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
        input = ""
        self.host = host
        self.server_port = server_port
        # Set up the socket connection to the server
        self.connection = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.messRec = MessageReceiver(self, self.connection)
        self.run()

    def run(self):
        # Initiate the connection to the server
        print "Connecting to " + self.host + ":" + str(self.server_port)
        self.connection.connect((self.host, self.server_port))
        self.messRec.start()
        while True:
            input = raw_input("")
            self.send_payload(input)
        

    def disconnect(self):
        self.disconnect
        pass

    def send_payload(self, input):
        data = input.split()
        request = data[0]
        content = ' '.join(data[1:])
        payload = {"request" : request, "content" : content}
        self.connection.sendall(json.dumps(payload))

    def process(self, data):
        msg = json.loads(data)
        timestamp = msg["timestamp"]
        sender = msg["sender"]
        response = msg["response"]
        content = msg["content"]

        if(response == "info"):
            print "INFO: "+content
        elif(response == "message"):
            print sender+": "+content
        elif(response == "error"):
            print "<"+timestamp+">ERROR: "+content
        elif(response == "history"):
            if(content == "start"):
                print "===============HISTORY==============="
            elif(content == "stop"):
                print "====================================="
                


if __name__ == '__main__':
    """
    This is the main method and is executed when you type "python Client.py"
    in your terminal.

    No alterations is necessary
    """
    client = Client('78.91.47.219', 9998)
