package com.niton.net.pack.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.crypto.SealedObject;

import com.niton.net.crypto.SimpleAES;
import com.niton.net.pack.CounterInputStream;
import com.niton.net.pack.CounterOutputStream;
import com.niton.net.pack.NetworkListener;
import com.niton.net.pack.packs.Package;
import com.niton.net.pack.requests.DissconectRequest;
import com.niton.net.pack.requests.Request;
import com.niton.net.pack.requests.SubConnectionRequest;
import com.niton.net.pack.response.Response;
import com.niton.net.pack.server.listeners.AESKeyResponder;
import com.niton.net.pack.server.listeners.DissconnectListener;
import com.niton.net.pack.server.listeners.MainSocketListener;
import com.niton.net.pack.server.listeners.PingResponder;
import com.niton.net.pack.server.listeners.RSAKeyResponder;
import com.niton.net.pack.server.listeners.TokenRequestListener;

/**
 * The Server class provides a
 *
 * @author VonPech
 */
public class Server {
	final static String line = "\n";
	private HashMap<String, Session> sessions = new HashMap<>();
	private ServerSocket socket;
	private ArrayList<NetworkListener> listeners = new ArrayList<>();
	private ArrayList<String> blockedIps = new ArrayList<>();
	private ServerListener listener;
	private int maxPackagesPerSec = 10;
	private final boolean log;

	public final int TOLKEN_SIZE;

	public Server(int tolkenSize, int port, boolean log) throws IOException {
		TOLKEN_SIZE = tolkenSize;
		this.log = log;
		socket = new ServerSocket(port);
		listener = new ServerListener(this);
		addListener(new TokenRequestListener(this));
		addListener(new PingResponder(this));
		addListener(new RSAKeyResponder(this));
		addListener(new AESKeyResponder(this));
		addListener(new MainSocketListener(this));
		addListener(new DissconnectListener(this));
	}

	public void addListener(NetworkListener listeners) {
		this.listeners.add(listeners);
	}

	/**
	 * Adds the IP to the blocking list. And kicks it if its connected.<br>
	 * Blocked IP'a are not able to connect
	 *
	 * @param IP
	 *            the IP to block
	 * @return if the IP was connected
	 */
	public boolean block(String IP, int delay) {
		synchronized (blockedIps) {
			blockedIps.add(IP);
		}
		return kick(IP, delay) > 0;
	}

	public void brodcast(Package<? extends Serializable> pack) {
		for (String key : sessions.keySet())
			sendPacket(pack, key);
	}

