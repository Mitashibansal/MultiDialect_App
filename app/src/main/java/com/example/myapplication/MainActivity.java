package com.example.myapplication;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    int count=0;

    MyClass1 obj2 = new MyClass1();
    String question = "";
    private FirebaseAuth mAuth;
    //private Button btnLogout;
    //ImageView voice_logo;
    EditText editText;
    ImageView imageView;

    public static final Integer RecordAudioRequestCode=1;

    private SpeechRecognizer speechRecognizer;
    AlertDialog.Builder alertSpeechDialog;
    AlertDialog alertDialog;
    Button playBtn;
    MediaPlayer mediaPlayer;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        UI Code
        mAuth=FirebaseAuth.getInstance();

        //sumeeet code
//       // editText=findViewById(R.id.editText);
        imageView=findViewById(R.id.imageView);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)
                !=PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }
        speechRecognizer=SpeechRecognizer.createSpeechRecognizer(this);

        final Intent speechIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizer.startListening(speechIntent);
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.beep);
        mediaPlayer.start();
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                if(count==0){
                    ViewGroup viewGroup=findViewById(android.R.id.content);
                    View dialogView= LayoutInflater.from(MainActivity.this).inflate(R.layout.alertcustom
                            ,viewGroup,false);
                }
                else
                {
                    ViewGroup viewGroup=findViewById(android.R.id.content);
                    View dialogView= LayoutInflater.from(MainActivity.this).inflate(R.layout.alertcustom
                            ,viewGroup,false);

                    alertSpeechDialog=new AlertDialog.Builder(MainActivity.this);
                    alertSpeechDialog.setMessage("Listening...");
                    alertSpeechDialog.setView(dialogView);
                    alertDialog=alertSpeechDialog.create();
                    alertDialog.show();
                }
                count+=1;

            }
            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                imageView.setImageResource(R.drawable.ic_baseline_mic_24);
                ArrayList<String> arrayList=bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//                editText.setText(arrayList.get(0));
//                alertDialog.dismiss();

//                String s= arrayList.get(0);
                question = arrayList.get(0);
                String Answer = "";
                System.out.println(question);
//                alertDialog.dismiss();

                if(question.equalsIgnoreCase("Hello App")){
                    final MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.beep);
                    mediaPlayer.start();
                    mediaPlayer.stop();
                }
                if (count<3) {
                    try {
                        Answer = obj2.main(question);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Answer);
                    playAudio(Answer);
                    speechRecognizer.startListening(speechIntent);
                }

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }

                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    imageView.setImageResource(R.drawable.ic_baseline_mic_24);
                    speechRecognizer.startListening(speechIntent);
                }
                return false;
            }
        });
