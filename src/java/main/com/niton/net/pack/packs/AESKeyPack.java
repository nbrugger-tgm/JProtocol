package com.niton.net.pack.packs;

public class AESKeyPack extends Package<byte[]> {
	/**
	 * <b>Type:</b> long<br>
	 * <b>Description:</b><br>
	 */
	private static final long serialVersionUID = -2120012139026279476L;

	public AESKeyPack(byte[] data, String tolken) {
		super(data, tolken, false);
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

}
