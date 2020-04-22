package com.niton.net.pack.server.listeners;

import java.io.Serializable;
import java.net.Socket;

import com.niton.net.crypto.SimpleAES;
import com.niton.net.crypto.SimpleRSA;
import com.niton.net.pack.NetworkListener;
import com.niton.net.pack.packs.AESKeyPack;
import com.niton.net.pack.packs.Package;
import com.niton.net.pack.requests.Request;
import com.niton.net.pack.response.AESKeyReceivedResponse;
import com.niton.net.pack.server.Server;
import com.niton.net.pack.server.Session;

/**
 * This is the AESKeyResponder Class
 * 
 * @author Niton
 * @version 2018-04-13
 */
public class AESKeyResponder implements NetworkListener {
	private Server s;

	public AESKeyResponder(Server s) {
		super();
		this.s = s;
	}

	@Override
	public boolean acceptPackage(Package<? extends Serializable> pack) {
		return pack instanceof AESKeyPack;
	}

	@Override
	public void onRecivePackage(Package<? extends Serializable> pack, Socket conection) {
	}

	// AES Key Reciver
	@Override
	public void onReciveRequest(Request request, Socket conection) {
		AESKeyPack AESpack = (AESKeyPack) request;
		Session s = this.s.getSession(AESpack.getClientTolken());
		s.setAesKey(SimpleAES.getKey(SimpleRSA.decrypt((byte[]) AESpack.getData(), s.getPriRsaKey())));
		this.s.setSession(AESpack.getClientTolken(), s);
		this.s.sendPacket(new AESKeyReceivedResponse(request.getClientTolken()), conection);
	}
}
