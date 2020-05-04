package com.niton.net.pack;

import java.io.Serializable;
import java.net.Socket;

import com.niton.net.pack.packs.Package;
import com.niton.net.pack.requests.Request;

/**
 * A listener to listen for incomming network Packages
 *
 * @author M.Marincek, Nils Brugger
 */

public interface NetworkListener {

	/**
	 * Checks if the package is relevant to this listener.<br>
	 * if the package should be processed by this listener this method should return true
	 * 
	 * @param pack
	 *            The package to checked
	 * @return true, if the NetworkListener will react to this package
	 */
	public boolean acceptPackage(Package<? extends Serializable> pack);

	/**
	 * Handles package, if acceptPackage(Package) was true<br>
	 * <b>Attention<b/>Requests are not redirected to this class 
	 * @param pack
	 *            package to be handled
	 * @param conection
	 *            Socket from where the package came
	 */
	public void onRecivePackage(Package<? extends Serializable> pack, Socket conection);

	/**
	 * Handles requests.<br>
	 * To not block the connection you need to send a response to the `connection` socket. With Client/Server.sendPackage.
	 * The package needs to be of the `Package` class
	 *
	 * @param request
	 *            Request to be handled
	 * @param conection
	 *            Socket from where the request came. Send the response over this socket
	 */
	public void onReciveRequest(Request request, Socket conection);
}
