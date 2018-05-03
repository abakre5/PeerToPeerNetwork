package com.p2p.server;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class SessionConnect implements Runnable {

	private static final Logger LOGGER = Logger.getLogger( SessionConnect.class.getName() );
	
	protected ServerSocket serverSocket;
	
	private Socket clientSocket;

	public SessionConnect(ServerSocket serverSocket, Socket clientSocket) {

		this.serverSocket = serverSocket;
		
		this.clientSocket = clientSocket;
		
		System.out.println("Client " + clientSocket + " connected.");

	}

	public void run(){

		ObjectOutputStream objectOutputStream = null;
		
		try {
		
			objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
		
		} catch (IOException e) {
			
			LOGGER.warning("Error: Something went wrong");
			
		}
		
		ObjectInputStream objectInputStream = null;
		try {
			
			objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
		
		} catch (IOException e) {
			
			LOGGER.warning("Error: Something went wrong");
			
		}
	
		FileOutputStream fileOutputStream = null;
		
		byte[] buffer = new byte[Server.BUFFER_SIZE];

		Object object = null;
		try {
			
			object = objectInputStream.readObject();
		
		} catch (ClassNotFoundException e) {

			LOGGER.warning("Error: Something went wrong");
			
		} catch (IOException e) {

			LOGGER.warning("Error: Something went wrong");
			
		}

		if (object instanceof String) {
			
			try {
				
				fileOutputStream = new FileOutputStream(object.toString());
			
			} catch (FileNotFoundException e) {
				
				LOGGER.warning("Error: Something went wrong");
				
			}
		
		} else {
		
			LOGGER.warning("Something went wrong");
		
		}

		Integer bytesRead = 0;
		
		do {
			
			try {
				object = objectInputStream.readObject();
			} catch (ClassNotFoundException e) {
				
				LOGGER.warning("Error: Something went wrong");
				
			} catch (IOException e) {
				
				LOGGER.warning("Error: Something went wrong");
				
			}

			if (!(object instanceof Integer)) {
				
				LOGGER.warning("Something went wrong");
			
			}

			bytesRead = (Integer) object;

			try {
				object = objectInputStream.readObject();
			} catch (ClassNotFoundException e) {
				
				LOGGER.warning("Error: Something went wrong");
				
			} catch (IOException e) {
				
				LOGGER.warning("Error: Something went wrong");
				
			}

			if (!(object instanceof byte[])) {
				
				LOGGER.warning("Something went wrong");
			
			}

			buffer = (byte[]) object;

			try {
				
				fileOutputStream.write(buffer, 0, bytesRead);
			
			} catch (IOException e) {
				
				LOGGER.warning("Error: Something went wrong");
				
			}

		} while (bytesRead == Server.BUFFER_SIZE);

		LOGGER.info("File transfer success");

		try {
			fileOutputStream.close();
			
			objectInputStream.close();
			
			objectOutputStream.close();
			
		} catch (IOException e) {
			
			LOGGER.warning("Error: Something went wrong");
			
		}

		

	}

}
