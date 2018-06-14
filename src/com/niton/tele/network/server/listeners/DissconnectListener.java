package com.niton.tele.network.server.listeners;

import java.io.Serializable;
import java.net.Socket;

import com.niton.tele.network.NetworkListener;
import com.niton.tele.network.packs.Package;
import com.niton.tele.network.requests.DissconectRequest;
import com.niton.tele.network.requests.Request;
import com.niton.tele.network.response.DissconnectResponse;
import com.niton.tele.network.server.Server;

/**
 * This is the DissconnectListener Class
 *
 * @author Niton
 * @version 2018-04-13
 */
public class DissconnectListener implements NetworkListener {
	private Server s;

	public DissconnectListener(Server s) {
		this.s = s;
	}

	@Override
	public boolean acceptPackage(Package<? extends Serializable> pack) {
		return pack instanceof DissconectRequest;
	}

	@Override
	public void onRecivePackage(com.niton.tele.network.packs.Package<? extends Serializable> pack, Socket conection) {
	}

	// Dissconnect
	@Override
	public void onReciveRequest(Request request, Socket conection) {
		s.sendPacket(new DissconnectResponse(request.getClientTolken()), conection);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		s.getSessions().remove(request.getClientTolken());
		System.gc();
	}
}
