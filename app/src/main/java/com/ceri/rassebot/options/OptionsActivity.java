package com.ceri.rassebot.options;

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

    private ScreenParam param;
    private Button valider;
    private EditText ip, port, speed, welcome;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
        param = new ScreenParam();
        param.paramWindowFullScreen(getWindow());

        preferences = getSharedPreferences(Tools.OPTIONS, MODE_PRIVATE);
        editor = preferences.edit();

        ip = (EditText)findViewById(R.id.ip);
        port = (EditText)findViewById(R.id.port);
        speed = (EditText)findViewById(R.id.speed);
        welcome = (EditText)findViewById(R.id.welcome_message);

        ip.setText(preferences.getString(Tools.IP, Tools.DEFAULT_IP));
        port.setText(preferences.getInt(Tools.PORT, Tools.DEFAULT_PORT) + "");
        speed.setText(preferences.getInt(Tools.SPEED, Tools.DEFAULT_SPEED) + "");
        welcome.setText(preferences.getString(Tools.WELCOME, Tools.DEFAULT_WELCOME));

        valider = (Button) findViewById(R.id.valider);
        valider.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        if (!"".equals(ip.getText().toString()) && !"".equals(port.getText().toString()) && !"".equals(speed.getText().toString())
                                && !"".equals(welcome.getText().toString())) {
                            editor.putString(Tools.IP, ip.getText().toString());
                            editor.putInt(Tools.PORT, Integer.parseInt(port.getText().toString()));
                            editor.putInt(Tools.SPEED, Integer.parseInt(speed.getText().toString()));
                            editor.putString(Tools.WELCOME, welcome.getText().toString());
                            editor.commit();
                            finish();
                        }
                        else
                            Tools.notifBar(view, getString(R.string.erreur_config));
                    }
                }
        );
    }
}