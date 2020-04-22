package com.niton.net.pack.server.listeners;

import com.niton.net.pack.NetworkListener;
import com.niton.net.pack.packs.ConnectionSuccessPackage;
import com.niton.net.pack.packs.ConnectionUnsuccessPackage;
import com.niton.net.pack.packs.Package;
import com.niton.net.pack.requests.Request;
import com.niton.net.pack.server.Server;
import com.niton.net.pack.server.Session;
import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;

import java.io.Serializable;
import java.net.Socket;

public class ConnectionSuccessDefaultListener implements NetworkListener {
    private Server s;

    public ConnectionSuccessDefaultListener(Server s) {
        this.s = s;
    }

    @Override
    public boolean acceptPackage(Package<? extends Serializable> pack) {
        return pack instanceof ConnectionSuccessPackage;
    }

    @Override
    public void onRecivePackage(Package<? extends Serializable> pack, Socket conection) {
        Session session = s.getSession(pack.getClientTolken());
        if(session.getConnection().isConnected()  && (session.getAesKey() != null)){
            s.sendPacket(new ConnectionSuccessPackage(pack.getClientTolken()),conection);
        }else{
            s.sendPacket(new ConnectionUnsuccessPackage(pack.getClientTolken()), conection);
        }
    }

    @Override
    public void onReciveRequest(Request request, Socket conection) {}
}
