package com.niton.net.pack.server.listeners;

import java.io.Serializable;
import java.net.Socket;

import com.niton.net.pack.NetworkListener;
import com.niton.net.pack.packs.Package;
import com.niton.net.pack.requests.Request;

/**
 * This is the SubConnectionReplayListener Class
 * @author Niton
 * @version 2018-04-18
 */
public class SubConnectionReplayListener implements NetworkListener {
	public SubConnectionReplayListener() {

	}

	/**
	 * @see com.niton.net.pack.NetworkListener#acceptPackage(com.niton.net.pack.packs.Package)
	 */
	@Override
	public boolean acceptPackage(Package<? extends Serializable> pack) {
		return false;
	}

	/**
	 * @see com.niton.net.pack.NetworkListener#onRecivePackage(com.niton.net.pack.packs.Package, java.net.Socket)
	 */
	@Override
	public void onRecivePackage(Package<? extends Serializable> pack, Socket conection) {
	}

	/**
	 * @see com.niton.net.pack.NetworkListener#onReciveRequest(com.niton.net.pack.requests.Request, java.net.Socket)
	 */
	@Override
	public void onReciveRequest(Request request, Socket conection) {
	}
}

