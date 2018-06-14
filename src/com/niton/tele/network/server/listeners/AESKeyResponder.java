package com.niton.tele.network.server.listeners;

import java.io.Serializable;
import java.net.Socket;

import com.niton.tele.crypto.SimpleAES;
import com.niton.tele.crypto.SimpleRSA;
import com.niton.tele.network.NetworkListener;
import com.niton.tele.network.packs.AESKeyPack;
import com.niton.tele.network.packs.Package;
import com.niton.tele.network.requests.Request;
import com.niton.tele.network.server.Server;
import com.niton.tele.network.server.Session;

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
		AESKeyPack AESpack = (AESKeyPack) pack;
		Session s = this.s.getSession(AESpack.getClientTolken());
		s.setAesKey(SimpleAES.getKey(SimpleRSA.decrypt(AESpack.getData(), s.getPriRsaKey())));
		this.s.setSession(AESpack.getClientTolken(), s);
	}

	// AES Key Reciver
	@Override
	public void onReciveRequest(Request request, Socket conection) {
	}
}
