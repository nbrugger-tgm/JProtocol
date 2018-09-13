package com.niton.net.pack.response;

public class TokenResponse extends Response<String> {

	/**
	 * <b>Type:</b> long<br>
	 * <b>Description:</b><br>
	 */
	private static final long serialVersionUID = 985520440376804684L;

	public TokenResponse(String data, String client) {
		super(data, client, false);
	}

	@Override
	public String getName() {
		return "Tolken";
	}

}
