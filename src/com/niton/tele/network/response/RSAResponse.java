package com.niton.tele.network.response;

import java.security.PublicKey;

public class RSAResponse extends Response<PublicKey> {
	private static final long serialVersionUID = -5658062860747339964L;

	public RSAResponse(PublicKey data, String client) {
		super(data, client, false);
	}

	@Override
	public String getName() {
		return "RSA_PK";
	}

}
