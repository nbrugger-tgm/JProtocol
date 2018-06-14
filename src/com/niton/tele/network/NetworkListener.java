package com.niton.tele.network;

import java.io.Serializable;
import java.net.Socket;

import com.niton.tele.network.packs.Package;
import com.niton.tele.network.requests.Request;

/**
 * The NetworkListener interface provides a listener for network-packages.
 *
 * @author M.Marincek
 */

public interface NetworkListener {

	/**
	 * Checks if the package is relevant to this listener
	 * 
	 * @param pack
	 *            The package to be checked
	 * @return true, if the NetworkListener will react to this package
	 */
	public boolean acceptPackage(Package<? extends Serializable> pack);

	/**
	 * Handles package, if acceptPackage(Package) was true
	 * 
	 * @param pack
	 *            package to be handled
	 * @param conection
	 *            Socket from where the package came
	 */
	public void onRecivePackage(Package<? extends Serializable> pack, Socket conection);

	/**
	 * Handles requests
	 * 
	 * @param request
	 *            Request to be handled
	 * @param conection
	 *            Socket from where the request came
	 */
	public void onReciveRequest(Request request, Socket conection);
}
