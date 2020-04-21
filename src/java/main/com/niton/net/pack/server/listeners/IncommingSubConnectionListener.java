package com.niton.net.pack.server.listeners;

import java.io.Serializable;
import java.net.Socket;

import com.niton.net.pack.NetworkListener;
import com.niton.net.pack.packs.Package;
import com.niton.net.pack.requests.Request;
import com.niton.net.pack.requests.SubConnectionRequest;

/**
 * This is the SubConnectionListener Class
 * @author Niton
 * @version 2018-04-18
 */
public abstract class IncommingSubConnectionListener implements NetworkListener {
	/**
	 * @see com.niton.net.pack.NetworkListener#acceptPackage(com.niton.net.pack.packs.Package)
	 */
	@Override
	public final boolean acceptPackage(Package<? extends Serializable> pack) {
		return pack instanceof SubConnectionRequest;
	}
	
	public final void onRecivePackage(Package<? extends Serializable> pack, Socket conection) {
		if(reactToConnection((String) pack.getData()))
			onEstamblishConnection(conection);
	}

	@Override
	public final void onReciveRequest(Request request, Socket conection) {}

	public abstract void onEstamblishConnection(Socket newConnection);
	public abstract boolean reactToConnection(String name);
}

