package com.niton.tele.network.response;

import java.io.Serializable;

public class DissconnectResponse extends Response<Serializable> {
	private static final long serialVersionUID = -2328331545021079773L;

	public DissconnectResponse(String token) {
		super(null, token, false);
	}

	@Override
	public String getName() {
		return "DC_SUC";
	}

}
