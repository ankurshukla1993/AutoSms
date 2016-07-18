

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

public class Receipents extends Activity {

    private Button getPhone ;
    private EditText getPhoneNUmber ;
    private Button setPhoneNumber ;
    private List<String> numberList ;
    private List_Adapter adapter ;
    private ListView list ;
    private Button send ;

    final int PICK_CONTACT = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_receipents);

        final String message = getIntent().getExtras().getString("Message");
        final String senderId = getIntent().getExtras().getString("Senderid");



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
                String finalStringUrl = "" ;
                String number_to_add = "" ;
                if(numberList.size() > 0)
                for(int i = 0; i < numberList.size(); i++){
                    number_to_add = number_to_add + numberList.get(i) + "," ;
                }
                finalStringUrl = "http://my.b2bsms.co.in/API/WebSMS/Http/v1.0a/index.php?username=petrol&password=petrol&sender=NVPETR&to="+number_to_add+"&message="+message+"&reqid=1&format={json|text}&route_id=<route+id>&sendondate=16-07-2016T11:39:33" ;
                Toast.makeText(Receipents.this, finalStringUrl, Toast.LENGTH_SHORT).show();
            }
        });

        getPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                    startActivityForResult(intent, PICK_CONTACT);
                }
                catch (Exception e) {
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
                Toast.makeText(Receipents.this, "Phone Number = " + phone_number + " and length = " + phone_number.length(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(Receipents.this, "One string = " + one, Toast.LENGTH_SHORT).show();
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