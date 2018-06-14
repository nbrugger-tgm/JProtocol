package com.niton.tele.network.client.listeners;

import java.io.Serializable;
import java.net.Socket;

import com.niton.tele.network.NetworkListener;
import com.niton.tele.network.client.NetworkClient;
import com.niton.tele.network.packs.Package;
import com.niton.tele.network.requests.DissconectRequest;
import com.niton.tele.network.requests.Request;

/**
 * This is the ClientDissconnectListener Class
 * 
 * @author Niton
 * @version 2018-04-13
 */
public class ClientDissconnectListener implements NetworkListener {
	private NetworkClient client;

	public ClientDissconnectListener(NetworkClient client) {
		super();
		this.client = client;
	}

	@Override
	public boolean acceptPackage(Package<? extends Serializable> pack) {
		return pack instanceof DissconectRequest;
	}

	@Override
	public void onRecivePackage(Package<? extends Serializable> pack, Socket conection) {
	}

	@Override
	public void onReciveRequest(Request request, Socket conection) {
		client.shutdown(conection);
	}
}
