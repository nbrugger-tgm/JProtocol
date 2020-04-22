package com.niton.net.pack.requests;

import java.io.Serializable;
import java.security.PublicKey;

import com.niton.net.pack.client.NetworkClient;
import com.niton.net.pack.response.RSAResponse;
import com.niton.net.pack.response.Response;

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
