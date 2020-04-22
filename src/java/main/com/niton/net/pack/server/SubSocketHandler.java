package com.niton.net.pack.server;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

import javax.crypto.SealedObject;
import javax.crypto.SecretKey;

import com.niton.net.crypto.SimpleAES;
import com.niton.net.pack.CounterInputStream;
import com.niton.net.pack.NetworkListener;
import com.niton.net.pack.packs.MainSocketPacket;
import com.niton.net.pack.packs.Package;
import com.niton.net.pack.requests.Request;
import com.niton.net.pack.server.listeners.MainSocketListener;

/**
 * This is the SubSocketHandler Class
 *
 * @author Niton
 * @version 2018-04-15
 */
public class SubSocketHandler extends Thread {
	private Server server;
	private ServerListener listener;
	private Socket s;

	public SubSocketHandler(Server server, ServerListener listener, Socket s) {
		super();
		this.server = server;
		this.listener = listener;
		this.s = s;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			InputStream in = s.getInputStream();
			CounterInputStream cis = new CounterInputStream(in);
			ObjectInputStream oin = new ObjectInputStream(cis);
			boolean encrypted = (boolean) oin.readObject();
			Package<? extends Serializable> raw;
			if (encrypted) {
				SecretKey aesKey = listener.getKeyForClient((String) oin.readObject());
				raw = (Package<? extends Serializable>) SimpleAES.decryptObject((SealedObject) oin.readObject(),
						aesKey);
			} else
				raw = (Package<? extends Serializable>) oin.readObject();
			if (server.isLog())
				System.out.println("[Server] Recived Package : " + raw.getClass().getSimpleName());
			String tolken = raw.getClientTolken();
			if (!(tolken == null || tolken.equals(ServerListener.NO_TOLKEN) || tolken.equals("X")
					|| tolken.equals(""))) {
				Session ses = server.getSession(tolken);
				ses.recived(cis.getRecivedBytes());
				server.setSession(tolken, ses);
			}
			ArrayList<Thread> handlers = new ArrayList<>();
			for (NetworkListener listener : server.getListeners())
				if (listener.acceptPackage(raw)) {
					if (server.isLog())
						System.out.println("[Server] Call Listener for this package ("+listener.getClass().getSimpleName()+")");
					Runnable code = new Runnable() {
						@Override
						public void run() {
							synchronized (this) {
								notify();
							}
							if (listener instanceof MainSocketListener) {
								MainSocketListener lis = (MainSocketListener) listener;
								lis.stream = oin;
								lis.counter = cis;
								lis.onRecivePackage(raw, s);
							} else if (raw instanceof Request)
								listener.onReciveRequest((Request) raw, s);
							else
								listener.onRecivePackage(raw, s);
						}
					};
					handlers.add(new Thread(code, "Network listener caller"));
				}
			for (Thread thread : handlers)
				synchronized (thread) {
					thread.start();
					thread.wait();
				}
			for (Thread thread : handlers)
				thread.join();
			if (!(raw instanceof MainSocketPacket)) {
				oin.close();
				cis.close();
				in.close();
				s.close();
			} else
				System.out.println("[Server] This is the main Socket. Not closing");
		} catch (Exception e) {
			System.err.println("[Server] Error while reciving Package: " + e);
		}
	}
}
