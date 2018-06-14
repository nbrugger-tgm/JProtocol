package com.niton.tele.network.packs;

import java.io.Serializable;

/**
 * The Package class provides a encryptable container to send serializable data
 * in networks
 *
 * @author M.Marincek
 *
 * @param <T>
 *            the data type to send in this package
 */
public abstract class Package<T extends Serializable> implements Serializable {
	private static final long serialVersionUID = -4747400176312605961L;
	private T data;
	private String clientToken;
	private boolean encrypted;

	/**
	 * Initializes a new Package with its content (data), its token and if it will
	 * be encrypted
	 * 
	 * @param data
	 *            The data to send (data type: T)
	 * @param token
	 *            The token to authenticate the client on the server.<br>
	 *            If this is not set the server will not know who you are.<br>
	 *            Get the HERE: @link
	 *            {@link com.niton.tele.network.client.NetworkClient#getTolken()}
	 * @param encrypted
	 *            If true, this package will be encrypted
	 */
	public Package(T data, String token, boolean encrypted) {
		setData(data);
		setClientTolken(token);
		setEncrypted(encrypted);
	}

	public String getClientTolken() {
		return clientToken;
	}

	public T getData() {
		return data;
	}

	/**
	 * Returns the String with represents the package's name
	 * 
	 * @return the package's name
	 */
	public abstract String getName();

	public boolean isEncrypted() {
		return encrypted;
	}

	public void setClientTolken(String clientToken) {
		this.clientToken = clientToken;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void setEncrypted(boolean encrypted) {
		this.encrypted = encrypted;
	}

	@Override
	public String toString() {
		return "Network package[\n\tType : " + getClass().getSimpleName() + " \n\tData : " + data + "\n\tClient : "
				+ getClientTolken() + "\n\tEncrypted : " + encrypted + "\n]";
	}

	/**
	 * Description : Should be true if the package maybe is big.<br>
	 * If the method returns true a separate socket will be opened to send the
	 * package
	 * 
	 * @author Niton
	 * @version 2018-04-13
	 * @return
	 */
	public abstract boolean useSeperateSocket();
}
