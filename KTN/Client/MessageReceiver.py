# -*- coding: utf-8 -*-
from threading import Thread
import json
import time

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
        while True:
            recieved_str = self.connection.recv(1024).strip()
            self.client.process(recieved_str)