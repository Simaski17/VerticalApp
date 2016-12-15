package app.rinno.com.verticalapp;

/**
 * Created by simaski on 13/12/2016.
 */

public class JHPlugins {

    private String server;
    private int port;
    private String message;

    public JHPlugins(String server, int port, String message) {
        this.server = server;
        this.port = port;
        this.message = message;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
