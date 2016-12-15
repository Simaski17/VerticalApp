package app.rinno.com.verticalapp.utils;

/**
 * Created by Dev21 on 22-11-16.
 */

public class MultiScaler extends ConexionUdp{

    private int device;
    private int input;
    private int av;
    private String command;

    /**
     *
     * @param av number status audio/video
     * @param device number id device
     * @param input number id input video
     */
    public MultiScaler(int av, int device, int input) {
        this.device = device;
        this.input = input;
        this.av = av;
    }

    /**
     * Constructor two parameters, av default is 0
     * @param device number id device
     * @param input number id input video
     */
    public MultiScaler(int device, int input) {
        this.device = device;
        this.input = input;
        this.av = 0;
        this.command = "";
    }

    /**
     *
     * @param command set command for execute
     */
    public MultiScaler(String command) {
        this.device = 0;
        this.input = 0;
        this.av = 0;
        this.command = command;
    }

    /**
     * Constructor without parameters
     */
    public MultiScaler() {
        this.device = 0;
        this.input = 0;
        this.av = 0;
        this.command = "";
    }

    public void connect(int port, String host){
        super.setPort(port);
        super.setHost(host);
    }

    /**
     * Method route without parameters
     */
    public String sendRoute(){
        return sendRoute(this.device,this.input);
    }

    /**
     * Method two parameters, av default is 0
     * @param device number id device
     * @param input number id input video
     */
    public String sendRoute(int device, int input){
        this.device = device;
        this.input = input;

        String fix = "#ROUTE";
        fix += " " + this.av;
        fix += "," + this.device;
        fix += "," + this.input;
        this.command = fix;
        super.send(this.command);
        return (super.isSuccessful())? "Sending":"NOK";
    }

    /**
     * Method send command without parameters
     */
    public String sendCommand(){
        return sendCommand(this.command);
    }

    /**
     * example route #ROUTE 0,1,2   audiovideo/device/inputvideo
     */
    public String sendCommand(String command){

        this.command = command;
        super.send(this.command);
        return (super.isSuccessful())? "Sending":"NOK";
    }

    public int getDevice() {
        return device;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    public int getInput() {
        return input;
    }

    public void setInput(int input) {
        this.input = input;
    }

    public int getAv() {
        return av;
    }

    public void setAv(int av) {
        this.av = av;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
