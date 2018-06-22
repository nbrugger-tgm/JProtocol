package com.niton.tele.test;

import java.io.Serializable;
import java.net.Socket;

import com.niton.tele.network.NetworkListener;
import com.niton.tele.network.packs.Package;
import com.niton.tele.network.requests.Request;
import com.niton.tele.network.server.Server;

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
	 * @see com.niton.tele.network.NetworkListener#acceptPackage(com.niton.tele.network.packs.Package)
	 */
	@Override
	public boolean acceptPackage(Package<? extends Serializable> pack) {
		return pack instanceof MessagePackge;
	}

	/**
	 * @see com.niton.tele.network.NetworkListener#onRecivePackage(com.niton.tele.network.packs.Package, java.net.Socket)
	 */
	@Override
	public void onRecivePackage(Package<? extends Serializable> pack, Socket conection) {
		s.brodcast(pack);
	}
	@Override
	public void onReciveRequest(Request request, Socket conection) {
	}
}

