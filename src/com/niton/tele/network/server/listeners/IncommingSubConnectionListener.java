package com.niton.tele.network.server.listeners;

import java.io.Serializable;
import java.net.Socket;

import com.niton.tele.network.NetworkListener;
import com.niton.tele.network.packs.Package;
import com.niton.tele.network.requests.Request;
import com.niton.tele.network.requests.SubConnectionPackage;

/**
 * This is the SubConnectionListener Class
 * @author Niton
 * @version 2018-04-18
 */
public abstract class IncommingSubConnectionListener implements NetworkListener {
	/**
	 * @see com.niton.tele.network.NetworkListener#acceptPackage(com.niton.tele.network.packs.Package)
	 */
	@Override
	public final boolean acceptPackage(Package<? extends Serializable> pack) {
		return pack instanceof SubConnectionPackage;
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

