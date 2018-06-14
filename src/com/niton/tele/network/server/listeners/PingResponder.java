package com.niton.tele.network.server.listeners;

import java.io.Serializable;
import java.net.Socket;

import com.niton.tele.network.NetworkListener;
import com.niton.tele.network.packs.Package;
import com.niton.tele.network.requests.Request;
import com.niton.tele.network.response.PingResponse;
import com.niton.tele.network.server.Server;

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
		return pack.getName().equals("PING");
	}

	@Override
	public void onRecivePackage(Package<? extends Serializable> pack, Socket conection) {
	}

	@Override
	public void onReciveRequest(Request request, Socket conection) {
		s.sendPacket(new PingResponse(), conection);
	}
}
