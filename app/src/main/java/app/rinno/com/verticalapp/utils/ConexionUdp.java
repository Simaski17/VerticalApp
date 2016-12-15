package app.rinno.com.verticalapp.utils;

import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by Dev21 on 22-11-16.
 */

public abstract class ConexionUdp {

    private int port;
    private String host;
    private String message;
    private boolean isSuccessful;
    private String messagehex;

    public ConexionUdp(int port, String host, String message) {
        this.port = port;
        this.host = host;
        this.message = message;
        this.isSuccessful = false;
        this.messagehex = "";
    }

    public ConexionUdp(int port, String host) {
        this.port = port;
        this.host = host;
        this.message = "Test";
        this.isSuccessful = false;
        this.messagehex = "";
    }

    public ConexionUdp() {
        this.port = 50000;
        this.host = "127.0.0.1";
        this.message = "Test";
        this.isSuccessful = false;
        this.messagehex = "";
    }

    public void send(){
        send(this.message);
    }

    /**
     * Send message UDP
     * @param message
     */
    public void send(String message){

        int server_port = this.port;
        DatagramSocket s = null;
        try {
            s = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        InetAddress local = null;
        try {
            local = InetAddress.getByName(this.host);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        byte[] messageByte = strHexToByte(message);
        DatagramPacket p = new DatagramPacket(messageByte, messageByte.length,local,server_port);

        try {
            s.send(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * String to format
     * @param s
     * @return
     */
    public byte[] strHexToByte(String s) {
        this.messagehex = String.format("%010x", new BigInteger(1, s.getBytes(/*YOUR_CHARSET?*/)));
        int len = this.messagehex.length();
        int indice = 0;
        byte[] data = new byte[(len / 2) + 2];
        for (int i = 0; i < len; i += 2) {
            int ini = (Character.digit(this.messagehex.charAt(i), 16) << 4);
            int limit = Character.digit(this.messagehex.charAt(i+1), 16);
            int seg  = ini + limit;
            indice = i / 2;
            data[indice] = (byte) ((Character.digit(this.messagehex.charAt(i), 16) << 4)
                    + Character.digit(this.messagehex.charAt(i+1), 16));
        }
        data[(indice + 1)] = (byte) 0x0d;
        data[(indice + 2)] = (byte) 0x0a;
        return data;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getMessagehex() {
        return messagehex;
    }

}
