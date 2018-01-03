package uniba.it.mobilemuseo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by Cespites RosaMaria matricola 551125  on 17/11/2017.
 */

public class VideoPlayer2 extends AppCompatActivity {

    private static final String TAG =VideoPlayer2.class.getSimpleName();
    private String ip = "192.168.1.101";
    private String video;
    private String operaId;
    private Uri uri;
    private ProgressDialog pDialog;
    private VideoView videoView;
    private MediaController mediaController;
    private File file;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player2);
        Intent intent = getIntent();
        String pkg = getPackageName();
        video = (String)intent.getSerializableExtra(pkg + ".video");
        operaId = (String)intent.getSerializableExtra(pkg + ".operaId");

        new downloadVideo().execute();
        // final String  uriPath = "http://"+ip+":8088/video/"+video;


        //String fileName = "somebody";
        //String filePlace = "android.resource://"+getPackageName()+"/raw/"+fileName;
        videoView=(VideoView)findViewById(R.id.videoView);
        mediaController=(MediaController) new MediaController(this);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Attendi...Caricamento video...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        //uri = Uri.parse(filePlace);
        // uri =Uri.parse("/storage/sdcard0/Movies/"+video);
        //videoView.setVideoURI(uri);
    }

    public void ritorna(View button) {
        Intent indietro_intent = new Intent(this,SchedaReperto.class);
        String pkg = getPackageName();
        indietro_intent.putExtra(pkg+".operaId",operaId);
        // startActivityForResult(indietro_intent, 1);
        setResult(RESULT_OK, indietro_intent);
        finish();
    }

    private class downloadVideo extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {

            int count;
            try {
                File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
                if( !sd.exists() ){
                    sd.mkdir();
                }

                file = new File(sd,video);

                URL url = new URL("http://192.168.1.101:8088/video/"+video);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                // downlod the file
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {}

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(), " Lettore Video pronto ! ",Toast.LENGTH_SHORT).show();
            uri =Uri.parse("/storage/sdcard0/Movies/"+video);
            videoView.setVideoURI(uri);




            try {
                if(!videoView.isPlaying()){
                    //uri = Uri.parse(uriPath);
                    videoView.setVideoURI(uri);
                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {

                        }
                    });// fa 2 chiamate get del video ..perchè ?
                }
                else {
                    videoView.pause();
                }
            }
            catch (Exception e){

            }
            videoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    pDialog.dismiss();
                    videoView.setMediaController(mediaController);
                    mediaController.setAnchorView(videoView);
                    videoView.start();
                }
            });


        }
    }

    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart()");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");

    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause()");
        super.onPause();

    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop()");
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy()");
        if (file.exists()){
            file.delete();
        }
        super.onDestroy();

    }

}
