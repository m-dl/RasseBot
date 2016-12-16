package com.ceri.rassebot.main;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.ceri.rassebot.R;
import com.ceri.rassebot.options.OptionsActivity;
import com.ceri.rassebot.tools.ScreenParam;
import com.ceri.rassebot.tools.Tools;

import java.util.ArrayList;
import java.util.Locale;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity {

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public static Activity m_Activity;
    private static MainActivity instance;
    private ScreenParam param;
    TextToSpeech textToSpeech;
    private TextView textview;
    private FloatingActionButton options, mic;
    private JoystickView joystickRobot, joystickCamera;
    private WebView stream;
    private int defaultZoomLevel = 100;

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
        setContentView(R.layout.activity_main);
        param = new ScreenParam();
        param.paramWindowFullScreen(getWindow());

        preferences = getSharedPreferences(Tools.OPTIONS, MODE_PRIVATE);
        editor = preferences.edit();

        m_Activity = MainActivity.this;

        textview = (TextView) findViewById(R.id.textview);
        stream = (WebView) findViewById(R.id.stream);
        stream.setInitialScale(defaultZoomLevel);

        // Get the width and height of the view because its different for different phone or table layouts
        // Pass these values to the URL in the web view to display the HTTP stream
        stream.post(new Runnable()
        {
            @Override
            public void run() {
                int width = stream.getWidth();
                int height = stream.getHeight();
                stream.loadUrl(preferences.getString(Tools.IP, Tools.DEFAULT_IP) + "?width="+width+"&height="+height);
            }
        });

        mic = (FloatingActionButton) findViewById(R.id.mic);
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });

        options = (FloatingActionButton) findViewById(R.id.options);
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(m_Activity, OptionsActivity.class);
                ActivityCompat.startActivity(m_Activity, intent, null);
            }
        });

        joystickRobot = (JoystickView) findViewById(R.id.joystick_robot);
        joystickRobot.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                textview.setText("Robot: " + String.valueOf(angle) + "° et " + String.valueOf(strength) + "%");
            }
        });

        joystickCamera = (JoystickView) findViewById(R.id.joystick_camera);
        joystickCamera.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                textview.setText("Caméra: " + String.valueOf(angle) + "° et " + String.valueOf(strength) + "%");
            }
        });

        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.FRANCE);
                    textToSpeech.speak(getString(R.string.welcome), TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        });
    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
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
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Tools.notifToast(result.get(0));
                }
                break;
            }
        }
    }
}
