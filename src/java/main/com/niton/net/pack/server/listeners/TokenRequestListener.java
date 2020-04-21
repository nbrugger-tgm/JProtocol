package com.niton.net.pack.server.listeners;

import java.io.Serializable;
import java.net.Socket;

import com.niton.net.pack.NetworkListener;
import com.niton.net.pack.packs.Package;
import com.niton.net.pack.requests.Request;
import com.niton.net.pack.requests.TokenRequest;
import com.niton.net.pack.response.TokenResponse;
import com.niton.net.pack.server.Server;
import com.niton.net.pack.server.ServerListener;
import com.niton.net.pack.server.Session;

/**
 * This is the TokenRequestListener Class
 *
 * @author Niton
 * @version 2018-04-13
 */
public class TokenRequestListener implements NetworkListener {
	private Server s;

	public TokenRequestListener(Server s) {
		super();
		this.s = s;
	}

	@Override
	public boolean acceptPackage(Package<? extends Serializable> pack) {
		return pack instanceof TokenRequest;
	}

	@Override
	public void onRecivePackage(Package<? extends Serializable> pack, Socket conection) {
	}

	// Tolken listener
	@Override
	public void onReciveRequest(Request request, Socket conection) {
		String tolken = s.generateTolken();
		while (s.getSessions().containsKey(tolken) || tolken.equals(ServerListener.NO_TOLKEN))
			tolken = s.generateTolken();
		TokenResponse answer = new TokenResponse(tolken, tolken);
		s.getSessions().put(tolken, new Session());
		s.sendPacket(answer, conection);
	}
}
