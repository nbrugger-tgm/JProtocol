package com.niton.net.pack.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import com.niton.net.crypto.SimpleAES;
import com.niton.net.crypto.SimpleRSA;
import com.niton.net.pack.CounterOutputStream;
import com.niton.net.pack.NetworkListener;
import com.niton.net.pack.client.listeners.ClientDissconnectListener;
import com.niton.net.pack.packs.AESKeyPack;
import com.niton.net.pack.packs.ConnectionSuccessPackage;
import com.niton.net.pack.packs.MainSocketPacket;
import com.niton.net.pack.packs.Package;
import com.niton.net.pack.requests.*;
import com.niton.net.pack.response.DissconnectResponse;
import com.niton.net.pack.response.Response;

/**
 * A network client. Used to Actively connect to an server.<br>
 * A Client can only be connected to ONE server<br>
 * With the Client you can send all {@link Serializable} datas to the server
 *
 * @author Niton
 * @version 2018-04-11
 */
public class NetworkClient {
	/**
	 * This is the Ping returned if a Ping times out
	 */
	public static final int MAX_PING = -1;

	private Socket socket = new Socket();
	private String ip;
	private String tolken = "NONE";
	private PublicKey rsaKey;
	private SecretKey aesKey;
	private int port;
	private ClientSenderThread sender = new ClientSenderThread(this);
	private ClientListenerThread inputListener = new ClientListenerThread(this);
	private boolean connected = true;
	private ArrayList<Package<? extends Serializable>> querry = new ArrayList<>();
	private int lastPing = MAX_PING;
	private ArrayList<NetworkListener> listeners = new ArrayList<>();
	private Runnable onDissconnect = () -> {
	};
	private final boolean log;

	/**
	 * Creates an Network Client with the destination where it should connect
	 * to.<br>
	 * The Constructor does <b>NOT</b> connects the client. For this purpose use
	 * {@link #connect(int)}
	 *
	 * @author Nils Brugger
	 * @version 2018-04-17
	 * @param IP
	 *            the IP the client should connect to later
	 * @param port
	 *            the destination point of the server
	 * @param log
	 *            if true the server will do some sysouts with imformation what he
	 *            is doing if false it will only print errors/exceptiones
	 */
	public NetworkClient(String IP, int port, boolean log) {
		this.log = log;
		setIp(IP);
		setPort(port);
		addListener(new ClientDissconnectListener(this));
	}

