package com.niton.net.pack.response;

import com.niton.net.pack.client.NetworkClient;

import java.io.Serializable;

public class AESKeyReceivedResponse extends Response {
    /**
     * Initializes a new Response with its content (data), its token and if it will
     * be encrypted
     *
     * @param client    The token to authenticate the client on the server.<br>
     *                  If this is not set the server will not know who you are.<br>
     *                  Get the HERE: @link
     *                  {@link NetworkClient#getTolken()}
     */
    public AESKeyReceivedResponse(String client) {
        super(null, client, false);
    }

    @Override
    public String getName() {
        return "AES_SUCCESS";
    }
}
