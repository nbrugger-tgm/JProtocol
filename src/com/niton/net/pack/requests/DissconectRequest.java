package com.niton.net.pack.requests;

import java.io.IOException;
import java.io.Serializable;

import com.niton.net.pack.client.NetworkClient;
import com.niton.net.pack.response.Response;
import com.niton.net.pack.server.Server;
import com.niton.net.pack.server.ServerListener;

public class DissconectRequest extends Request {
	private static final long serialVersionUID = 1L;
	private transient NetworkClient client = null;
	private transient Server server = null;

	public DissconectRequest(NetworkClient client) {
		super(client.getTolken(), false);
		this.client = client;
	}

	public DissconectRequest(Server server) {
		super(ServerListener.NO_TOLKEN, false);
		this.server = server;
	}

	@Override
	public String getName() {
		return "DIS_CON_COMP";
	}

	@Override
	public void onResponse(Response<? extends Serializable> answer) {
		try {
			if (client != null) {
				client.getInputListener().interrupt();
				client.getSender().interrupt();
				client.getSocket().close();
			} else {
				server.getSession(answer.getClientTolken()).getConnection().close();
				server.getSessions().remove(answer.getClientTolken());

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see com.niton.net.pack.packs.Package#useSeperateSocket()
	 */
	@Override
	public boolean useSeperateSocket() {
		return false;
	}

}
