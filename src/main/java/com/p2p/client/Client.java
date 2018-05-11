package com.p2p.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Logger;

import com.p2p.server.Server;

public class Client {

	private static final Logger LOGGER = Logger.getLogger( Client.class.getName() );
	
	private Socket socket;

	private String filePath;

	public Client(String host, int port, String filePath) {

		this.filePath = filePath;
		

		LOGGER.info("File to Download :: " + this.filePath);
		
		try {

			this.socket = new Socket(host, port);

		} catch (UnknownHostException e) {

			LOGGER.warning("Error : Host not Found");

		} catch (IOException e) {

			LOGGER.warning("Error : Port not Found");

		}

	}

	public void recieve() {

		File file = new File(filePath);

		ObjectOutputStream objectOutputStream = null;

		try {

			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

		} catch (IOException e) {

			LOGGER.warning("Error : Error in init Output Stream");

		}

		try {

			objectOutputStream.writeObject(file.getName());

		} catch (IOException e) {

			LOGGER.warning("Error: Something went wrong");

		}

		FileInputStream fileInputStream = null;

		try {

			fileInputStream = new FileInputStream(file);

		} catch (FileNotFoundException e) {

			LOGGER.warning("Error: Something went wrong");

		}

		byte[] buffer = new byte[Server.BUFFER_SIZE];

		Integer bytesRead = 0;

		try {

			while ((bytesRead = fileInputStream.read(buffer)) > 0) {

				objectOutputStream.writeObject(bytesRead);

				objectOutputStream.writeObject(Arrays.copyOf(buffer, buffer.length));

			}

		} catch (IOException e) {

			LOGGER.warning("Error: Download Fail");

		}

		try {

			objectOutputStream.close();

			objectOutputStream.close();

			socket.close();

		} catch (IOException e) {

			LOGGER.warning("Error: Closing connection");

		}

		LOGGER.info("File Download Successful");
		
		System.exit(0);

	}

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		
		LOGGER.info("Enter Host Address :: ");
		
		String hostInetAddress = ConnectHost.hostAddress(new Scanner(System.in).nextLine());

		new Client(ConnectHost.getHost(hostInetAddress), ConnectHost.getPort(hostInetAddress), ConnectHost.getFilePath(hostInetAddress)).recieve();

	}

}