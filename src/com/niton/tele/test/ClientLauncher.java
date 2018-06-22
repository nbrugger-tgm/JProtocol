package com.niton.tele.test;

import java.util.Scanner;

import com.niton.tele.network.client.NetworkClient;

/**
 * This is the ClientLauncher Class
 * 
 * @author Nils
 * @version 2018-06-21
 */
public class ClientLauncher {
	public static void main(String[] args) throws Exception {
		NetworkClient client = new NetworkClient("localhost", 5432, false);
		client.addListener(new ClientMessageListener());
		client.connect(1000);
		Scanner s = new Scanner(System.in);
		while (true)
			client.sendPackage(new MessagePackge(s.nextLine(), client.getTolken()));
	}
}
