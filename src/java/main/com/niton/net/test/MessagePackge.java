package com.niton.net.test;

import com.niton.net.pack.packs.Package;

/**
 * This is the MessagePackge Class
 * @author Nils
 * @version 2018-06-21
 */
public class MessagePackge extends Package<String> {
	/**
	 * Creates an Instance of MessagePackge.java
	 * @author Nils
	 * @version 2018-06-21
	 * @param data
	 * @param token
	 * @param encrypted
	 */
	public MessagePackge(String message, String token) {
		super(message, token, false);
	}

	private static final long serialVersionUID = 4194199597896597940L;
	/**
	 * @see com.niton.net.pack.packs.Package#getName()
	 */
	@Override
	public String getName() {
		return "msg_package";
	}

	/**
	 * @see com.niton.net.pack.packs.Package#useSeperateSocket()
	 */
	@Override
	public boolean useSeperateSocket() {
		return false;
	}
}

