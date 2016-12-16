package com.ceri.rassebot.tools;

import android.view.Window;
import android.view.WindowManager;

public class ScreenParam {

    public void paramWindowFullScreen(Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}