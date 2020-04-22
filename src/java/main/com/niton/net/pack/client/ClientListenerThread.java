package com.niton.net.pack.client;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;

import javax.crypto.SealedObject;

import com.niton.net.crypto.SimpleAES;
import com.niton.net.pack.NetworkListener;
import com.niton.net.pack.packs.Package;
import com.niton.net.pack.requests.Request;
import com.niton.net.pack.response.Response;

/**
 * This is the ClientListenerThread Class
 *
 * @author Niton
 * @version 2018-04-11
 */
public class ClientListenerThread extends Thread {
	private NetworkClient c;
	private boolean running = true;
	private Request next;

	public ClientListenerThread(NetworkClient client) {
		c = client;
		setName("Client Listener");
	}

	/**
	 * @see java.lang.Thread#destroy()
	 */
	@Override
	public void destroy() {
		running = false;
		try {
			this.join();
		} catch (InterruptedException e) {
		}
	}

	/**
	 * @see java.lang.Thread#interrupt()
	 */
	@Override
	public void interrupt() {
		running = false;
		try {
			this.join();
		} catch (InterruptedException e) {
		}
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void run() {
		while (running) {

			if (!c.getSocket().isConnected() || c.getSocket().isClosed() || !c.getSocket().isBound()) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			try {
				Socket mainChannel = c.getSocket();
				InputStream s;
				s = mainChannel.getInputStream();
				if (c.isLog())
					System.out.println("[Client] Listen to Main Socket Input");
				if (!c.getSocket().isConnected() || c.getSocket().isClosed() || !c.getSocket().isBound())
					continue;
				ObjectInputStream ois = new ObjectInputStream(s);
				if (c.isLog())
					System.out.println("[Client] Package Input!");
				Package<? extends Serializable> pack;
				if ((boolean) ois.readObject())
					pack = (Package<? extends Serializable>) SimpleAES.decryptObject((SealedObject) ois.readObject(),
							c.getAesKey());
				else
					pack = (Package<? extends Serializable>) ois.readObject();
				if(next != null && pack instanceof Response){
					next.responde((Response<? extends Serializable>) pack);
					next = null;
					continue;
				}
				if (c.isLog())
					System.out.println("[Client] Recived Package : " + pack);
				for (NetworkListener nl : c.getListeners())
					if (nl.acceptPackage(pack))
						if (pack instanceof Request)
							nl.onReciveRequest((Request) pack, mainChannel);
						else
							nl.onRecivePackage(pack, mainChannel);
			} catch (Exception e) {
				System.err.println("[Client] Reciving Package on main socket failed : " + e);
				continue;
			}
		}
	}

	public void stopThread() {
		running = false;
		try {
			this.join();
		} catch (InterruptedException e) {
		}
	}

	public void notifyNextResponse(Request pack) {
		this.next = pack;
	}
}
