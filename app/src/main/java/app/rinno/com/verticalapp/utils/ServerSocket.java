package app.rinno.com.verticalapp.utils;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import app.rinno.com.verticalapp.MainActivity;

/**
 * Created by Dev21 on 21-11-16.
 */

public class ServerSocket {
    java.net.ServerSocket serverSocket;
    String message = "";
    static final int socketServerPORT = 9090;
    MainActivity activity;


    public ServerSocket (MainActivity activity) {
        this.activity = activity;
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();

    }

    public int getPort() {
        return socketServerPORT;
    }

    public void onDestroy() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class SocketServerThread extends Thread {

        int count = 0;

        @Override
        public void run() {
            Socket soc = null;
            try {
                // create ServerSocket using specified port
                try {
                    serverSocket = new java.net.ServerSocket(socketServerPORT);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                while(true) {
                    soc = serverSocket.accept();
                    assert soc != null;
                    DataInputStream in = new DataInputStream(new BufferedInputStream(soc.getInputStream()));
                    final DataOutputStream out = new DataOutputStream(new BufferedOutputStream(soc.getOutputStream()));
                    /*Recepcion de Mensaje*/
                    message = in.readUTF();

                    activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                           activity.setImage(message);
                        }
                    });
                    Log.d("URL:", message);

                    /*Respuesta al Cliente*/
                    String response = "OKI";
                    out.write(response.getBytes());
                    out.flush();
                    out.close();
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    public String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress
                            .nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "Server running at : "
                                + inetAddress.getHostAddress();
                    }
                }
            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }
        return ip;
    }

}
