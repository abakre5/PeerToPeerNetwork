package com.p2p.server;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SessionConnect implements Runnable {

	private ServerSocket serverSocket;
	
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
			
			System.out.println("Error: Something went wrong");
			
		}
		
		ObjectInputStream objectInputStream = null;
		try {
			
			objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
		
		} catch (IOException e) {
			
			System.out.println("Error: Something went wrong");
			
		}
	
		FileOutputStream fileOutputStream = null;
		
		byte[] buffer = new byte[Server.BUFFER_SIZE];

		Object object = null;
		try {
			
			object = objectInputStream.readObject();
		
		} catch (ClassNotFoundException e) {

			System.out.println("Error: Something went wrong");
			
		} catch (IOException e) {

			System.out.println("Error: Something went wrong");
			
		}

		if (object instanceof String) {
			
			try {
				
				fileOutputStream = new FileOutputStream(object.toString());
			
			} catch (FileNotFoundException e) {
				
				System.out.println("Error: Something went wrong");
				
			}
		
		} else {
		
			System.out.println("Something went wrong");
		
		}

		Integer bytesRead = 0;

		do {
			
			try {
				object = objectInputStream.readObject();
			} catch (ClassNotFoundException e) {
				
				System.out.println("Error: Something went wrong");
				
			} catch (IOException e) {
				
				System.out.println("Error: Something went wrong");
				
			}

			if (!(object instanceof Integer)) {
				
				System.out.println("Something went wrong");
			
			}

			bytesRead = (Integer) object;

			try {
				object = objectInputStream.readObject();
			} catch (ClassNotFoundException e) {
				
				System.out.println("Error: Something went wrong");
				
			} catch (IOException e) {
				
				System.out.println("Error: Something went wrong");
				
			}

			if (!(object instanceof byte[])) {
				
				System.out.println("Something went wrong");
			
			}

			buffer = (byte[]) object;

			try {
				
				fileOutputStream.write(buffer, 0, bytesRead);
			
			} catch (IOException e) {
				
				System.out.println("Error: Something went wrong");
				
			}

		} while (bytesRead == Server.BUFFER_SIZE);

		System.out.println("File transfer success");

		try {
			fileOutputStream.close();
			
			objectInputStream.close();
			
			objectOutputStream.close();
			
		} catch (IOException e) {
			
			System.out.println("Error: Something went wrong");
			
		}

		

	}

}
