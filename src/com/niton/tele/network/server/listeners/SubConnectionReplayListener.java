package com.niton.tele.network.server.listeners;

import java.io.Serializable;
import java.net.Socket;

import com.niton.tele.network.NetworkListener;
import com.niton.tele.network.packs.Package;
import com.niton.tele.network.requests.Request;

/**
 * This is the SubConnectionReplayListener Class
 * @author Niton
 * @version 2018-04-18
 */
public class SubConnectionReplayListener implements NetworkListener {
	public SubConnectionReplayListener() {

	}

	/**
	 * @see com.niton.tele.network.NetworkListener#acceptPackage(com.niton.tele.network.packs.Package)
	 */
	@Override
	public boolean acceptPackage(Package<? extends Serializable> pack) {
		return false;
	}

	/**
	 * @see com.niton.tele.network.NetworkListener#onRecivePackage(com.niton.tele.network.packs.Package, java.net.Socket)
	 */
	@Override
	public void onRecivePackage(Package<? extends Serializable> pack, Socket conection) {
	}

	/**
	 * @see com.niton.tele.network.NetworkListener#onReciveRequest(com.niton.tele.network.requests.Request, java.net.Socket)
	 */
	@Override
	public void onReciveRequest(Request request, Socket conection) {
	}
}