	/**
	 * Registers the listener.<br>
	 * Every registered Listener will get called if the client registers a Package
	 * from the Server
	 *
	 * @author Nils Brugger
	 * @version 2018-04-17
	 * @param listener
	 *            the listener to register
	 */
	public void addListener(NetworkListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	/**
	 * This method sends a ping and checks if the connection is ok and that the ping
	 * is lower than the timeout
	 *
	 * @author Nils Brugger
	 * @version 2018-04-17
	 * @param timeout
	 *            the max time the ping should take if the time exceeds the method
	 *            will return false
	 * @return if the connection is intact
	 */
	public boolean checkConnection(int timeout) {
		int i = ping(timeout);
		if (i == MAX_PING)
			connected = false;
		else
			connected = true;
		return connected;
	}

	/**
	 * Starts the 6 Way Handshake.<br>
	 * The call of this method is <i><b>NECCESSARY</b></i> for the client to
	 * work.<br>
	 * the Handshake connection takes max timeout*4 ms.<br>
	 * The Handshake works like this:
	 *
	 * <pre>
	 * Client                    Server
	 *   |      PING REQUEST        |
	 *   |------------------------> |
	 *   |                          |
	 *   |      Ping Response       |
	 *   |<------------------------ |
	 *   |                          |
	 *   |      Token Request       |
	 *   |------------------------> |
	 *   |                          |
	 *   | ClientToken (Response)   |
	 *   |<------------------------ |
	 *   |                          |
	 *   |      RSA Request         |
	 *   |------------------------> |
	 *   |                          |
	 *   | RSA-PublicKey(Response)  |
	 *   |<------------------------ |
	 *   |                          |
	 *   | RSA Encrypted AES Key    |
	 *   |------------------------> |
	 *   |                          |
	 *   | Main Socket Package      |
	 *   |<b>-------------------------</b> |
	 * </pre>
	 *
	 * @author Nils Brugger
	 * @version 2018-04-17
	 * @param timeout
	 *            the max time each request response should take if exceeded the
	 *            Method will throw an {@link SocketException}
	 * @throws UnknownHostException
	 *             on an Name/Host resolving problem
	 * @throws IOException
	 *             on any IO error mainly an {@link SocketException}. caused by may
	 *             Reasons
	 * @throws InvalidKeyException
	 *             an Encryption error
	 * @throws NoSuchAlgorithmException
	 *             an Encryption error
	 * @throws NoSuchPaddingException
	 *             an Encryption error
	 * @throws IllegalBlockSizeException
	 *             an Encryption error
	 * @throws BadPaddingException
	 *             an Encryption error
	 */
	public void connect(int timeout) throws UnknownHostException, IOException, InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		if (!inputListener.isAlive())
			inputListener.start();
		if (!sender.isAlive())
			sender.start();
		if (socket.isConnected())
			throw new SocketException("[Client] We Are allready connected with Ping : " + lastPing);
		else {
			connected = true;
			if (log)
				System.out.println("[Client] Build connection");
			checkConnection(timeout);
			if (!connected)
				throw new SocketException(" Connection Attempt failed!");

			try {
				Request r  = new TokenRequest(this);
				sendPackage(r);
				r.await();
				sendPackage(r = new RSARequest(this));
				r.await();
				setAesKey(SimpleAES.generateKey(128));
				byte[] encryptedAESKey = getAesKey().getEncoded();
				encryptedAESKey = SimpleRSA.encrypt(encryptedAESKey, rsaKey);
				sendPackage(r = new AESKeyPack(encryptedAESKey, getTolken()));
				r.await();
				connectMainSocket();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Description : Connects the main Socket to the Server.<br>
	 * We recommend to NOT call this method as it is automaticly called by
	 * {@link #connect(int)} and causes errors if recalled<br>
	 * The connection isnt encrypted because of the missing 3 Way encryption
	 * Handshake
	 *
	 * @author Nils Brugger
	 * @version 2018-04-17
	 */
	public void connectMainSocket() {
		try {
			socket.connect(new InetSocketAddress(getIp(), getPort()));
			sendPackage(new MainSocketPacket(tolken));
			sendPackage(new ConnectionSuccessPackage(tolken));
		} catch (IOException e) {
			System.out.println("[Client] Main Socket Error");
			e.printStackTrace();
		}
	}

	/**
	 * Description : Ask the server for a peacefully disconnect without errors.<br>
	 * As the server recive the request he should end up the things he is doing with
	 * this client and send a {@link DissconnectResponse} which will call the client
	 * to finaly cut of the connection
	 *
	 * @author Nils Brugger
	 * @version 2018-04-17
	 */
	public synchronized void disconnect() {
		sendPackage(new DissconectRequest(this));
	}

	/**
	 * Description : Returns the address of the Server including port
	 *
	 * @author Nils Brugger
	 * @version 2018-04-17
	 * @return the adress of the server
	 */
	public InetSocketAddress getAdress() {
		return new InetSocketAddress(getIp(), getPort());
	}

	public SecretKey getAesKey() {
		return aesKey;
	}

	public ClientListenerThread getInputListener() {
		return inputListener;
	}

	public String getIp() {
		return ip;
	}

	public int getLastPing() {
		return lastPing;
	}

	public ArrayList<NetworkListener> getListeners() {
		return listeners;
	}

	public InputStream getMainInputStream() throws IOException {
		return socket.getInputStream();
	}
	// public Socket openSubConnection(boolean encrypted) throws
	// UnknownHostException, IOException {
	// Socket s = new Socket(getIp(), getPort());
	// CounterOutputStream cos = new CounterOutputStream(s.getOutputStream());
	// CounterInputStream cis = new CounterInputStream(s.getInputStream());
	// ObjectOutputStream out = new ObjectOutputStream(cos);
	// ObjectInputStream in = new ObjectInputStream(cis);
	// return s;
	// }

	public OutputStream getMainOutputStream() throws IOException {
		return socket.getOutputStream();
	}

	public Runnable getOnDissconnect() {
		return onDissconnect;
	}

	public int getPort() {
		return port;
	}

	public synchronized ArrayList<Package<? extends Serializable>> getQuerry() {
		return querry;
	}

	public PublicKey getRsaKey() {
		return rsaKey;
	}

	/**
	 * @return the listener
	 */
	public ClientSenderThread getSender() {
		return sender;
	}

	public Socket getSocket() {
		return socket;
	}

	public String getTolken() {
		return tolken;
	}

	public boolean isConnected() {
		return connected;
	}

	/**
	 * @return the log
	 */
	public boolean isLog() {
		return log;
	}

	public void listen() {
		sender.start();
		inputListener.start();
	}

	public int ping(int timeout) {
		Object loock = new Object();
		long start = System.currentTimeMillis();
		synchronized (loock) {
			lastPing = MAX_PING;
			sendPackage(new PingRequest(this, loock, start));
			try {
				loock.wait(timeout);
			} catch (InterruptedException e) {
			}
		}
		return lastPing;

	}

	public boolean sendPackage(Package<? extends Serializable> pack) {
		try {
			synchronized (querry) {
				querry.add(pack);
				if (connected) {
					if (log)
						System.out.println("[Client] Added " + pack.getClass().getSimpleName() + " to Querry");
				} else
					throw new java.net.SocketException("Client not connected");
				return true;
			}
		} catch (SocketException e) {
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void sendResponse(Response<? extends Serializable> pack, Socket sock, boolean wait) {
		Object lock = new Object();
		new Thread(new Runnable() {

			@SuppressWarnings("resource")
			@Override
			public void run() {
				try {
					System.out.println("[Client] Try to send Response : " + pack.getClass().getSimpleName());
					OutputStream base;
					base = sock.getOutputStream();

					CounterOutputStream cos = new CounterOutputStream(base);
					ObjectOutputStream packageStream = new ObjectOutputStream(cos);
					Object o;
					if (!(pack instanceof Response<?>))
						packageStream.writeBoolean(pack.isEncrypted());
					if (pack.isEncrypted())
						o = SimpleAES.encryptObject(pack, getAesKey());
					else
						o = pack;
					packageStream.writeObject(o);
				} catch (Exception e) {
					System.out.println("[Client] Sending aborted");
					e.printStackTrace();
				}
				synchronized (lock) {
					lock.notifyAll();
				}
			}
		}).start();
		if (wait)
			synchronized (lock) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
	}

	public void setAesKey(SecretKey aesKey) {
		this.aesKey = aesKey;
	}

	public synchronized void setIp(String ip) {
		this.ip = ip;
	}

	public void setLastPing(int lastPing) {
		this.lastPing = lastPing;
	}

	public void setOnDissconnect(Runnable onDissconnect) {
		this.onDissconnect = onDissconnect;
	}

	public synchronized void setPort(int port) {
		this.port = port;
	}

	public void setRsaKey(PublicKey rsaKey) {
		this.rsaKey = rsaKey;
	}

	public void setTolken(String tolken) {
		this.tolken = tolken;
	}

	/**
	 * Description :
	 *
	 * @author Niton
	 * @version 2018-04-13
	 * @param conection
	 */
	public void shutdown(Socket conection) {
		onDissconnect.run();
		DissconnectResponse response = new DissconnectResponse(null);
		response.setClientTolken(tolken);
		sendResponse(response, conection, false);
		sender.interrupt();
		inputListener.interrupt();
		tolken = null;
		connected = true;
		querry.clear();
	}

	public byte[] toByteArray(int i) {
		byte[] bs = new byte[(i - i % Byte.MAX_VALUE) / Byte.MAX_VALUE + 1];
		for (int j = 0; j < bs.length; j++) {
			byte value = (byte) (i >= Byte.MAX_VALUE ? Byte.MAX_VALUE : i);
			bs[i] = value;
			i -= value;
		}
		return bs;
	}
}
