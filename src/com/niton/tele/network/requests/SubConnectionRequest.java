package com.niton.tele.network.requests;

import java.io.Serializable;

import com.niton.tele.network.client.NetworkClient;
import com.niton.tele.network.packs.Package;
import com.niton.tele.network.response.Response;
import com.niton.tele.network.server.listeners.SubConnectionReplayListener;

/**
 * This is the SubConnectionPackage Class
 * @author Niton
 * @version 2018-04-18
 */
public final class SubConnectionRequest extends Request {
	private static final long serialVersionUID = 5154700825727581176L;
	private String name;
	private transient SubConnectionReplayListener listener;
	public SubConnectionRequest(String clientToken,String name,SubConnectionReplayListener listener) {
		super(clientToken, false);
		this.name = name;
		this.listener = listener;
	}

	/**
	 * @see com.niton.tele.network.packs.Package#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @see com.niton.tele.network.packs.Package#useSeperateSocket()
	 */
	@Override
	public boolean useSeperateSocket() {
		return true;
	}

	/**
	 * @see com.niton.tele.network.requests.Request#onResponse(com.niton.tele.network.response.Response)
	 */
	@Override
	public void onResponse(Response<? extends Serializable> answer) {
		listener.onRecivePackage(answer, conection);
	}
}

