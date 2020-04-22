package com.niton.net.pack.server.listeners;

import java.io.Serializable;
import java.net.Socket;

import com.niton.net.pack.NetworkListener;
import com.niton.net.pack.packs.Package;
import com.niton.net.pack.requests.PingRequest;
import com.niton.net.pack.requests.Request;
import com.niton.net.pack.response.PingResponse;
import com.niton.net.pack.server.Server;

/**
 * This is the PingResponder Class
 *
 * @author Niton
 * @version 2018-04-13
 */
public class PingResponder implements NetworkListener {
	private Server s;

	public PingResponder(Server s) {
		super();
		this.s = s;
	}

	@Override
	public boolean acceptPackage(Package<? extends Serializable> pack) {
		return  pack instanceof  PingRequest;
	}

	@Override
	public void onRecivePackage(Package<? extends Serializable> pack, Socket conection) {
	}

	@Override
	public void onReciveRequest(Request request, Socket conection) {
		s.sendPacket(new PingResponse(), conection);
	}
}
