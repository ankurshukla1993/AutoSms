package autosms.ankur.com.autosms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Ankur on 7/18/2016.
 */
public class List_Adapter extends ArrayAdapter<String> {

    private List<String> numbers ;
    private Context c ;

    public List_Adapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public List_Adapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
        this.c = context ;
        this.numbers = items ;
    }


    public List_Adapter(Context con, List<String> numbers) {
        super(con, 0,numbers);
        this.c = con ;
        this.numbers = numbers;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView ;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(c);
            v = vi.inflate(R.layout.list_elements, null);
        }

        TextView tv = (TextView)v.findViewById(R.id.tv_number) ;
        Button b = (Button)v.findViewById(R.id.button) ;
        tv.setText(numbers.get(position));
        //Toast.makeText(v.getContext(), "Number Added " + numbers.get(position).toString(), Toast.LENGTH_SHORT).show();

        Log.d("ListAdapter element ",numbers.get(position)) ;

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog diaBox = AskOption(position);
                diaBox.show();
            }
        });

        return v;
    }

    private AlertDialog AskOption(final int pos)
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(c)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.delete)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        numbers.remove(pos) ;
                        notifyDataSetChanged();
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
