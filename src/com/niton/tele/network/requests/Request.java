package com.niton.tele.network.requests;

import java.io.Serializable;

import com.niton.tele.network.packs.Package;
import com.niton.tele.network.response.Response;

/**
 * The Request class provides a encryptable container to ask for and catch
 * response in networks
 *
 * @author M.Marincek
 */
public abstract class Request extends Package<Serializable> {
	private static final long serialVersionUID = -1278103283509235543L;

	/**
	 * Initializes a new Package with its token and if it will be encrypted
	 * 
	 * @param token
	 *            The token to authenticate the client on the server.<br>
	 *            If this is not set the server will not know who you are.<br>
	 *            Get the HERE: @link
	 *            {@link com.niton.tele.network.client.NetworkClient#getTolken()}
	 * @param encrypted
	 *            If true, this package will be encrypted
	 */
	public Request(String token, boolean encrypted) {
		super(null, token, encrypted);
	}

	/**
	 * Will be called, when if the Server/Client responses.
	 * 
	 * @param answer
	 *            The Response to handle
	 */
	public abstract void onResponse(Response<? extends Serializable> answer);

}
