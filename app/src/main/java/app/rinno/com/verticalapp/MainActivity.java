package app.rinno.com.verticalapp;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import app.rinno.com.verticalapp.CLS.ClientSocket;
import app.rinno.com.verticalapp.utils.MultiScaler;
import app.rinno.com.verticalapp.utils.ServerSocket;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {

    static int status = 0;
    SurfaceHolder holder;
    private MediaPlayer player;
    private int item = 1;
    public static File carpetaHome = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Source/");
    public static File carpetaVideos = new File(carpetaHome.getAbsoluteFile() + "/videos/");
    public static File carpetaBeacons = new File(carpetaHome.getAbsolutePath() + "/Beacon");
    VideoView surface;

    private int url = 0;
    private ArrayList<String> videoList = new ArrayList<String>();

    ServerSocket server;

    String pathDir;
    String pathFile;
    String pathDirectory;

    ImageView imgs;
    LinearLayout llImage, llVideo, llHidden;
    boolean mRestored = false;

    /*Params Escalador*/
    static String ip = "192.168.1.39";
    static int port = 50000;
    static int group;
    static int screen;
    static int input1 = 1;
    static int input2 = 2;

    private String servidor;
    private int puerto = 9090;
    private String message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        if (!carpetaHome.exists()) {
            carpetaHome.mkdir();
        }
        /*Modo In*/


        /*Fin modo*/

        server = new ServerSocket(this);

        mRestored = savedInstanceState != null;
        llImage = (LinearLayout) findViewById(R.id.layoutImg);
        llVideo = (LinearLayout) findViewById(R.id.layoutVideo);

        imgs = (ImageView) findViewById(R.id.imgs);
        imgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String conf = (server.getIpAddress() + ":" + server.getPort());
                Toast toast = Toast.makeText(MainActivity.this, "Config =  " + conf, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();
                cerrarPanel(0);
            }
        });
        cerrarPanel(0);

        pathDir = "/Source/videos/";
        pathDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();

        //File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Planarsat/Derivacion/");



        pathFile = pathDirectory + pathDir + "lineaentel4k.mp4";
        Log.i("Aqui", "URL: "+pathFile);
        videoList.add(pathFile);
        /*videoList.add(pathX+"/2.mp4");
        videoList.add(pathX+"/4.mp4");
        videoList.add(pathX+"/6.mp4");
        videoList.add(pathX+"/8.mp4");
        videoList.add(pathX+"/9.mp4");
        videoList.add(pathX+"/12.mp4");
        videoList.add(pathX+"/13.mp4");
        videoList.add(pathX+"/14.mp4");
        videoList.add(pathX+"/15.mp4");*/

        //videoList.add(pathX + "/ListaA.mp4");
        /*videoList.add(pathX + "/Propuesta02_rotado.mp4");
        videoList.add(pathX + "/Propuesta03_rotado.mp4");
        videoList.add(pathX + "/Propuesta04_rotado.mp4");
        videoList.add(pathX + "/Propuesta05_rotado.mp4");
        videoList.add(pathX + "/Propuesta06_rotado.mp4");*/



        surface = (VideoView) findViewById(R.id.surface);

        holder = surface.getHolder();
        holder.addCallback(this);

        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        player = new MediaPlayer();
        player.setOnPreparedListener(this);


        //VideoControllerView controller = new VideoControllerView(this);
        //player.setLooping(true);
        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                //Log.d("ERROR", "MALDITASEA [ " + what + "] { " + extra + " }");
                //Intent i = new Intent(Beacons.this, Beacons.class);
                //startActivity(i);
               // finish();
                return false;
            }
        });


        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                url++;
                if (surface != null) {
                    //current = null;
                    //surface.stopPlayback();
                }

                if (url > videoList.size() - 1) {
                    url = 0;
                }

                try {
                    video(videoList.get(url));
                } catch (Exception ex) {

                }
            }
        });

        try {
            video(videoList.get(0));
        } catch (Exception ex) {
            Log.i("No carga video:", ex.toString());
        }

    }

    public boolean makeDirectories(String dirPath)
            throws IOException {
        String[] pathElements = dirPath.split("/");
        if (pathElements != null && pathElements.length > 0) {

            for (String singleDir : pathElements) {

                if (!singleDir.equals("")) {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + dirPath;
                    File dir = new File(path);
                    boolean existed = dir.exists();
                    boolean created = false;
                    if (!existed) {

                        try {
                            created = dir.mkdir();
                        } catch (Exception ex) {

                        }
                        if (created) {
                            Log.i("CREATED directory: ", singleDir);
                        } else {
                            Log.i("COULD NOT directory: ", singleDir);
                            //return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public void setImage(String path) {

        Log.i("Path Server: ", path);
        String[] part = path.split("\\|");

        String message = part[2];

        group = Integer.parseInt( part[0] );
        screen = Integer.parseInt( part[1] );

        String prefix = (group == 2)? "_v":"";

        if(group == 1){

            surface.setVisibility(View.GONE);
            llVideo.setVisibility(View.GONE);
            llHidden.setVisibility(View.GONE);
            sendRoute();
        }

        String ruta = carpetaHome.getAbsolutePath() + "/" + message + prefix + ".png";

        if (message.equals("close")) {
            cerrarPanel(0);
        } else if(message.equals("disconnect")){
            cerrarPanel(0);

        } else if(message.equals("chao")){
            url = 0;
            player.reset();
            player.start();
        }
        else if (message.indexOf("mp4") > -1) {
            cerrarPanel(0);
            try {
                video(ruta);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            try {
                String imgUrl = (path.trim() == "") ? "" : ruta;
                Log.d("imagen:", imgUrl);
                Uri ur = Uri.fromFile(new File(imgUrl));
                imgs.setImageURI(ur);

                if(group==2) {

                    YoYo.with(Techniques.SlideInLeft)
                            .duration(500)
                            .playOn(findViewById(R.id.imgs));
                }

                if(group==1) {

                    YoYo.with(Techniques.SlideInUp)
                            .duration(500)
                            .playOn(findViewById(R.id.imgs));
                }

                cerrarPanel(1);
            } catch (Exception ex) {

            }
        }
    }

    public void sendRoute(){

        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... params) {

                MultiScaler ms = new MultiScaler();
                ms.connect(port,ip);
                ms.sendRoute(screen,input2);
                return null;

            }
        }.execute();
    }

    public void cerrarPanel(int estado) {

        if (estado == 0) {
            imgs.setVisibility(View.GONE);
            if(group == 1) {
                new AsyncTask<Void,Void,Void>(){

                    @Override
                    protected Void doInBackground(Void... params) {

                        MultiScaler ms = new MultiScaler();
                        ms.connect(port,ip);
                        ms.sendRoute(screen,input1);
                        return null;
                    }
                }.execute();
            }

        } else {
            imgs.setVisibility(View.VISIBLE);
        }

        status = estado;
    }

    public void video(String url) throws IOException {
        Log.i("Video: ", url);
        if (player != null) {

            player.stop();
            player.reset();
            /*Toast.makeText(getApplicationContext(), "Sincronizacion Hecha!!!", Toast.LENGTH_SHORT).show();
            EventBus.getDefault().postSticky(new JHPlugins("172.17.177.4",  puerto,"2" + "|" + "1" + "|" + "chao"));
                    EventBus.getDefault().postSticky(new JHPlugins( "172.17.177.5", puerto,"2" + "|" + "1" + "|" + "chao"));
                    EventBus.getDefault().postSticky(new JHPlugins( "172.17.177.6", puerto,"2" + "|" + "1" + "|" + "chao"));
                    EventBus.getDefault().postSticky(new JHPlugins( "172.17.177.7", puerto,"2" + "|" + "1" + "|" + "chao"));
                    EventBus.getDefault().postSticky(new JHPlugins( "172.17.177.8", puerto,"2" + "|" + "1" + "|" + "chao"));
                    EventBus.getDefault().postSticky(new JHPlugins( "172.17.177.9", puerto,"2" + "|" + "1" + "|" + "chao"));*/
            player.setDataSource(url);
            player.prepareAsync();
            //Log.i(  );
            //player.start();

        }
    }


    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        //if (!mRestored) player.seekTo(player.getDuration());
        //player.seekTo(2);
        //Toast.makeText(getApplicationContext(), "INICIA VIDEO", Toast.LENGTH_SHORT).show();
        player.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientSocket myClient = new ClientSocket("172.17.177.4",  puerto,"2" + "|" + "1" + "|" + "chao");
                myClient.execute();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientSocket myClient = new ClientSocket("172.17.177.5",  puerto,"2" + "|" + "1" + "|" + "chao");
                myClient.execute();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientSocket myClient = new ClientSocket("172.17.177.6",  puerto,"2" + "|" + "1" + "|" + "chao");
                myClient.execute();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientSocket myClient = new ClientSocket("172.17.177.7",  puerto,"2" + "|" + "1" + "|" + "chao");
                myClient.execute();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientSocket myClient = new ClientSocket("172.17.177.8",  puerto,"2" + "|" + "1" + "|" + "chao");
                myClient.execute();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ClientSocket myClient = new ClientSocket("172.17.177.9",  puerto,"2" + "|" + "1" + "|" + "chao");
                myClient.execute();
            }
        }).start();

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setDisplay(holder);


    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void JHPlugins(JHPlugins event) {

        servidor = event.getServer();
        puerto = event.getPort();
        message = event.getMessage();

        ClientSocket myClient = new ClientSocket(servidor, puerto, message);
        myClient.execute();
    }


}
