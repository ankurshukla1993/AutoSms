package autosms.ankur.com.autosms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by arpit on 7/17/2016.
 */
public class SelectMessage extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected)
            Toast.makeText(SelectMessage.this, "No Internet Connection....", Toast.LENGTH_LONG).show();

    }
    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected)
            Toast.makeText(SelectMessage.this, "No Internet Connection !!", Toast.LENGTH_SHORT).show();

        return isConnected;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_message);
    }
}
