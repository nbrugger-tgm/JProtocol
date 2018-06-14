package com.niton.tele.network.server.listeners;

import java.io.Serializable;
import java.net.Socket;

import com.niton.tele.network.NetworkListener;
import com.niton.tele.network.packs.Package;
import com.niton.tele.network.requests.Request;
import com.niton.tele.network.requests.TokenRequest;
import com.niton.tele.network.response.TokenResponse;
import com.niton.tele.network.server.Server;
import com.niton.tele.network.server.ServerListener;
import com.niton.tele.network.server.Session;

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
