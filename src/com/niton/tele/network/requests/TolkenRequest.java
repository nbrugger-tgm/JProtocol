package com.niton.tele.network.requests;

import java.io.Serializable;

import com.niton.tele.network.client.NetworkClient;
import com.niton.tele.network.response.Response;
import com.niton.tele.network.response.TolkenResponse;

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

}
