package com.niton.net.pack.requests;

import java.io.Serializable;

import com.niton.net.pack.client.NetworkClient;
import com.niton.net.pack.response.Response;
import com.niton.net.pack.response.TolkenResponse;

public class TolkenRequest extends Request {
	private static final long serialVersionUID = -5276209189508767848L;
	private volatile transient NetworkClient client;
	public TolkenRequest(NetworkClient client) {
		super("", false);
		this.client = client;
	}

	@Override
	public void onResponse(Response<? extends Serializable> answer) {
		if(answer instanceof TolkenResponse) {
			TolkenResponse tolkenAnswer = (TolkenResponse) answer;
			client.setTolken(tolkenAnswer.getData());
			synchronized (client.getWaiter()) {
				client.getWaiter().notifyAll();
			}
		}
	}

	@Override
	public String getName() {
		return "Tolken";
	}

	/**
	 * @see com.niton.net.pack.packs.Package#useSeperateSocket()
	 */
	@Override
	public boolean useSeperateSocket() {
		return false;
	}

}
