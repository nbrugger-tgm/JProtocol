package com.niton.net.test;

import java.io.Serializable;
import java.net.Socket;

import com.niton.net.pack.NetworkListener;
import com.niton.net.pack.packs.Package;
import com.niton.net.pack.requests.Request;

/**
 * This is the ClientMessageListener Class
 * @author Nils
 * @version 2018-06-21
 */
public class ClientMessageListener implements NetworkListener {
	/**
	 * @see com.niton.net.pack.NetworkListener#acceptPackage(com.niton.net.pack.packs.Package)
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

