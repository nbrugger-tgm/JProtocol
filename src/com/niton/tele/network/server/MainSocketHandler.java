package com.niton.tele.network.server;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;

import javax.crypto.SealedObject;
import javax.crypto.SecretKey;

import com.niton.tele.crypto.SimpleAES;
import com.niton.tele.network.CounterInputStream;
import com.niton.tele.network.NetworkListener;
import com.niton.tele.network.packs.Package;
import com.niton.tele.network.requests.Request;

/**
 * This is the MainSocketHandler Class
 *
 * @author Niton
 * @version 2018-04-13
 */
public class MainSocketHandler extends Thread {
	private CounterInputStream counter;
	private ObjectInputStream input;
	private ServerListener listener;
	private Server server;
	private Socket socket;

	public MainSocketHandler(Socket mainSocket, ServerListener listener, Server server, ObjectInputStream instream,
			CounterInputStream counter) {
		socket = mainSocket;
		this.listener = listener;
		this.server = server;
		input = instream;
		this.counter = counter;
	}

	/**
	 * @see java.lang.Thread#run()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			main: while (true)
				while (socket.isConnected() && !socket.isClosed()) {
					System.out.println("[Server] Listen to main Socket of " + socket.getInetAddress());
					boolean encrypted;
					Package<? extends Serializable> raw;
					try {
						encrypted = (boolean) input.readObject();
						if (encrypted) {
							SecretKey aesKey = listener.getKeyForClient((String) input.readObject());
							raw = (Package<? extends Serializable>) SimpleAES
									.decryptObject((SealedObject) input.readObject(), aesKey);
						} else
							raw = (Package<? extends Serializable>) input.readObject();
					} catch (SocketException e) {
						System.err.println("[Server] Stopped listening to main socket of "
								+ socket.getRemoteSocketAddress().toString() + " because connection lost.");
						break main;
					}
					if (server.isLog())
						System.out.println("[Server] Recived Package : " + raw.getClass().getSimpleName());
					String tolken = raw.getClientTolken();
					if (!(tolken == null || tolken.equals(ServerListener.NO_TOLKEN) || tolken.equals("X")
							|| tolken.equals(""))) {
						Session ses = server.getSession(tolken);
						ses.recived(counter.getRecivedBytes());
						server.setSession(tolken, ses);
					}
					for (NetworkListener listener : server.getListeners())
						if (listener.acceptPackage(raw)) {
							if (server.isLog())
								System.out.println("[Server] Call Listener for this package");
							if (raw instanceof Request)
								listener.onReciveRequest((Request) raw, socket);
							else
								listener.onRecivePackage(raw, socket);
						}
				}
			if (server.isLog())
				System.out.println("[Server] Waiting for listening to main socket of "
						+ socket.getRemoteSocketAddress().toString());
		} catch (Exception e) {
			System.err.println(
					"[Server] Exception on Main Socket " + socket.getRemoteSocketAddress().toString() + " : " + e);
		}
	}

}
