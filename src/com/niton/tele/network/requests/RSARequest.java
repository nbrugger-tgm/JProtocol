package com.niton.tele.network.requests;

import java.io.Serializable;
import java.security.PublicKey;

import com.niton.tele.network.client.NetworkClient;
import com.niton.tele.network.response.RSAResponse;
import com.niton.tele.network.response.Response;

public class RSARequest extends Request {
	private static final long serialVersionUID = -8956453699365256538L;
	private transient NetworkClient client;

	public RSARequest(NetworkClient client) {
		super(client.getTolken(), false);
		this.client = client;
	}

	@Override
	public String getName() {
		return "RSA_Q";
	}

	@Override
	public void onResponse(Response<? extends Serializable> answer) {
		if (answer instanceof RSAResponse) {
			client.setRsaKey((PublicKey) answer.getData());
			synchronized (client.getWaiter()) {
				client.getWaiter().notifyAll();
			}
		}
	}

	/**
	 * @see com.niton.tele.network.packs.Package#useSeperateSocket()
	 */
	@Override
	public boolean useSeperateSocket() {
		return true;
	}

}
