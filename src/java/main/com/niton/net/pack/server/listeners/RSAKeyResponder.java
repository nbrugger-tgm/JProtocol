package com.niton.net.pack.server.listeners;

import java.io.Serializable;
import java.net.Socket;
import java.security.KeyPair;

import com.niton.net.crypto.SimpleRSA;
import com.niton.net.pack.NetworkListener;
import com.niton.net.pack.packs.Package;
import com.niton.net.pack.requests.RSARequest;
import com.niton.net.pack.requests.Request;
import com.niton.net.pack.response.RSAResponse;
import com.niton.net.pack.server.Server;
import com.niton.net.pack.server.Session;

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
