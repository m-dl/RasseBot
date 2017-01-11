package com.ceri.rassebot.options;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ceri.rassebot.R;
import com.ceri.rassebot.tools.ScreenParam;
import com.ceri.rassebot.tools.Tools;

public class OptionsActivity extends AppCompatActivity {

    public final static String SERVER_FLAG = "server_flag";

    private ScreenParam param;
    private Button valider;
    private EditText ip, port, socketPort, speed, welcome;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
        param = new ScreenParam();
        param.paramWindowFullScreen(getWindow());

        // preferences data
        preferences = getSharedPreferences(Tools.OPTIONS, MODE_PRIVATE);
        editor = preferences.edit();

        ip = (EditText)findViewById(R.id.ip);
        port = (EditText)findViewById(R.id.port);
        socketPort = (EditText)findViewById(R.id.socket_port);
        speed = (EditText)findViewById(R.id.speed);
        welcome = (EditText)findViewById(R.id.welcome_message);

        // set value with preferences data
        ip.setText(preferences.getString(Tools.IP, Tools.DEFAULT_IP));
        port.setText(preferences.getInt(Tools.PORT, Tools.DEFAULT_PORT) + "");
        socketPort.setText(preferences.getInt(Tools.SOCKET_PORT, Tools.DEFAULT_SOCKET_SENDER_PORT) + "");
        speed.setText(preferences.getInt(Tools.SPEED, Tools.DEFAULT_SPEED) + "");
        welcome.setText(preferences.getString(Tools.WELCOME, Tools.DEFAULT_WELCOME));

        // when validate is clicked
        valider = (Button) findViewById(R.id.valider);
        valider.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        // save only if all fields are completed
                        if (!"".equals(ip.getText().toString()) && !"".equals(port.getText().toString()) && !"".equals(socketPort.getText().toString())
                                && !"".equals(speed.getText().toString()) && !"".equals(welcome.getText().toString())) {

                            // if server url has changed, we'll have to reload the socket
                            boolean serverFlag = false;
                            if(!preferences.getString(Tools.IP, Tools.DEFAULT_IP).equals(ip.getText().toString()) ||
                                    preferences.getInt(Tools.PORT, Tools.DEFAULT_PORT) != Integer.parseInt(port.getText().toString()) ||
                                    preferences.getInt(Tools.SOCKET_PORT, Tools.DEFAULT_SOCKET_SENDER_PORT) != Integer.parseInt(socketPort.getText().toString()))
                                serverFlag = true;

                            // check user entry errors
                            if(Integer.parseInt(port.getText().toString()) > 65535)
                                port.setText("65535");
                            if(Integer.parseInt(socketPort.getText().toString()) > 65535)
                                socketPort.setText("65535");
                            if(Integer.parseInt(speed.getText().toString()) < 1)
                                speed.setText("1");
                            if(Integer.parseInt(speed.getText().toString()) > 100)
                                speed.setText("100");

                            // save preferences
                            editor.putString(Tools.IP, ip.getText().toString());
                            editor.putInt(Tools.PORT, Integer.parseInt(port.getText().toString()));
                            editor.putInt(Tools.SOCKET_PORT, Integer.parseInt(socketPort.getText().toString()));
                            editor.putInt(Tools.SPEED, Integer.parseInt(speed.getText().toString()));
                            editor.putString(Tools.WELCOME, welcome.getText().toString());
                            editor.commit();

                            // finish activity
                            Intent intent = new Intent();
                            intent.putExtra(SERVER_FLAG, serverFlag);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                        else
                            Tools.notifBar(view, getString(R.string.erreur_config));
                    }
                }
        );
    }
}