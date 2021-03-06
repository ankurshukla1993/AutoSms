package autosms.ankur.com.autosms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    int num = 0;
    WebView wv;
    EditText tv;
    Button b;
    Button b1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wv = (WebView) findViewById(R.id.webview);
        tv = (EditText) findViewById(R.id.number);
        b = (Button) findViewById(R.id.button);
        b1 = (Button)findViewById(R.id.buttonresend) ;

        WebView wv = (WebView) findViewById(R.id.webview);
        wv.loadUrl("file:///android_res/drawable/hp_logo.gif");

        if (checkConnection())
            preparedata();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Resending OTP", Toast.LENGTH_SHORT).show();
                if (checkConnection())
                preparedata();
            }
        });


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String otp = tv.getText().toString();
                if(!"".equalsIgnoreCase(otp) && otp != null){
                    if(otp.equalsIgnoreCase(String.valueOf(num))){
                        Intent i = new Intent(MainActivity.this, SelectMessage.class);
                        startActivity(i);
                        finish();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "OTP does not match !! :(", Toast.LENGTH_SHORT).show();
                    Log.d("OTP DID NOT MATCH","OTP = "+otp) ;
                }

                /*Intent i = new Intent(MainActivity.this, SelectMessage.class);
                startActivity(i);
                finish();*/
            }
        });
    }

    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected)
            Toast.makeText(MainActivity.this, "No Internet Connection !!", Toast.LENGTH_SHORT).show();

        return isConnected;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected)
            Toast.makeText(MainActivity.this, "No Internet Connection....", Toast.LENGTH_LONG).show();
        //else
        //preparedata() ;
    }

    public void preparedata() {
        Random rand = new Random();
        num = rand.nextInt(9000000) + 1000000;
        String url1 = "http://my.b2bsms.co.in/API/WebSMS/Http/v1.0a/index.php?username=petrol&password=petrol&sender=NVPETR&to=9016925840,9016184470&message=Your+OTP+is+"+String.valueOf(num)+"&reqid=1&format=text&route_id=&sendondate=16-07-2016T11:39:33";
        Log.d("prepareData", "url = " + url1);
        String resp1 = volleyCall(url1);
        Log.d("prepareData", "response = " + resp1);
    }

    public String volleyCall(String url) {
        final List<String> resp = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        resp.add(response);
                        //Toast.makeText(MainActivity.this, "reponse=" + response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "OTP not sent", Toast.LENGTH_SHORT).show();
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);

        return ("response");
    }
}
