package com.niton.net.pack.packs;

import com.niton.net.pack.requests.Request;
import com.niton.net.pack.response.Response;

import java.io.Serializable;

public class AESKeyPack extends Request {
	/**
	 * <b>Type:</b> long<br>
	 * <b>Description:</b><br>
	 */
	private static final long serialVersionUID = -2120012139026279476L;

	public AESKeyPack(byte[] data, String tolken) {
		super(tolken, false);
		setData(data);
	}

	@Override
	public String getName() {
		return "AES_KEY";
	}

	/**
	 * @see com.niton.net.pack.packs.Package#useSeperateSocket()
	 */
	@Override
	public boolean useSeperateSocket() {
		return true;
	}

	@Override
	public void onResponse(Response<? extends Serializable> answer) {}
}
