

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Receipents extends Activity {
    TextView textDetail;
    Button getPhone ;
    EditText getPhoneNUmber ;
    Button setPhoneNumber ;
    List<String> numberList ;

    final int PICK_CONTACT = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_receipents);

        final String message = getIntent().getExtras().getString("Message");
        final String senderId = getIntent().getExtras().getString("Senderid");


        textDetail = (TextView) findViewById(R.id.textView1);
        getPhone = (Button)findViewById(R.id.getPhoneBook) ;
        getPhoneNUmber = (EditText)findViewById(R.id.setPhoneNumber) ;
        setPhoneNumber = (Button)findViewById(R.id.setNumber) ;

        numberList = new ArrayList<>() ;

        //readContacts();

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
                    /*String p = "91" + phone_number.substring(phone_number.length() - 10).toString();
                    numberList.add(p) ;
                    textDetail.setText(textDetail.getText() + "\n" +p);*/
                    Toast.makeText(Receipents.this, "Number entered is greater that 10 digits", Toast.LENGTH_SHORT).show();
                } else if(phone_number.length() < 10){
                    Toast.makeText(Receipents.this, "Phone number has less that 10 integers and length = " + phone_number.length(), Toast.LENGTH_SHORT).show();
                }else if(phone_number.length() == 10){
                    textDetail.setText(textDetail.getText() + "\n91" + phone_number);
                    phone_number = "91" + phone_number ;
                    numberList.add(phone_number) ;
                }
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
                Toast.makeText(this, "You are selected Contact name "+name, Toast.LENGTH_LONG).show();


                Toast.makeText(Receipents.this, "Phone Number = " + phoneNumber, Toast.LENGTH_SHORT).show();
                if (phoneNumber.length() > 10) {
                    String one = getGoodNumber(phoneNumber) ;
                    Toast.makeText(Receipents.this, "One string = " + one, Toast.LENGTH_SHORT).show();
                    String p = "91" + one.substring(one.length() - 10).toString();//phoneNumber.substring(phoneNumber.length() - 3).toString();
                    numberList.add(p) ;
                    textDetail.setText(textDetail.getText() + "\n" + p + "  and name = " + name);
                } else if(phoneNumber.length() < 10){
                    Toast.makeText(Receipents.this, "Phone number has less that 10 integers", Toast.LENGTH_SHORT).show();
                }else if(phoneNumber.length() == 10){
                    numberList.add(phoneNumber.toString()) ;
                    textDetail.setText(textDetail.getText() + "\n" + phoneNumber + "  and name = " + name);
                }


            }
        }
        catch (Exception e) {
            // TODO: handle exception
        }
    }

    /*public void readContacts() {
        StringBuffer sb = new StringBuffer();
        sb.append("......Contact Details.....");
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        String phone = null;
        String emailContact = null;
        String emailType = null;
        String image_uri = "";
        Bitmap bitmap = null;

        if (cur.getCount() > 0) {
            int i = 0 ;

            //cur.moveToNext()
            while (i < 50 ) {
                cur.moveToNext() ;
                i++ ;
                String id = cur.getString(cur .getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur .getString(cur .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                image_uri = cur .getString(cur .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

                if (Integer .parseInt(cur.getString(cur .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Log.d("Receipents" ,"name = " + name + "ID = "+id);
                    sb.append("\n Contact Name:" + name);
                    Cursor pCur = cr.query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);

                    while (pCur.moveToNext()) {
                        phone = pCur .getString(pCur .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        sb.append("\n Phone number:" + phone);
                        Log.d("Receipents : ","phone = " + phone);
                    }
                    pCur.close();
                    Cursor emailCur = cr.query( ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[] { id }, null);

                    while (emailCur.moveToNext()) {
                        emailContact = emailCur .getString(emailCur .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        emailType = emailCur .getString(emailCur .getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                        sb.append("\nEmail:" + emailContact + "Email type:" + emailType);
                        Log.d("Receipents : ", "Email =" + emailContact + " Email Type : " + emailType);
                    } emailCur.close();
                }

                if (image_uri != null) {
                    System.out.println(Uri.parse(image_uri));
                    try {
                        bitmap = MediaStore.Images.Media .getBitmap(this.getContentResolver(), Uri.parse(image_uri));
                        sb.append("\n Image in Bitmap:" + bitmap); System.out.println(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                sb.append("\n........................................");
            }
            textDetail.setText(sb);
        }
    }*/
}