//        playBtn = findViewById(R.id.idBtnPlay);
//        pauseBtn = findViewById(R.id.idBtnPause);
//        playBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MyClass1 obj1 = new MyClass1();
//                String url = null;
//                try {
//                    url = obj1.main();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                playAudio(url);
//            }
//        });

    }
    // UI Code
    private void checkPermission(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    @Override
    protected  void onDestroy(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onDestroy();
        speechRecognizer.destroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==RecordAudioRequestCode && grantResults.length>0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add("Login");
        menu.add("Register");
        menu.add("Logout");

        return super.onCreateOptionsMenu(menu);
    }

    @Override

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals("Login")){
            Toast.makeText(this, "LOGIN SELECTED", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();

        }
        else if (item.getTitle().equals("Register")){
            Toast.makeText(this, "REGISTER SELECTED", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            finish();
        }
        else if (item.getTitle().equals("Logout")){
            Toast.makeText(this, "LOGOUT SELECTED", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    //out of oncreate
   // @Override
//    public void onStart(){
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//        super.onStart();
//        FirebaseUser currentUser= mAuth.getCurrentUser();
//        if (currentUser==null){
//            startActivity(new Intent(MainActivity.this, LoginActivity.class));
//        }
//
//    }

    public void logout(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        FirebaseAuth.getInstance().signOut();

        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }



    public void playAudio(String audioUrl) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
//        String audioUrl = "https://firebasestorage.googleapis.com/v0/b/rapid-tts.appspot.com/o/audio-bb2db3faaa1baed57b672b6f21ec3237.mp3?alt=media";
        System.out.println(audioUrl);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Audio started playing..", Toast.LENGTH_SHORT).show();

    }

}
class MyClass1{

//    public String Trans(String t) throws JSONException, IOException, InterruptedException
//    {
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//        OkHttpClient client = new OkHttpClient();
//        MediaType mediaType = MediaType.parse("application/json");
//        String value = "{\r\n   \"q\": \""+t+"\",\r \"target\": \"fr\",\r \"source\": \"auto\"\r  }";
//        RequestBody body = RequestBody.create(mediaType, value);
//        Request request = new Request.Builder()
//                .url("https://google-translate-plus.p.rapidapi.com/translate")
//                .post(body)
//                .addHeader("content-type", "application/json")
//                .addHeader("X-RapidAPI-Key", "dc61bdd0fdmsha4eedbe548c2a51p19ee3djsn03a9a7e3ddba")
//                .addHeader("X-RapidAPI-Host", "google-translate-plus.p.rapidapi.com")
//                .build();
//
//        try(Response response = client.newCall(request).execute()){
////        System.out.println(response.body().string());
//        JSONObject json= new JSONObject(response.body().string());
////        System.out.println(json4.getJSONObject("data").getJSONObject("translation").get("translatedText"));
//        String d = (String) json.getJSONObject("data").getJSONObject("translation").get("translatedText");
//        return d;}
//    }

//    public String Lang_detect(String t) throws JSONException, IOException, InterruptedException
//    {StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//        OkHttpClient client1 = new OkHttpClient();
//        RequestBody body1 = new FormBody.Builder()
//                .add("q",t)
//                .build();
//        Request request1 = new Request.Builder()
//                .url("https://google-translate1.p.rapidapi.com/language/translate/v2/detect")
//                .post(body1)
//                .addHeader("content-type", "application/x-www-form-urlencoded")
//                .addHeader("Accept-Encoding", "application/gzip")
//                .addHeader("X-RapidAPI-Key", "dc61bdd0fdmsha4eedbe548c2a51p19ee3djsn03a9a7e3ddba")
//                .addHeader("X-RapidAPI-Host", "google-translate1.p.rapidapi.com")
//                .build();
//        try(Response response1 = client1.newCall(request1).execute())
//        {
////        System.out.println(response1.body().string());
//            JSONObject json1 = new JSONObject(response1.body().string());
////        System.out.println(json1.getJSONObject("data").getJSONArray("detections").getJSONArray(0).getJSONObject(0).get("language"));
//
//            String f = (String) json1.getJSONObject("data").getJSONArray("detections").getJSONArray(0).getJSONObject(0).get("language");
//            return f;
//        }
//    }

    public String Chatbot(String d) throws JSONException, IOException, InterruptedException{
        String bid="168698";
        String key="NSWiTvHmJuTAX8y5";
        String res="";
//        String d="maharashtra mein ghooman vaaste kaun kaun si jagah hoovat rahe";
        byte arr[] = d.getBytes("UTF8");
        for(int i=0;i<d.length();i++)
        {
            char ch=d.charAt(i);
            if(Character.isLetterOrDigit(ch))
                res+=ch;
            else
            {
                res=res+"%"+(arr[i]-12);
            }
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://api.brainshop.ai/get?bid="+bid+"&key="+key+"&uid=mashape&msg="+res)
                .get()
                .addHeader("X-RapidAPI-Key", "0c602a2540msh9511bece86ee244p17e47ejsn1574d51b3e94")
                .addHeader("X-RapidAPI-Host", "acobot-brainshop-ai-v1.p.rapidapi.com")
                .build();

        try (Response response2 = client.newCall(request).execute()) {
            JSONObject obj=new JSONObject(response2.body().string());
//            System.out.println(obj.get("cnt"));
            res = (String) obj.get("cnt");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;

//        OkHttpClient client2 = new OkHttpClient();
//        Request request2 = new Request.Builder()
//                .url("https://ai-chatbot.p.rapidapi.com/chat/free?message=\"+res+\"&uid=user1")
//                .get()
//                .addHeader("X-RapidAPI-Key", "0c602a2540msh9511bece86ee244p17e47ejsn1574d51b3e94")
//                .addHeader("X-RapidAPI-Host", "ai-chatbot.p.rapidapi.com")
//                .build();
//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url("http://api.brainshop.ai/get?bid="+bid+"&key="+key+"&uid=mashape&msg="+res)
//                .get()
//                .addHeader("X-RapidAPI-Key", "0c602a2540msh9511bece86ee244p17e47ejsn1574d51b3e94")
//                .addHeader("X-RapidAPI-Host", "acobot-brainshop-ai-v1.p.rapidapi.com")
//                .build();
//
////        try (Response response = client.newCall(request).execute()) {
////            System.out.println(response.body().string());
////        }
//        try(Response response2 = client.newCall(request).execute()) {
////        System.out.println(response2.body().string());
//            JSONObject json2 = new JSONObject(response2.body().string());
////        System.out.println(json2.getJSONObject("chatbot").get("response"));
//            String e = (String) json2.get("cnt");
//            return e;
    }

//    public String Trans2(String e,String f) throws JSONException, IOException, InterruptedException{
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//        OkHttpClient client3 = new OkHttpClient();
//        RequestBody body3 = new FormBody.Builder()
//                .add("source_language", "en")
//                .add("target_language",f)
//                .add("text",e)
//                .build();
//        Request request3 = new Request.Builder()
//                .url("https://text-translator2.p.rapidapi.com/translate")
//                .post(body3)
//                .addHeader("content-type", "application/x-www-form-urlencoded")
//                .addHeader("X-RapidAPI-Key", "4849c45e3emsha0d875d38012181p133537jsn8110931ff778")
//                .addHeader("X-RapidAPI-Host", "text-translator2.p.rapidapi.com")
//                .build();
//        try(Response response3 = client3.newCall(request3).execute()){
////        System.out.println(response3.body().string();
//        JSONObject json3= new JSONObject(response3.body().string());
////        System.out.println(json3.getJSONObject("data").get("translatedText"));
//        String s = (String) json3.getJSONObject("data").get("translatedText");
//        return s;}
//    }

    public String TTS(String s,String f) throws JSONException, IOException, InterruptedException{
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        OkHttpClient client4 = new OkHttpClient();
        RequestBody body4 = new FormBody.Builder()
                .add("voice_code", "hi-IN-1")
                .add("text",s)
                .add("speed", "0.75")
                .add("pitch", "1.00")
                .add("output_type", "audio_url")
                .build();
        Request request4 = new Request.Builder()
                .url("https://cloudlabs-text-to-speech.p.rapidapi.com/synthesize")
                .post(body4)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("X-RapidAPI-Key", "e7e544164cmsh7f3903a5681f869p142836jsn4acaeee472b2")
                .addHeader("X-RapidAPI-Host", "cloudlabs-text-to-speech.p.rapidapi.com")
                .build();
        Response response4 = client4.newCall(request4).execute();
//        System.out.println(response.body().string());
        JSONObject json4= new JSONObject(response4.body().string());
//        System.out.println(json4.getJSONObject("result").get("audio_url"));
        String u = (String) json4.getJSONObject("result").get("audio_url");
        return u;
    }

    public String main(String t ) throws IOException, InterruptedException, JSONException {
        System.out.println("Hello, World!");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        MyClass1 obj = new MyClass1();
//        String t = "koi chutkula sunao";
        /* API -0 language translated from eng text to english */
//        String d = obj.Trans(t);
//        /* API -1 : Language detection */
//        String f = obj.Lang_detect(t);
        /* API -2 : Chatbot answering */
        String e = obj.Chatbot(t);
        /* API -3 : Language Translation from eng to detected Language  */
//        String s = obj.Trans2(e,f);
        /* API -4 : Text to Speech (URL)*/
        String u = obj.TTS(e,"hi");
        System.out.println(u);
        /* STEP-5 : URL to Real time audio */
        return u;
//        String url = null;
//        try {
//            url = obj1.main();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        playAudio(u);

    }
}


