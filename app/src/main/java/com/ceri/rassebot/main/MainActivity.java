package com.ceri.rassebot.main;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.ceri.rassebot.R;
import com.ceri.rassebot.options.OptionsActivity;
import com.ceri.rassebot.socket.Client;
import com.ceri.rassebot.tools.ScreenParam;
import com.ceri.rassebot.tools.Tools;

import java.util.ArrayList;
import java.util.Locale;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity {

    private final static String FORCED_VOICE_LANGUAGE = "fr-FR";
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private final int REQ_CODE_OPTIONS = 200;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public static Activity m_Activity;
    private static MainActivity instance;
    private ScreenParam param;
    public static TextToSpeech textToSpeech;
    private TextView textview;
    private FloatingActionButton options, mic;
    private JoystickView joystickRobot, joystickCamera;
    private WebView stream;
    private int defaultZoomLevel = 100;
    private Client client;

    // Constructor
    public MainActivity() {
        instance = this;
    }

    // Getter
    public static Context getContext() {
        if (instance == null) {
            instance = new MainActivity();
        }
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: faire async task socket
        setContentView(R.layout.activity_main);
        // full screen design
        param = new ScreenParam();
        param.paramWindowFullScreen(getWindow());

        // preferences data
        preferences = getSharedPreferences(Tools.OPTIONS, MODE_PRIVATE);
        editor = preferences.edit();

        m_Activity = MainActivity.this;
        // socket connection
        client = new Client(preferences.getString(Tools.IP, Tools.DEFAULT_IP), preferences.getInt(Tools.SOCKET_PORT, Tools.DEFAULT_SOCKET_PORT));
        // send default robot speed
        //client.sendCommand(Client.SPEED + " " + preferences.getInt(Tools.SPEED, Tools.DEFAULT_SPEED));
        client.sendCommand(Client.SPEED + " " + preferences.getInt(Tools.SPEED, Tools.DEFAULT_SPEED));

        textview = (TextView) findViewById(R.id.textview);

        // webview to show robot camera stream
        stream = (WebView) findViewById(R.id.stream);
        stream.setInitialScale(defaultZoomLevel);
        stream.post(new Runnable()
        {
            @Override
            public void run() {
                int width = stream.getWidth()-5; // html margin glitch
                int height = stream.getHeight()-20; // html margin glitch
                stream.loadUrl(Tools.HTTP + preferences.getString(Tools.IP, Tools.DEFAULT_IP) +
                        Tools.PORT_SEPARATOR + preferences.getInt(Tools.PORT, Tools.DEFAULT_PORT) +
                        Tools.STREAM_HTTP + Tools.WIDTH_HTTP + width + Tools.HEIGHT_HTTP + height);
            }
        });

        // control system with user voice
        mic = (FloatingActionButton) findViewById(R.id.mic);
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });

        // options activity
        options = (FloatingActionButton) findViewById(R.id.options);
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(m_Activity, OptionsActivity.class);
                startActivityForResult(intent, REQ_CODE_OPTIONS);
            }
        });

        // control robot
        joystickRobot = (JoystickView) findViewById(R.id.joystick_robot);
        joystickRobot.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                textview.setText("Robot: " + String.valueOf(angle) + "° et " + String.valueOf(strength) + "%");
                //String command = Tools.angleToDirection(angle);
                //if(command != null)
                    client.sendCommand(Client.ROBOT + " " + angle + " " + strength);
            }
        });

        // control camera
        joystickCamera = (JoystickView) findViewById(R.id.joystick_camera);
        joystickCamera.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                textview.setText("Caméra: " + String.valueOf(angle) + "° et " + String.valueOf(strength) + "%");
//                String command = Tools.angleToBidirection(angle);
//                if(command != null)
                    client.sendCommand(Client.CAMERA + " " + angle + " " + strength);
            }
        });

        // vocal application
        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.FRANCE);
                    textToSpeech.speak(preferences.getString(Tools.WELCOME, Tools.DEFAULT_WELCOME), TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        });
    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, FORCED_VOICE_LANGUAGE);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Tools.notifToast(getResources().getString(R.string.speech_not_supported));
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            // vocal command activity result
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String command = Tools.treatVocalCommand(result.get(0));
                    Tools.notifToast(command);
                    // if no command, tell user, else send it
                    if(command != null)
                        client.sendCommand(command);
                    else
                        textToSpeech.speak(getString(R.string.erreur_commande_inconnue), TextToSpeech.QUEUE_FLUSH, null, null);
                }
                break;
            }
            // options activity result
            case REQ_CODE_OPTIONS: {
                if (resultCode == RESULT_OK && null != data) {
                     if(data.getBooleanExtra(OptionsActivity.SERVER_FLAG, false)) {
                         // creat new socket connection if server has changed
                         //client.stopSocket();
                         client = new Client(preferences.getString(Tools.IP, Tools.DEFAULT_IP), preferences.getInt(Tools.SOCKET_PORT, Tools.DEFAULT_SOCKET_PORT));
                         // reload webview
                         int width = stream.getWidth()-5; // html margin glitch
                         int height = stream.getHeight()-20; // html margin glitch
                         stream.loadUrl(Tools.HTTP + preferences.getString(Tools.IP, Tools.DEFAULT_IP) +
                                 Tools.PORT_SEPARATOR + preferences.getInt(Tools.PORT, Tools.DEFAULT_PORT) +
                                 Tools.STREAM_HTTP + Tools.WIDTH_HTTP + width + Tools.HEIGHT_HTTP + height);
                    }
                }
                break;
            }
        }
    }
}
