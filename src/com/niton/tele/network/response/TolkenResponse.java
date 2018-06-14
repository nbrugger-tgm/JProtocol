package com.niton.tele.network.response;

public class TolkenResponse extends Response<String> {

	public TolkenResponse(String data, String client) {
		super(data, client,false);
	}

	@Override
	public String getName() {
		return "Tolken";
	}

}
