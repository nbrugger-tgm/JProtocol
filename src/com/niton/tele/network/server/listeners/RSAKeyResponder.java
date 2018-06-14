package com.niton.tele.network.server.listeners;

import java.io.Serializable;
import java.net.Socket;
import java.security.KeyPair;

import com.niton.tele.crypto.SimpleRSA;
import com.niton.tele.network.NetworkListener;
import com.niton.tele.network.packs.Package;
import com.niton.tele.network.requests.RSARequest;
import com.niton.tele.network.requests.Request;
import com.niton.tele.network.response.RSAResponse;
import com.niton.tele.network.server.Server;
import com.niton.tele.network.server.Session;

/**
 * This is the RSAResponder Class
 *
 * @author Niton
 * @version 2018-04-13
 */
public class RSAKeyResponder implements NetworkListener {
	private Server s;

	public RSAKeyResponder(Server s) {
		super();
		this.s = s;
	}

	@Override
	public boolean acceptPackage(Package<? extends Serializable> pack) {
		return pack instanceof RSARequest;
	}

	@Override
	public void onRecivePackage(Package<? extends Serializable> pack, Socket conection) {
	}

	@Override
	public void onReciveRequest(Request request, Socket conection) {
		String tolken = request.getClientTolken();
		Session s = this.s.getSession(tolken);
		KeyPair pair = SimpleRSA.generateKeys();
		s.setPrivateRsaKey(pair.getPrivate());
		s.setPublicRsaKey(pair.getPublic());
		this.s.setSession(tolken, s);
		RSAResponse response = new RSAResponse(pair.getPublic(), tolken);
		this.s.sendPacket(response, conection);
	}
}
