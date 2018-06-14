package com.niton.tele.test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.niton.tele.network.client.NetworkClient;
import com.niton.tele.network.server.Server;

/**
 * This is the TestLauncher Class
 *
 * @author Niton
 * @version 2018-04-12
 */
public class TestLauncher {
	public static NetworkClient client;
	public static Server server;

	public static void main(String[] args) throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		try {
			server = new Server(10, 1234, true);
			client = new NetworkClient("localhost", 1234, true);
			server.start();
			client.connect(5000);
			System.out.println("[Client] Connection Complete!");
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
