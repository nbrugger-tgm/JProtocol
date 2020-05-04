package com.niton.net.pack.server;

import java.io.Serializable;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;

import javax.crypto.SecretKey;
/**
 * A session is used by the server to hold data about the connection with a client.
 * Containing the most important informations about the connection like exchanged bytes
 */
public class Session implements Serializable {
	private static final long serialVersionUID = -4688845051982836018L;
	private transient Socket connection;
	private long recivedBytes = 0;
	private long sendBytes = 0;
	private final long connectionStarted = System.currentTimeMillis();
	private SecretKey aesKey = null;
	private PrivateKey priRSAKey;
	private PublicKey pubRSAKey;
	private HashMap<String, Object> datas = new HashMap<>();

	public SecretKey getAesKey() {
		return aesKey;
	}
	/**
	 * Get the System.ms timestamp from the moment the connection was established
	 */
	public long getConectionStarted() {
		return connectionStarted;
	}

	/**
	 * Get the time the connections lasts in minutes
	 */
	public int getConnectedMinutes() {
		return (int) ((System.currentTimeMillis() - connectionStarted) / (1000 * 60) % 60);
	}

	/**
	 * Get the time the connections lasts in secconds
	 */
	public int getConnectedSecconds() {
		return (int) ((System.currentTimeMillis() - connectionStarted) / 1000) % 60;
	}
	/**
	 * @return the MainSocket of the connection
	 */
	public Socket getConnection() {
		return connection;
	}
	
	/**
	 * The server is able to save key,value pairs for each session/client
	 * This values can be received here
	 */
	public Object getData(String key) {
		return datas.get(key);
	}
	/**
	 * The server is able to save key,value pairs for each session/client
	 * This values can be received here
	 */
	public HashMap<String, Object> getDatas() {
		return datas;
	}
	
	public String getIp() {
		return connection.getInetAddress().getHostAddress();
	}

	public PrivateKey getPriRsaKey() {
		return priRSAKey;
	}

	public PublicKey getPubRsaKey() {
		return pubRSAKey;
	}

	public long getRecivedBytes() {
		return recivedBytes;
	}

	public long getSendBytes() {
		return sendBytes;
	}
	/**
	 * Adds to the received byte count
	 */
	public void recived(long bytes) {
		recivedBytes += bytes;
	}
	/**
	 * Adds to the sent byte count
	 */
	public void send(long bytes) {
		sendBytes += bytes;
	}

	public void setAesKey(SecretKey aesKey) {
		this.aesKey = aesKey;
	}

	public void setConnection(Socket connection) {
		this.connection = connection;
	}

	public void setData(String key, Object o) {
		if (datas.containsKey(key))
			datas.replace(key, o);
		else
			datas.put(key, o);
	}

	public void setPrivateRsaKey(PrivateKey priRsaKey) {
		priRSAKey = priRsaKey;
	}

	public void setPublicRsaKey(PublicKey pubRsaKey) {
		pubRSAKey = pubRsaKey;
	}

	public void setRecivedBytes(long recivedBytes) {
		this.recivedBytes = recivedBytes;
	}

	public void setSendBytes(long sendBytes) {
		this.sendBytes = sendBytes;
	}

	@Override
	public String toString() {
		int conMins = getConnectedMinutes();
		int conSecs = getConnectedSecconds();
		int conHours = conMins / 60;
		String sendToServer;
		if (recivedBytes >= Math.pow(10, 9))
			sendToServer = recivedBytes / Math.pow(10, 9) + " GB";
		else if (recivedBytes >= Math.pow(10, 6))
			sendToServer = recivedBytes / Math.pow(10, 6) + " MB";
		else if (recivedBytes >= Math.pow(10, 3))
			sendToServer = recivedBytes / Math.pow(10, 3) + " KB";
		else
			sendToServer = recivedBytes + " Byte";
		String sendToClient = "";
		if (sendBytes >= Math.pow(10, 9))
			sendToClient = sendBytes / Math.pow(10, 9) + " GB";
		else if (recivedBytes >= Math.pow(10, 6))
			sendToClient = sendBytes / Math.pow(10, 6) + " MB";
		else if (recivedBytes >= Math.pow(10, 3))
			sendToClient = sendBytes / Math.pow(10, 3) + " KB";
		else
			sendToClient = sendBytes + " Byte";
		String s = "Session {\n" + "\tIp4: " + getConnection() + "\n" + "\tConnected Time: " + conHours + ":" + conMins
				+ ":" + conSecs + "\n" + "\tSend To Server : " + sendToServer + "\n" + "\tSend To Client : "
				+ sendToClient + "\n" + "\tConnection Encrypted : " + (aesKey != null) + "\n}";
		return s;
	}
}
