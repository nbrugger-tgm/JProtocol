package com.niton.net.pack.response;

import com.niton.net.pack.client.NetworkClient;

import java.io.Serializable;

public class DataResponse<T extends Serializable> extends Response<T> {
    /**
     * Initializes a new Response with its content (data), its token and if it will
     * be encrypted
     *
     * @param data      The data to send (data type: T)
     * @param client    The token to authenticate the client on the server.<br>
     *                  If this is not set the server will not know who you are.<br>
     *                  Get the HERE: @link
     *                  {@link NetworkClient#getTolken()}
     */
    public DataResponse(T data, String client) {
        super(data, client, true);
    }

    @Override
    public String getName() {
        return "DATA";
    }
}
