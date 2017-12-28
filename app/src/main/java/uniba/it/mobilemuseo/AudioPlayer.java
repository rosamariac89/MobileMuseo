package uniba.it.mobilemuseo;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import java.io.IOException;


public class AudioPlayer extends Activity {

    private static final String TAG =AudioPlayer.class.getSimpleName();
    private MediaPlayer mp=null;
    String ip = "192.168.43.191";
    private Handler handler = new Handler();
    private double startTime = 0;
    private SeekBar sk=null;
    private String audio;
    private String operaId;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_player);
        Intent intent = getIntent();
        String pkg = getPackageName();

        audio = (String)intent.getSerializableExtra(pkg + ".audio");
        operaId = (String)intent.getSerializableExtra(pkg + ".operaId");

        sk=(SeekBar) findViewById(R.id.bar);

        mp=MediaPlayer.create(this, Uri.parse("http://"+ip+":8088/audio/"+audio) );
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    private Runnable updateBar = new Runnable() {
        public void run()
        {
            try {
                startTime = mp.getCurrentPosition();
                sk.setProgress((int)startTime);
                handler.postDelayed(this, 100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void play(View v)
    {
        try {
            mp.start();
            if(mp.getDuration() != 0){
                sk.setMax((int) mp.getDuration());
            }
            else{
                sk.setMax(181000);
            }
            handler.postDelayed(updateBar,100);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void pause(View v)
    {
        try {
            mp.pause();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void stop(View v)
    {
        try {
            mp.pause();
            mp.seekTo(0);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
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
        mp.release();
        mp = null;
        super.onDestroy();
    }



}
