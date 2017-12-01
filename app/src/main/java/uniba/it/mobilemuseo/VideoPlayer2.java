package uniba.it.mobilemuseo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by Rosamaria matricola 551125 on 17/11/2017.
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player2);
        Intent intent = getIntent();
        String pkg = getPackageName();
        video = (String)intent.getSerializableExtra(pkg + ".video");
        operaId = (String)intent.getSerializableExtra(pkg + ".operaId");
       // final String  uriPath = "http://"+ip+":8088/video/"+video;
        final String  uriPath = "http://192.168.1.101:8088/video/depechemodesomebody.mp4";

        String fileName = "somebody";
        String filePlace = "android.resource://"+getPackageName()+"/raw/"+fileName;
        videoView=(VideoView)findViewById(R.id.videoView);
        mediaController=(MediaController) new MediaController(this);
       //videoView.setVideoURI(Uri.parse(filePlace));


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Attendi...Caricamento video...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        try {
            if(!videoView.isPlaying()){
            uri = Uri.parse(uriPath);
            //videoView.setVideoURI(uri);
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp = MediaPlayer.create(getApplicationContext(),uri);
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
                mp = MediaPlayer.create(getApplicationContext(),uri);
                mp.setLooping(true);
                videoView.setMediaController(mediaController);
                mediaController.setAnchorView(videoView);
                videoView.start();
               }
        });

    }

    public void ritorna(View button) {
        Intent indietro_intent = new Intent(this,SchedaReperto.class);
        String pkg = getPackageName();
        indietro_intent.putExtra(pkg+".operaId",operaId);
        // startActivityForResult(indietro_intent, 1);
        setResult(RESULT_OK, indietro_intent);
        finish();
    }

    protected void onStart() {
        Log.i(TAG, "onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume()");
        super.onResume();
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
        super.onDestroy();
    }

}
