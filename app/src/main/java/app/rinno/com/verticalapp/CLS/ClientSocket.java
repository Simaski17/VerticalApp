package app.rinno.com.verticalapp.CLS;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Dev21 on 20-09-16.
 */
public class ClientSocket extends AsyncTask<Void, Void, Void>
{
    public interface OnReadListener
    {
        public void onRead(ClientSocket client, String response);
    }

    String dstAddress;
    int dstPort;
    String response = "";
    OnReadListener listener = null;
    String message;

    public ClientSocket(String addr, int port, String message)
    {
        this.dstAddress = addr;
        this.dstPort = port;
        this.message = message;
    }

    @Override
    protected Void doInBackground(Void... arg0)
    {

        Socket socket = null;

        try
        {
            socket = new Socket(dstAddress, dstPort);

            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            out.writeUTF(message);
            out.flush();

            final byte[] inputaData = new byte[1024];
            in.read(inputaData);

            final Runnable Rhn = new Runnable()
            {
                @Override
                public void run()
                {
                    String inputString = new String(inputaData).trim();
                    Log.d("INFO-TAG", inputString);
                }
            };


        } catch (UnknownHostException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "IOException: " + e.toString();
        } finally
        {
            if (socket != null)
            {
                try
                {
                    socket.close();
                } catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        //textResponse.setText(response);
        super.onPostExecute(result);
    }

    public void setListener(OnReadListener listener)
    {
        this.listener = listener;
    }
}