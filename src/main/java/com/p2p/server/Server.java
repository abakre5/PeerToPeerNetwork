package com.p2p.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
 
public class Server extends Thread {
	
    public static final int BUFFER_SIZE = 100;
    
    private ServerSocket serverSocket;
 
    private Socket clientSocket;
    
    public Server(int port) throws IOException {
    	
    	serverSocket = new ServerSocket(port);
    	
    }
    
    public void listenClient() throws IOException {
    	
    	while(true){
		
    		clientSocket = serverSocket.accept();
			
    		new Thread((Runnable) new SessionConnect(serverSocket, clientSocket)).start();
		
    	}
    	
    }
    
  
    public static void main(String[] args) throws IOException {
    	
        new Server(2424).listenClient();
   
    }
}  