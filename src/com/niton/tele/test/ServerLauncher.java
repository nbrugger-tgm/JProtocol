package com.niton.tele.test;

import java.io.IOException;

import com.niton.tele.network.server.Server;

/**
 * This is the Launcher Class
 * @author Nils
 * @version 2018-06-21
 */
public class ServerLauncher {
	/**
	 * Description : 
	 * @author Nils
	 * @version 2018-06-21
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Server chatServer = new Server(2, 5432, true);
		chatServer.addListener(new ServerMessageListener(chatServer));
		chatServer.start();
	}
}

