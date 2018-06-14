package com.niton.tele.network.response;

import java.io.Serializable;

import com.niton.tele.network.packs.Package;

/**
 * The Response class provides a encryptable container to send serializable data
 * as a response to requests in networks
 *
 * @author M.Marincek
 *
 * @param <T>
 *            the data type to send in this package
 */
public abstract class Response<T extends Serializable> extends Package<T> {
	private static final long serialVersionUID = 6660575673894829320L;

	/**
	 * Initializes a new Response with its content (data), its token and if it will
	 * be encrypted
	 * 
	 * @param data
	 *            The data to send (data type: T)
	 * @param client
	 *            The token to authenticate the client on the server.<br>
	 *            If this is not set the server will not know who you are.<br>
	 *            Get the HERE: @link
	 *            {@link com.niton.tele.network.client.NetworkClient#getTolken()}
	 * @param encrypted
	 *            If true, this package will be encrypted
	 */
	public Response(T data, String client, boolean encrypted) {
		super(data, client, encrypted);
	}

	/**
	 * @see com.niton.tele.network.packs.Package#useSeperateSocket()
	 */
	@Override
	public final boolean useSeperateSocket() {
		return false;
	}
}
