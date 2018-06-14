package com.niton.tele;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.niton.tele.network.NetworkListener;
import com.niton.tele.network.client.NetworkClient;
import com.niton.tele.network.requests.Request;
import com.niton.tele.network.server.Server;
import com.niton.tele.network.packs.Package;

public class ServerClientTest {
	public static void main(String[] args) throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, ClassNotFoundException {
		// long x = 1;
		// for(int i = new Scanner(System.in).nextInt(); i > 0 ;i --)
		// x *= i;
		// System.out.println(x);

		Rectangle text = new Rectangle(100, 100, 100, 100);
		Server s = new Server(5, 4567);
		s.start();
		NetworkClient c = new NetworkClient("localhost", 4567);
		c.listen();
		c.connect();
		s.addListener(new NetworkListener() {

			@Override
			public void onReciveRequest(Request request, Socket conection) {
			}

			@Override
			public void onRecivePackage(Package<? extends Serializable> pack, Socket conection) {
				System.out.println(pack.getData());
			}

			@Override
			public boolean acceptPackage(Package<? extends Serializable> pack) {
				return true;
			}

			@Override
			public boolean useOwnThread() {
				return true;
			}
		});
		Package<Rectangle> p = new Package<Rectangle>(text, c.getTolken(), true) {
			private static final long serialVersionUID = 757119434192465558L;

			@Override
			public String getName() {
				return "REC";
			}

		};
		for (int i = 0; i < 100; i++) {
			c.sendPackage(p);
		}
	}
}
