package autosms.ankur.com.autosms;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by arpit on 7/17/2016.
 */
public class SelectMessage extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private ListView list = null;
    private Button addNew = null;
    private EditText newMessage = null;
    private List<String> messageList = null;
    private SharedPreference sharedPreference = null;
    private String messageSelected = null ;
    private Spinner spinner = null ;
    private Button go = null ;
    private Message_list_adapter adapter ;
    private TextView display_selected_message = null ;

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
        spinner = (Spinner) findViewById(R.id.senderid_spinner);
        go = (Button)findViewById(R.id.buttonGo) ;
        addNew = (Button)findViewById(R.id.btnNew);
        newMessage = (EditText)findViewById(R.id.etmessage);
        newMessage.setVisibility(View.INVISIBLE);
        display_selected_message = (TextView)findViewById(R.id.displayMessage) ;

        //messageList.add("Default") ;

        messageList = sharedPreference.getMessages(getApplicationContext());
        if(messageList == null)
            messageList = new ArrayList<>() ;

        /*adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, messageList);*/
        adapter = new Message_list_adapter(this, messageList) ;
        list.setAdapter(adapter);

        if(messageList != null) {
            adapter.notifyDataSetChanged();
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                newMessage.setVisibility(View.INVISIBLE);
                messageSelected = messageList.get(position);
                display_selected_message.setText(messageSelected);
                Toast.makeText(SelectMessage.this, "Selected Message :: " + messageSelected, Toast.LENGTH_SHORT).show();
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog diaBox = AskOption(position);
                diaBox.show();
                return false;
            }
        });


        /*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/



        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(this,
                R.array.senderid_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(spinner_adapter);
        spinner.setSelected(false);


        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String items = spinner.getSelectedItem().toString();
                Bundle b = new Bundle();
                b.putString("Message", messageSelected);
                b.putString("Senderid", items);
                Intent i = new Intent(SelectMessage.this, Receipents.class);
                i.putExtras(b);
                startActivity(i);
            }
        });

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMessage.setVisibility(View.VISIBLE);
                String message = newMessage.getText().toString();

                Toast.makeText(SelectMessage.this, "Written Text = " + message, Toast.LENGTH_SHORT).show();

                if(message != null && !"".equalsIgnoreCase(message)){
                    Log.d("ButtonClicked ::", "Message=" + message);
                    sharedPreference.addMessages(getApplicationContext(), message);
                    messageSelected = message ;
                    display_selected_message.setText(messageSelected);
                    messageList.add(message) ;
                    adapter.notifyDataSetChanged();
                    Log.d("ButtonClicked ::", "Message=" + message + " is now add  size = " + messageList.size());
                    newMessage.setText(null);
                    newMessage.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private AlertDialog AskOption(final int pos)
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.delete)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        messageList.remove(pos) ;
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }
}
