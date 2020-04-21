package com.niton.net.pack.requests;

import java.io.Serializable;
import java.net.Socket;

import com.niton.net.pack.client.NetworkClient;
import com.niton.net.pack.packs.Package;
import com.niton.net.pack.response.Response;
import com.niton.net.pack.server.listeners.SubConnectionReplayListener;

/**
 * This is the SubConnectionPackage Class
 * @author Niton
 * @version 2018-04-18
 */
public final class SubConnectionRequest extends Request {
	private static final long serialVersionUID = 5154700825727581176L;
	private String name;
	private transient SubConnectionReplayListener listener;
	private Socket conection;
	public SubConnectionRequest(String clientToken,String name,SubConnectionReplayListener listener) {
		super(clientToken, false);
		this.name = name;
		this.listener = listener;
	}

	/**
	 * @see com.niton.net.pack.packs.Package#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @see com.niton.net.pack.packs.Package#useSeperateSocket()
	 */
	@Override
	public boolean useSeperateSocket() {
		return true;
	}

	/**
	 * @see com.niton.net.pack.requests.Request#onResponse(com.niton.net.pack.response.Response)
	 */
	@Override
	public void onResponse(Response<? extends Serializable> answer) {
		listener.onRecivePackage(answer, conection);
	}
}

