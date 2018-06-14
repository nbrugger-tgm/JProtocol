package com.niton.tele.network.server.listeners;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;

import com.niton.tele.network.CounterInputStream;
import com.niton.tele.network.NetworkListener;
import com.niton.tele.network.packs.MainSocketPacket;
import com.niton.tele.network.packs.Package;
import com.niton.tele.network.requests.Request;
import com.niton.tele.network.server.MainSocketHandler;
import com.niton.tele.network.server.Server;
import com.niton.tele.network.server.Session;

/**
 * This is the MainSocketConnector Class
 *
 * @author Niton
 * @version 2018-04-13
 */
public class MainSocketListener implements NetworkListener {
	private Server s;

	public ObjectInputStream stream;

	public CounterInputStream counter;

	public MainSocketListener(Server s) {
		this.s = s;
	}

	@Override
	public boolean acceptPackage(Package<? extends Serializable> pack) {
		return pack instanceof MainSocketPacket;
	}

	@Override
	public void onRecivePackage(Package<? extends Serializable> pack, Socket conection) {
		if (s.isLog())
			System.out.println("[Server] Main Socket Connected");
		Session s = this.s.getSession(pack.getClientTolken());
		s.setConnection(conection);
		this.s.setSession(pack.getClientTolken(), s);
		if (this.s.isLog())
			System.out.println("[Server] Get Session : " + this.s.getSession(pack.getClientTolken()));
		MainSocketHandler handler = new MainSocketHandler(conection, this.s.getListener(), this.s, stream, counter);
		handler.start();
	}

	// Main Socket Listener
	@Override
	public void onReciveRequest(Request request, Socket conection) {
	}

}
