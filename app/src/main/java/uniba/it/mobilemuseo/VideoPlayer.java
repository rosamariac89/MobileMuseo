package uniba.it.mobilemuseo;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


/**
 * Created by Rosamaria matricola 551125 on 13/11/2017.
 */

public class VideoPlayer extends Activity implements SurfaceHolder.Callback {

    private static final String TAG =VideoPlayer.class.getSimpleName();
    private MediaPlayer mediaPlayer;
    private SurfaceHolder holder;
    private SurfaceView surface;
    //private String ip = "192.168.1.74";
    private String ip = "192.168.1.102";
    private String video;
    private String operaId;
    private Uri uri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player);
        Intent intent = getIntent();
        String pkg = getPackageName();


        video = (String)intent.getSerializableExtra(pkg + ".video");
        operaId = (String)intent.getSerializableExtra(pkg + ".operaId");

        System.out.println(video);

        surface = (SurfaceView) findViewById(R.id.surfView);
        holder = surface.getHolder();
        holder.addCallback(this);


        final String  uriPath = "http://"+ip+":8088/video/"+video;
        uri = Uri.parse(uriPath);


        }


    public void surfaceCreated(SurfaceHolder surfaceHolder) {

             //mediaPlayer= MediaPlayer.create(this, uri);
             mediaPlayer= MediaPlayer.create(this,R.raw.shimbalai);

            mediaPlayer.setDisplay(holder);
            mediaPlayer.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                    }
                }
        );

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

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
        mediaPlayer.release();
        mediaPlayer = null;
    }


}
