package com.p2p.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Base64;

import com.p2p.server.Server;

public class Client {

	private Socket socket;

	private String filePath;

	public Client(String host, int port, String filePath) {

		this.filePath = filePath;

		try {

			this.socket = new Socket(host, port);

		} catch (UnknownHostException e) {

			System.out.println("Error : Host not Found");

		} catch (IOException e) {

			System.out.println("Error : Port not Found");

		}

	}

	public void recieve() {

		File file = new File(filePath);

		ObjectOutputStream objectOutputStream = null;

		try {

			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

		} catch (IOException e) {

			System.out.println("Error : Error in init Output Stream");

		}

		try {

			objectOutputStream.writeObject(file.getName());

		} catch (IOException e) {

			System.out.println("Error: Something went wrong");

		}

		FileInputStream fileInputStream = null;

		try {

			fileInputStream = new FileInputStream(file);

		} catch (FileNotFoundException e) {

			System.out.println("Error: Something went wrong");

		}

		byte[] buffer = new byte[Server.BUFFER_SIZE];

		Integer bytesRead = 0;

		try {

			while ((bytesRead = fileInputStream.read(buffer)) > 0) {

				objectOutputStream.writeObject(bytesRead);

				objectOutputStream.writeObject(Arrays.copyOf(buffer, buffer.length));

			}

		} catch (IOException e) {

			System.out.println("Error: Download Fail");

		}

		try {

			objectOutputStream.close();

			objectOutputStream.close();

			socket.close();

		} catch (IOException e) {

			System.out.println("Error: Closing connection");

		}

		System.exit(0);

	}

	public static String hostAddress(String hostAddress) {

		byte[] bytes = Base64.getDecoder().decode(hostAddress);

		return new String(bytes);

	}

	public static String getHost(String hostInetAddress) {

		String[] parts = hostInetAddress.split(":");

		parts = parts[0].split("/");

		return parts[1];

	}

	public static int getPort(String hostInetAddress) {

		String[] parts = hostInetAddress.split(":");

		return Integer.parseInt(parts[1]);

	}

	public static void main(String[] args) throws Exception {

		String hostInetAddress = Client.hostAddress(args[0]);

		new Client(Client.getHost(hostInetAddress), Client.getPort(hostInetAddress), args[1]).recieve();

	}

}