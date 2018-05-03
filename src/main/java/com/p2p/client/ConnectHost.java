package com.p2p.client;

import java.util.Base64;

public class ConnectHost {
	
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
	
}
