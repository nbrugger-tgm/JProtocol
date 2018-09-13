package com.niton.net.text;

import java.net.Socket;

/**
 * This is the TextListener Class
 * @author Nils Brugger
 * @version 2018-09-13
 */
public interface TextListener {
	/**
	 * <b>Description :</b><br>
	 * Called if a connection to the server sends an string
	 * @author Nils Brugger
	 * @version 2018-09-13
	 * @param text
	 * @param s
	 */
	public void reciveText(String text,Socket s);
}

