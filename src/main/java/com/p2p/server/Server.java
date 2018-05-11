package com.p2p.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.Scanner;
import java.util.logging.Logger;

public class Server extends Thread {
	
	private static final Logger LOGGER = Logger.getLogger( Server.class.getName() );

	public static final int BUFFER_SIZE = 100;

	private ServerSocket serverSocket;

	private Socket clientSocket;
	
	private String filePath;

	@SuppressWarnings("resource")
	public Server() throws IOException {

		this.serverSocket = new ServerSocket(0);

		serverSocket.getInetAddress();
		
		LOGGER.info("Enter file path :: ");
		
		this.filePath = new Scanner(System.in).nextLine();
		
		String hostAddress = InetAddress.getLocalHost().toString();

		hostAddress += ":" + serverSocket.getLocalPort();
		
		hostAddress += "@" + filePath;

		String bytesEncoded = Base64.getEncoder().encodeToString(hostAddress.getBytes());

		LOGGER.info("Host Address :: " + bytesEncoded);

	}

	public void listenClient() throws IOException {

		while (true) {

			clientSocket = serverSocket.accept();

			new Thread((Runnable) new SessionConnect(serverSocket, clientSocket)).start();

		}

	}

	public static void main(String[] args) throws IOException {
			
		new Server().listenClient();

	}
}