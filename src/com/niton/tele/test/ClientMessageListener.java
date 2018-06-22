package com.niton.tele.test;

import java.io.Serializable;
import java.net.Socket;

import com.niton.tele.network.NetworkListener;
import com.niton.tele.network.packs.Package;
import com.niton.tele.network.requests.Request;

/**
 * This is the ClientMessageListener Class
 * @author Nils
 * @version 2018-06-21
 */
public class ClientMessageListener implements NetworkListener {
	/**
	 * @see com.niton.tele.network.NetworkListener#acceptPackage(com.niton.tele.network.packs.Package)
	 */
	@Override
	public boolean acceptPackage(Package<? extends Serializable> pack) {
		return pack instanceof MessagePackge;
	}

	@Override
	public void onRecivePackage(Package<? extends Serializable> pack, Socket conection) {
		System.out.println("Brodcast : "+pack.getData());
	}

	@Override
	public void onReciveRequest(Request request, Socket conection) {
	}
}

