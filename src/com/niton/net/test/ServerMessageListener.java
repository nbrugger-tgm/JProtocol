package com.niton.net.test;

import java.io.Serializable;
import java.net.Socket;

import com.niton.net.pack.NetworkListener;
import com.niton.net.pack.packs.Package;
import com.niton.net.pack.requests.Request;
import com.niton.net.pack.server.Server;

/**
 * This is the ServerMessageListener Class
 * @author Nils
 * @version 2018-06-21
 */
public class ServerMessageListener implements NetworkListener {
	private Server s;

	public ServerMessageListener(Server s) {
		super();
		this.s = s;
	}

	/**
	 * @see com.niton.net.pack.NetworkListener#acceptPackage(com.niton.net.pack.packs.Package)
	 */
	@Override
	public boolean acceptPackage(Package<? extends Serializable> pack) {
		return pack instanceof MessagePackge;
	}

	/**
	 * @see com.niton.net.pack.NetworkListener#onRecivePackage(com.niton.net.pack.packs.Package, java.net.Socket)
	 */
	@Override
	public void onRecivePackage(Package<? extends Serializable> pack, Socket conection) {
		s.brodcast(pack);
	}
	@Override
	public void onReciveRequest(Request request, Socket conection) {
	}
}

