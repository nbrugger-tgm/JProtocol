package com.niton.net.pack.packs;

import com.niton.net.pack.client.NetworkClient;
import com.niton.net.pack.packs.Package;
import com.niton.net.pack.response.Response;

import java.io.Serializable;

public class ConnectionSuccessPackage extends Package<Serializable> {

    /**
     * Initializes a new Package with its token and if it will be encrypted
     *
     * @param token     The token to authenticate the client on the server.<br>
     *                  If this is not set the server will not know who you are.<br>
     *                  Get the HERE: @link
     *                  {@link NetworkClient#getTolken()}
     * @param encrypted
     */
    public ConnectionSuccessPackage(String token) {
        super(null,token, false);
    }

    @Override
    public String getName() {
        return "CON_SUCCESS";
    }

    @Override
    public boolean useSeperateSocket() {
        return false;
    }
}
