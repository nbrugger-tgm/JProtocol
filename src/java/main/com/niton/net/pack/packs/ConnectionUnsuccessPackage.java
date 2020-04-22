package com.niton.net.pack.packs;

import com.niton.net.pack.client.NetworkClient;

import java.io.Serializable;

public class ConnectionUnsuccessPackage extends Package<Serializable> {

    /**
     * Initializes a new Package with its token and if it will be encrypted
     *
     * @param token     The token to authenticate the client on the server.<br>
     *                  If this is not set the server will not know who you are.<br>
     *                  Get the HERE: @link
     *                  {@link NetworkClient#getTolken()}
     * @param encrypted
     */
    public ConnectionUnsuccessPackage(String token) {
        super(null,token, false);
    }

    @Override
    public String getName() {
        return "CON_NO_sSUCCESS";
    }

    @Override
    public boolean useSeperateSocket() {
        return false;
    }
}
