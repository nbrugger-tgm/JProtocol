package com.niton.tele.network.response;

import com.niton.tele.network.server.ServerListener;

public class ValidIdResponse extends Response<Boolean> {
	private static final long serialVersionUID = -4391411665069679397L;

	public ValidIdResponse() {
		super(true, ServerListener.NO_TOLKEN, true);
	}

	@Override
	public String getName() {
		return "VAL_ID_ANS";
	}

}
