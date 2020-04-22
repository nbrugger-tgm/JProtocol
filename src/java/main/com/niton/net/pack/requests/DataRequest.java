package com.niton.net.pack.requests;

import com.niton.net.pack.client.NetworkClient;
import com.niton.net.pack.response.Response;

import java.io.Serializable;

public class DataRequest<T extends Serializable> extends Request {
    private transient Response<T> response;
    private transient boolean seperateSocket = true;
    private String name = "DATA_PCK";
    private final transient Object waiter = new Object();

    /**
     * Initializes a new Package with its token and if it will be encrypted
     *
     * @param token     The token to authenticate the client on the server.<br>
     *                  If this is not set the server will not know who you are.<br>
     *                  Get the HERE: @link
     *                  {@link NetworkClient#getTolken()}
     * @param encrypted
     */
    public DataRequest(String token, boolean encrypted) {
        super(token, encrypted);
    }

    @Override
    public void onResponse(Response<? extends Serializable> answer) {
        try {
            response = (Response<T>) answer;
        }catch (ClassCastException e){
            System.err.println("Data request : Response was of wrong type!"+(response.getData() == null ? "" : response.getData().getClass().getName()));
        }
        synchronized (waiter){
            waiter.notifyAll();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean useSeperateSocket() {
        return seperateSocket;
    }

    public void setSeperateSocket(boolean seperateSocket) {
        this.seperateSocket = seperateSocket;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void awaitResponse(){
        if(response == null){
            synchronized (waiter){
                try {
                    waiter.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Response<T> getResponse() {
        return response;
    }
}
