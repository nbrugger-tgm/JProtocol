package com.niton.tele.network.server;

import java.net.Socket;

import javax.crypto.SecretKey;

public class ServerListener extends Thread {
	public static final String NO_TOLKEN = "X";
	private transient Server server;
	private boolean running = true;

	public ServerListener(Server s) {
		server = s;
		setName("Server Listener");
	}

	@Override
	public void destroy() {
		running = false;
		try {
			this.join();
		} catch (InterruptedException e) {
		}
	}

	public SecretKey getKeyForClient(String sock) {
		for (String tolken : server.getSessions().keySet())
			if (tolken.equals(sock)) {
				Session se = server.getSession(tolken);
				return se.getAesKey();
			}
		return null;
	}

	@Override
	public void interrupt() {
		running = false;
		try {
			this.join();
		} catch (InterruptedException e) {
		}
	}

	@Override
	public void run() {
		while (running) {
			try {
				Thread.sleep(1000 / server.getMaxPackagesPerSec());
			} catch (InterruptedException e1) {
				System.out.println("[Server] Listener Interupted!");
			}
			try {
				Socket s = server.getSocket().accept();
				if (server.getBlockedIps().contains(s.getInetAddress().getHostAddress())) {
					s.close();
					continue;
				}
				SubSocketHandler handler = new SubSocketHandler(server, this, s);
				handler.start();
			} catch (Exception e) {
				System.err.println("[Server] Error while accepting Connection : " + e);
			}
		}
	}

	/**
	 * Description :
	 *
	 * @author Niton
	 * @version 2018-04-11
	 */
	public void stopThread() {
		running = false;
		try {
			this.join();
		} catch (InterruptedException e) {
		}
	}
}