	public String generateTolken() {
		Random r = new Random();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < TOLKEN_SIZE; i++)
			builder.append((char) r.nextInt());
		return builder.toString();
	}

	public ArrayList<String> getBlockedIps() {
		return blockedIps;
	}

	public ServerListener getListener() {
		return listener;
	}

	public ArrayList<NetworkListener> getListeners() {
		return listeners;
	}

	public int getMaxPackagesPerSec() {
		return maxPackagesPerSec;
	}

	public Session getSession(String tolken) {
		return sessions.get(tolken);
	}

	public HashMap<String, Session> getSessions() {
		return sessions;
	}

	public HashMap<String, Session> getSessionsFromIp(String IP) {
		HashMap<String, Session> sessiones = new HashMap<>();
		for (String key : sessions.keySet()) {
			Session s = sessions.get(key);
			if (s.getConnection().getInetAddress().getHostAddress().equals(IP))
				sessiones.put(key, s);
		}
		return sessiones;
	}

	public ServerSocket getSocket() {
		return socket;
	}

	/**
	 * @return the log
	 */
	public boolean isLog() {
		return log;
	}

	public void kick(Session s, String token, int delay) {
		sendPacket(new DissconectRequest(this), s.getConnection());
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (sessions.containsKey(token)) {
					System.out.println("[Server] Client " + s.getIp() + "  has exceeded the time limit of "
							+ delay / 1000 + " sec");
					try {
						s.getConnection().close();
					} catch (IOException e) {
						System.err.println("[Server] An error ocured during cutting of Client : " + e);
					}
					sessions.remove(token);
				}

			}
		}).start();
	}

	/**
	 * Kicks all sessions of this IP
	 *
	 * @param IP
	 *            the IP to kick
	 * @return the count of cutted connectiones
	 */
	public int kick(String IP, int delay) {
		final HashMap<String, Session> sessiones = getSessionsFromIp(IP);
		for (String token : sessiones.keySet())
			kick(sessiones.get(token), token, delay);
		return sessiones.size();
	}

	/**
	 * Overwrites the blocked IPs
	 *
	 * @param in
	 *            the source
	 * @throws IOException
	 */
	public void readBlockedList(InputStream in) throws IOException {
		byte[] all = new byte[in.available()];
		in.read(all);
		blockedIps.clear();
		String s = new String(all, "UTF-8");
		for (String ip : s.split(line))
			if (ip.length() >= 7)
				blockedIps.add(ip);
	}

	@SuppressWarnings("unchecked")
	public void saveBlockedList(OutputStream file) throws IOException {
		StringBuilder textRep = new StringBuilder();
		for (String string : (ArrayList<String>) blockedIps.clone())
			textRep.append(string + line);
		file.write(textRep.toString().getBytes("UTF-8"));
	}

	@SuppressWarnings("unchecked")
	public void sendPacket(Package<? extends Serializable> pack, Socket sock) {
		try {
			System.out.println("[Server] Try send " + pack.getClass().getSimpleName());
			OutputStream base;
			base = sock.getOutputStream();

			CounterOutputStream cos = new CounterOutputStream(base);
			@SuppressWarnings("resource")
			ObjectOutputStream packageStream = new ObjectOutputStream(cos);
			packageStream.writeObject(pack.isEncrypted());
			Object o;
			Session s = null;
			String tolken = pack.getClientTolken();
			s = getSession(tolken);
			if (pack.isEncrypted())
				o = SimpleAES.encryptObject(pack, s.getAesKey());
			else
				o = pack;
			packageStream.writeObject(o);
			if (s != null) {
				s.send(cos.getSendBytes());
				sessions.replace(tolken, s);
			}
		} catch (Exception e) {
			System.out.println("[Server] Sending aborted");
			e.printStackTrace();
		}
		try {
			if (pack instanceof Request) {
				Session s = getSession(pack.getClientTolken());
				InputStream baseIn = sock.getInputStream();
				CounterInputStream cis = new CounterInputStream(baseIn);
				ObjectInputStream oin = new ObjectInputStream(cis);
				if (log)
					System.out.println("[Server] Recive Response");
				Object o = oin.readObject();
				Response<? extends Serializable> res;
				if (pack.isEncrypted())
					res = (Response<? extends Serializable>) SimpleAES.decryptObject((SealedObject) o, s.getAesKey());
				else
					res = (Response<? extends Serializable>) o;
				((Request) pack).onResponse(res);
				if (s != null) {
					s.recived(cis.getRecivedBytes());
					sessions.replace(pack.getClientTolken(), s);
				}
				oin.close();
				sock.close();
			}
		} catch (Exception e) {
			System.err.println("[Server] Reciving Request failed : " + e);
		}

	}

	public void sendPacket(Package<? extends Serializable> pack, String tolken) {

		new Thread(new Runnable() {

			@SuppressWarnings({ "unchecked", "resource" })
			@Override
			public void run() {
				try {
					if(log)
						System.out.println("[Server] Try send " + pack.getClass().getSimpleName());
					pack.setClientTolken(tolken);
					Session s = getSession(tolken);
					Socket sock;
//					if (pack.useSeperateSocket())
//						sock = new Socket(s.getConnection().getInetAddress(), s.getConnection().getPort());
//					else
						sock = s.getConnection();
					OutputStream base = sock.getOutputStream();
					CounterOutputStream cos = new CounterOutputStream(base);
					ObjectOutputStream packageStream = new ObjectOutputStream(cos);
					packageStream.writeObject(pack.isEncrypted());
					Serializable o;
					if (pack.isEncrypted())
						o = SimpleAES.encryptObject(pack, s.getAesKey());
					else
						o = pack;
					packageStream.writeObject(o);
					s.send(cos.getSendBytes());
					sessions.replace(tolken, s);
					if (pack instanceof Request) {
						InputStream baseIn = s.getConnection().getInputStream();
						CounterInputStream cin = new CounterInputStream(baseIn);
						ObjectInputStream oin = new ObjectInputStream(cin);
						Response<? extends Serializable> res;
						if (log)
							System.out.println("[Server] Recive Response");
						if (pack.isEncrypted())
							res = (Response<? extends Serializable>) SimpleAES
									.decryptObject((SealedObject) oin.readObject(), s.getAesKey());
						else
							res = (Response<? extends Serializable>) oin.readObject();
						s.recived(cin.getRecivedBytes());
						sessions.replace(tolken, s);
						((Request) pack).onResponse(res);
					}
					if (pack.useSeperateSocket() && !(pack instanceof SubConnectionRequest)) {
						packageStream.close();
						base.close();
						sock.close();
					}
				} catch (Exception e) {
					System.err.println("[Server] Error while send Packet or Recive Response : ");
					e.printStackTrace();
				}
			}
		}, "Server Sender").start();
	}

	public void setBlockedIps(ArrayList<String> blockedIps) {
		this.blockedIps = blockedIps;
	}

	public void setMaxPackagesPerSec(int maxPackagesPerSec) {
		this.maxPackagesPerSec = maxPackagesPerSec;

	}

	public void setSession(String clientTolken, Session value) {
		if (sessions.containsKey(clientTolken))
			sessions.replace(clientTolken, value);
		else
			sessions.put(clientTolken, value);
	}

	public void setSessions(HashMap<String, Session> sessions) {
		this.sessions = sessions;
	}

	public void start() {
		listener.start();
	}

	public void stop(int delay) {
		for (String string : sessions.keySet()) {
			Session s = sessions.get(string);
			kick(s.getIp(), delay);
		}
		Object lock = new Object();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				try {
					socket.close();
				} catch (IOException e) {
				}
				listener.interrupt();
				synchronized (lock) {
					lock.notifyAll();
				}
			}
		}).start();

		synchronized (lock) {
			try {
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Removes the IP to the blocking list.<br>
	 * Blocked IP'a are not able to connect
	 *
	 * @param IP
	 *            the IP to unblock
	 */
	public void unblock(String IP) {
		synchronized (blockedIps) {
			blockedIps.remove(IP);
		}
	}
}
