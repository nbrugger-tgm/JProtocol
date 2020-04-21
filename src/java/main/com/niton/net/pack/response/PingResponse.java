package com.niton.net.pack.response;

import java.io.Serializable;

public class PingResponse extends Response<Serializable> {

	/**
	 * <b>Type:</b> long<br>
	 * <b>Description:</b><br>
	 */
	private static final long serialVersionUID = -2631223206419447368L;

	public PingResponse() {
		super(null, "", false);
	}

	@Override
	public String getName() {
		return "PING";
	}

}
