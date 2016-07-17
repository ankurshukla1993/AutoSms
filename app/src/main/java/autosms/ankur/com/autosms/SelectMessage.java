package autosms.ankur.com.autosms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by arpit on 7/17/2016.
 */
public class SelectMessage extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    ListView list = null;
    Button addNew = null;
    EditText newMessage = null;
    List<String> messageList = null;
    SharedPreference sharedPreference = null;
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
        sharedPreference = new SharedPreference();
        list = (ListView)findViewById(R.id.list);

        messageList = sharedPreference.getMessages(getApplicationContext());
        if(messageList != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, messageList);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle b = new Bundle();
                    b.putString("Message", messageList.get(position));
                    Intent i = new Intent(SelectMessage.this, SelectSenderId.class);
                    i.putExtra("Message", b);
                    startActivity(i);
                }
            });
        }
        addNew = (Button)findViewById(R.id.btnNew);
        newMessage = (EditText)findViewById(R.id.etmessage);


        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = newMessage.getText().toString();
                if(message != null && !"".equalsIgnoreCase(message)){
                    Log.d("ButtonClicked ::", "Message=" + message);
                    sharedPreference.addMessages(getApplicationContext(),message);
                    Bundle b = new Bundle();
                    b.putString("Message", message);
                    Intent i = new Intent(SelectMessage.this, SelectSenderId.class);
                    i.putExtra("Message", b);
                    startActivity(i);
                }
            }
        });
    }
}
