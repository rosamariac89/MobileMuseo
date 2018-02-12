package uniba.it.mobilemuseo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Locale;

public class SchedaReperto extends Activity implements View.OnClickListener{


    private final String TAG = SchedaReperto.class.getSimpleName();

    private TextView statusMessage;
    private static final int RC_BARCODE_CAPTURE = 9001;
    private String operaId;

    private String ip= "192.168.1.171";   //indirizzo ip 
    //private String ip="192.168.43.191";
    private TextView descrizShort;
    private TextView titolo;
    private TextView annoPubblicazione;
    private TextView dimensione;
    private TextView periodoStorico;
    private TextView peso;
    private TextView cultura;
    private TextView valore;
    private TextView proprietario;
    private TextView autore;
    private TextView tipo;
    private ImageView immagine;

    private String vsTipologia;
    private String vsArtista;


    private HashMap<String, String> operaDarte;
    private HashMap<String, String> artista;
    private HashMap<String, String> tipologia;

    private TextToSpeech lettoreTesto;
    private Button audioDescrizione;
    private Button stopButton;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheda_reperto);
        Intent intent = getIntent();
        String pkg = getPackageName();

        operaDarte =  new HashMap<>();
        artista =  new HashMap<>();
        tipologia =  new HashMap<>();

        audioDescrizione=(Button)findViewById(R.id.audioDescrizioneButton);
        stopButton=(Button)findViewById(R.id.stopButton);


        statusMessage = (TextView)findViewById(R.id.status_message);
        titolo = (TextView) findViewById(R.id.titolo);
        descrizShort = (TextView) findViewById(R.id.descrizShort);
        annoPubblicazione = (TextView) findViewById(R.id.annoPubblicazione);
        dimensione = (TextView) findViewById(R.id.dimensione);
        periodoStorico = (TextView) findViewById(R.id.periodoStorico);
        peso = (TextView) findViewById(R.id.peso);
        cultura = (TextView) findViewById(R.id.cultura);
        valore = (TextView) findViewById(R.id.valore);
        proprietario = (TextView) findViewById(R.id.proprietario);
        autore = (TextView) findViewById(R.id.autore);
        tipo = (TextView) findViewById(R.id.tipo);

        immagine= (ImageView) findViewById(R.id.immagine);

        operaId = (String)intent.getSerializableExtra(pkg + ".operaId");


        new GetOpera().execute();
        new GetArtista().execute();
        new GetTipo().execute();

        lettoreTesto=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    lettoreTesto.setLanguage(Locale.ITALIAN);
                }
            }
        });

        audioDescrizione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = descrizShort.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                lettoreTesto.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lettoreTesto !=null){
                    lettoreTesto.stop();
                    if(!lettoreTesto.isSpeaking()){
                        Toast.makeText(getApplicationContext(), "Audio Lettore non attivo ! ",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    protected void onActivityResult(int reqCode, int resCode, Intent data) {

        super.onActivityResult(reqCode, resCode, data);

        if (resCode == RESULT_OK) {
            String pkg = getPackageName();
            operaId = (String)data.getSerializableExtra(pkg + ".operaId");

        }


    }

    private class GetOpera extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(MainActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://"+ip+":8000/api/OperaDarte/";
            String jsonStr = sh.makeServiceCall(url);


            //Log.i(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    String length = jsonObj.getString("length");
                    JSONArray opere = jsonObj.getJSONArray("json");
                    for(int i = 0;i<opere.length();i++) {
                        JSONObject c = opere.getJSONObject(i);
                        if(c.getString("id").equals( operaId)){

                            String idOpera = c.getString("id");
                            String annoPubblicazione = c.getString("AnnoPubblicazione");
                            String titolo = c.getString("Titolo");
                            String descrizioneShort = c.getString("DescrizioneShort");
                            String descrizioneEstesa = c.getString("DescrizioneEstesa");
                            String vsMuseo = c.getString("vsMuseo");
                            vsTipologia = c.getString("vsTipologia");  // tipologia get
                            vsArtista = c.getString("vsArtista"); // artista get
                            String dimensione = c.getString("Dimensione");
                            String periodo = c.getString("Periodo");
                            String peso = c.getString("Peso");
                            String cultura = c.getString("Cultura");
                            String valore = c.getString("Valore");
                            String proprietario = c.getString("Proprietario");


                            String immagine=c.getString("Immagine");
                            String audio = c.getString("Audio");
                            String video = c.getString("Video");


                            operaDarte.put("idOperaDarte",idOpera);
                            operaDarte.put("Titolo",titolo);
                            operaDarte.put("DescrizioneShort",descrizioneShort);
                            operaDarte.put("AnnoPubblicazione",annoPubblicazione);
                            operaDarte.put("Dimensione",dimensione);
                            operaDarte.put("Periodo",periodo);
                            operaDarte.put("Peso",peso);
                            operaDarte.put("Cultura",cultura);
                            operaDarte.put("Valore",valore);
                            operaDarte.put("Proprietario",proprietario);
                            operaDarte.put("Immagine",immagine);
                            operaDarte.put("Audio",audio);
                            operaDarte.put("Video",video);


                        }
                    }


                } catch (final JSONException e) {
                    //Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            titolo.setText(operaDarte.get("Titolo"));
            descrizShort.setText(operaDarte.get("DescrizioneShort"));
            annoPubblicazione.setText("Pubblicato nel "+operaDarte.get("AnnoPubblicazione"));
            dimensione.setText("Dimensioni : "+operaDarte.get("Dimensione"));
            periodoStorico.setText("Periodo storico  "+operaDarte.get("Periodo"));
            peso.setText("Peso "+operaDarte.get("Peso"));
            cultura.setText("Cultura  "+operaDarte.get("Cultura"));
            valore.setText("Valore in $ "+operaDarte.get("Valore"));
            proprietario.setText("Proprietario \n"+operaDarte.get("Proprietario"));
            //System.out.println(operaDarte.get("Immagine"));
            Context context = getApplicationContext();
            Glide.with(context)
                    .load("http://192.168.1.171:8088/img/"+operaDarte.get("Immagine"))
                    .into(immagine);

        }
    }

    private class GetArtista extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(MainActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://"+ip+":8000/api/Artista/";
            String jsonStr = sh.makeServiceCall(url);


            //Log.i(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    String length = jsonObj.getString("length");
                    JSONArray artisti = jsonObj.getJSONArray("json");
                    for(int i = 0;i<artisti.length();i++) {
                        JSONObject c = artisti.getJSONObject(i);
                        if(c.getString("id").equals( vsArtista)){

                            String idArtista = c.getString("id");
                            String nome = c.getString("Nome");
                            String cognome = c.getString("Cognome");
                            String nazionalita = c.getString("Nazionalita");


                            artista.put("idArtista",idArtista);
                            artista.put("Nome",nome);
                            artista.put("Cognome",cognome);
                            artista.put("Nazionalita",nazionalita);
                        }
                    }


                } catch (final JSONException e) {
                    //Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            autore.setText(artista.get("Nome")+" "+artista.get("Cognome")+ " \n\n "+artista.get("Nazionalita"));


        }
    }

    private class GetTipo extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(MainActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://"+ip+":8000/api/Tipologia/";
            String jsonStr = sh.makeServiceCall(url);


            //Log.i(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    String length = jsonObj.getString("length");
                    JSONArray tipologie = jsonObj.getJSONArray("json");
                    for(int i = 0;i<tipologie.length();i++) {
                        JSONObject c = tipologie.getJSONObject(i);
                        if(c.getString("id").equals( vsTipologia)){

                            String idTipo = c.getString("id");
                            String tecnica = c.getString("Tecnica");

                            tipologia.put("idTipo",idTipo);
                            tipologia.put("Tecnica",tecnica);

                        }
                    }


                } catch (final JSONException e) {
                    //Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                //Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            tipo.setText("Tecnica  "+tipologia.get("Tecnica"));


        }
    }




    public void contenutiAudio(View button) {
        Intent audio_intent = new Intent(this,AudioPlayer.class);
        String pkg = getPackageName();
        audio_intent.putExtra(pkg+".audio",operaDarte.get("Audio"));
        audio_intent.putExtra(pkg+".operaId",operaId);
        startActivityForResult(audio_intent, 1);

    }
    public void contenutiVideo(View button) {
        Intent video_intent = new Intent(this,VideoPlayer.class);
        String pkg = getPackageName();
        video_intent.putExtra(pkg+".video",operaDarte.get("Video"));
        video_intent.putExtra(pkg+".operaId",operaId);
        startActivityForResult(video_intent, 1);
    }

    public void onClick(View v) {
        Log.e(TAG, "Json parsing error: " );

    }

    public void showToast(String string){
        Context context = getApplicationContext();
        CharSequence text = string;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, -500);
        toast.show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart()");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
        if(lettoreTesto !=null){
            lettoreTesto.stop();
            lettoreTesto.shutdown();
        }
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
        if(lettoreTesto !=null){
            lettoreTesto.stop();
            lettoreTesto.shutdown();
        }
        super.onDestroy();
    }




}
