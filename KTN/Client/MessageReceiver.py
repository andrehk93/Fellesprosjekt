# -*- coding: utf-8 -*-
from threading import Thread
import json

class MessageReceiver(Thread):
    """
    This is the message receiver class. The class inherits Thread, something that
    is necessary to make the MessageReceiver start a new thread, and permits
    the chat client to both send and receive messages at the same time
    """
    loggedin = False;
    def __init__(self, client, connection):
        """
        This method is executed when creating a new MessageReceiver object
        """
        self.client = client
        self.connection = connection
        super(MessageReceiver, self).__init__()
        # Flag to run thread as a deamon
        self.deamon = True

    def run(self):
        input = ""
        response = ""

        while not self.loggedin:
            username = raw_input("Username: ")
            self.login(username)
            self.process(self.connection.recv(1024).strip())

        
        while input[0:7] != "/logout":
            input = raw_input("> ")
            self.sendMessage(input)
            self.process(self.connection.recv(1024).strip())

    def send(self, message):
        self.connection.sendall(json.dumps(message))

    def login(self, username):
        message = {"request" : "login", "username": username}
        self.client.send_payload(json.dumps(message))

    def process(self, data):
        msg = json.loads(data)

        if(msg["response"] == "login"):
            if(msg["username"]):
                self.loggedin = msg['username']
            else:
                print msg['error']
        elif(msg["response"] == "message"):
            print msg["message"]     

    def receive_message(self):
        pass

    def sendMessage(self, msg):
        message = {"request" : "message", "message" : msg, "username" : self.loggedin }
        self.client.send_payload(json.dumps(message))