package com.niton.net.pack.requests;

import java.io.Serializable;

import com.niton.net.pack.client.NetworkClient;
import com.niton.net.pack.response.Response;
import com.niton.net.pack.response.TokenResponse;

public class TokenRequest extends Request {
	private static final long serialVersionUID = -5276209189508767848L;
	private volatile transient NetworkClient client;

	public TokenRequest(NetworkClient client) {
		super("", false);
		this.client = client;
	}

	@Override
	public String getName() {
		return "Tolken";
	}

	@Override
	public void onResponse(Response<? extends Serializable> answer) {
		if (answer instanceof TokenResponse) {
			TokenResponse tolkenAnswer = (TokenResponse) answer;
			client.setTolken(tolkenAnswer.getData());
			synchronized (client.getWaiter()) {
				client.getWaiter().notifyAll();
			}
		}
	}

	/**
	 * @see com.niton.net.pack.packs.Package#useSeperateSocket()
	 */
	@Override
	public boolean useSeperateSocket() {
		return true;
	}

}
