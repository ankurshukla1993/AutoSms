

package autosms.ankur.com.autosms;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Receipents extends Activity implements ConnectivityReceiver.ConnectivityReceiverListener{
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected)
            Toast.makeText(Receipents.this, "No Internet Connection....", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(Receipents.this, "Internet Connection Back Again....", Toast.LENGTH_LONG).show();
    }

    private Button getPhone ;
    private EditText getPhoneNUmber ;
    private Button setPhoneNumber ;
    private List<String> numberList ;
    private List_Adapter adapter ;
    private ListView list ;
    private Button send ;
    private String message ;
    private String senderId ;
    private String finalStringUrl ;
    private String pattern = "^[A-Za-z0-9. ]+$";

    final int PICK_CONTACT = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_receipents);


        message = getIntent().getExtras().getString("Message");
        senderId = getIntent().getExtras().getString("Senderid");


        message = getGoodMessage(message) ;




        getPhone = (Button)findViewById(R.id.getPhoneBook) ;
        getPhoneNUmber = (EditText)findViewById(R.id.setPhoneNumber) ;
        setPhoneNumber = (Button)findViewById(R.id.setNumber) ;
        list = (ListView)findViewById(R.id.listDisplay) ;
        send = (Button)findViewById(R.id.sendMessage) ;

        numberList = new ArrayList<>() ;
        adapter = new List_Adapter(Receipents.this,numberList) ;
        //numberList.add("Default Number") ;
        list.setAdapter(adapter);



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection()){
                    //Toast.makeText(Receipents.this, "No Internet Connection....", Toast.LENGTH_LONG).show();

                    String number_to_add = "";
                    if (numberList.size() > 0)
                        for (int i = 0; i < numberList.size(); i++) {
                            number_to_add = number_to_add + numberList.get(i) + ",";
                        }

                    if (message.matches(pattern))
                        finalStringUrl = "http://my.b2bsms.co.in/API/WebSMS/Http/v1.0a/index.php?username=petrol&password=petrol&sender=" + senderId + "&to=";
                    else
                        finalStringUrl = "http://my.b2bsms.co.in/API/WebSMS/Http/v1.0a/index.php?username=petrol&password=petrol&sender=" + senderId + "&to=" ;

                    finalStringUrl = finalStringUrl + number_to_add + "&message=" + message + "&reqid=1&format=" +
                            "text&route_id=&sendondate=16-07-2016T11:39:33";

                    if(!message.matches(pattern))
                        finalStringUrl = finalStringUrl + "&msgtype=unicode" ;

                    //Toast.makeText(Receipents.this, finalStringUrl, Toast.LENGTH_SHORT).show();
                    Log.d("Recepients : ", finalStringUrl) ;
                    String resp1 = volleyCall(finalStringUrl);
                    if (resp1 != null) {
                        finish();
                    } else {
                        Toast.makeText(Receipents.this, "Message Sending Failed !!", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(Receipents.this, "No Internet Connection....", Toast.LENGTH_LONG).show();
                }
            }
        });

        getPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                    startActivityForResult(intent, PICK_CONTACT);
                } catch (Exception e) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        });

        setPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_number = getGoodNumber(getPhoneNUmber.getText().toString()) ;
                //Toast.makeText(Receipents.this, "Phone Number = " + phone_number + " and length = " + phone_number.length(), Toast.LENGTH_SHORT).show();
                if (phone_number.length() > 10) {
                    Toast.makeText(Receipents.this, "Number entered is greater that 10 digits", Toast.LENGTH_SHORT).show();
                } else if(phone_number.length() < 10){
                    Toast.makeText(Receipents.this, "Phone number has less that 10 integers and length = " + phone_number.length(), Toast.LENGTH_SHORT).show();
                }else if(phone_number.length() == 10){

                    phone_number = "91" + phone_number ;
                    numberList.add(phone_number) ;
                    adapter.notifyDataSetChanged();
                }
                getPhoneNUmber.setText(null);
            }
        });
    }

    private String getGoodMessage(String message) {

        String returnMessage = "" ;
        for(int i = 0; i < message.length(); i++){
            if(Character.isWhitespace(message.charAt(i)))
                returnMessage = returnMessage + "+" ;
            else
                returnMessage = returnMessage + message.charAt(i) ;
        }

        return returnMessage;
    }

    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected)
            Toast.makeText(Receipents.this, "No Internet Connection !!", Toast.LENGTH_SHORT).show();

        return isConnected;
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
                        Toast.makeText(Receipents.this, "Message is delivered", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Receipents.this, "Message Not Sent, Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);

        return ("response");
    }


    public String getGoodNumber(String number){

        String final_number = "" ;
        for(int i = 0; i < number.length(); i++){
            if(Character.isDigit(number.charAt(i))) {
                final_number = final_number + number.charAt(i) ;
            }
        }

        return final_number ;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        try
        {
            if (requestCode == PICK_CONTACT)
            {
                Cursor cursor =  managedQuery(intent.getData(), null, null, null, null);
                cursor.moveToNext();
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String  name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                String phone=cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                String phoneNumber="test";

                if ( phone.equalsIgnoreCase("1"))
                    phone = "true";
                else
                    phone = "false" ;

                if (Boolean.parseBoolean(phone))
                {
                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
                    while (phones.moveToNext())
                    {
                        phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    phones.close();
                }

                //Toast.makeText(this, "You are selected Contact name "+name, Toast.LENGTH_LONG).show();
                //Toast.makeText(Receipents.this, "Phone Number = " + phoneNumber, Toast.LENGTH_SHORT).show();

                if (phoneNumber.length() > 10) {
                    String one = getGoodNumber(phoneNumber) ;
                    //Toast.makeText(Receipents.this, "One string = " + one, Toast.LENGTH_SHORT).show();
                    String p = "91" + one.substring(one.length() - 10).toString();//phoneNumber.substring(phoneNumber.length() - 3).toString();
                    numberList.add(p) ;
                    adapter.notifyDataSetChanged();

                } else if(phoneNumber.length() < 10){
                    Toast.makeText(Receipents.this, "Phone number has less that 10 integers", Toast.LENGTH_SHORT).show();
                }else if(phoneNumber.length() == 10){
                    numberList.add(phoneNumber.toString()) ;
                    adapter.notifyDataSetChanged();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}