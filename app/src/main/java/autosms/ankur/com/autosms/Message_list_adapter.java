package autosms.ankur.com.autosms;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ankur on 7/19/2016.
 */
public class Message_list_adapter extends ArrayAdapter<String> {

    private List<String> messages ;
    private Context c ;


    public Message_list_adapter(Context con, List<String> numbers) {
        super(con, 0,numbers);
        this.c = con ;
        this.messages = numbers;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(c);
            v = vi.inflate(R.layout.message_list_elements, null);
        }

        TextView tv = (TextView) v.findViewById(R.id.messageid);
        tv.setText(messages.get(position));
        //Toast.makeText(v.getContext(), "Number Added " + numbers.get(position).toString(), Toast.LENGTH_SHORT).show();

        Log.d("ListAdapter element ", messages.get(position));
        return v ;
    }

}
