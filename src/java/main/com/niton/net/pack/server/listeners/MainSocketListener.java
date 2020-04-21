package com.niton.net.pack.server.listeners;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;

import com.niton.net.pack.CounterInputStream;
import com.niton.net.pack.NetworkListener;
import com.niton.net.pack.packs.MainSocketPacket;
import com.niton.net.pack.packs.Package;
import com.niton.net.pack.requests.Request;
import com.niton.net.pack.server.MainSocketHandler;
import com.niton.net.pack.server.Server;
import com.niton.net.pack.server.Session;

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
