package autosms.ankur.com.autosms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;

import java.util.ArrayList;

/**
 * Created by Ankur on 7/17/2016.
 */
public class Splash extends Activity{

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    private SharedPreference sharedPreference = null ;
    private ArrayList<String> check ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        sharedPreference = new SharedPreference() ;
        check = sharedPreference.getMessages(getApplicationContext()) ;

        WebView wv = (WebView) findViewById(R.id.webview);
        wv.loadUrl("file:///android_res/raw/dataload.gif");



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if(check == null){
                    Intent i = new Intent(Splash.this, MainActivity.class);
                    startActivity(i);
                }else{
                    Intent i = new Intent(Splash.this, SelectMessage.class);
                    startActivity(i);
                }


                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
