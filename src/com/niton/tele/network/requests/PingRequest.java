package com.niton.tele.network.requests;

import java.io.Serializable;

import com.niton.tele.network.client.NetworkClient;
import com.niton.tele.network.response.PingResponse;
import com.niton.tele.network.response.Response;
import com.niton.tele.network.server.ServerListener;

public class PingRequest extends Request {
	private static final long serialVersionUID = -8291859782495389057L;
	transient private Object lock;
	private transient NetworkClient cl;
	private transient long s;

	public PingRequest(NetworkClient client, Object lock, long start) {
		super(ServerListener.NO_TOLKEN, false);
		this.lock = lock;
		cl = client;
		s = start;
	}

	@Override
	public String getName() {
		return "PING";
	}

	@Override
	public void onResponse(Response<? extends Serializable> answer) {
		if (answer instanceof PingResponse) {
			int differeenz = (int) (System.currentTimeMillis() - s);
			cl.setLastPing(differeenz / 2);
			System.out.println("Ping Response : " + differeenz + "ms");
			synchronized (lock) {
				lock.notify();
			}

		}
	}

	/**
	 * @see com.niton.tele.network.packs.Package#useSeperateSocket()
	 */
	@Override
	public boolean useSeperateSocket() {
		return cl.getSocket() != null;
	}
}
