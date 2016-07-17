package autosms.ankur.com.autosms;

/**
 * Created by arpit on 7/17/2016.
 */import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.gson.Gson;

public class SharedPreference {

    public static final String PREFS_NAME = "AutoSMS";
    public static final String MESSAGES = "Messages";

    public SharedPreference() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveMessages(Context context, List<String> messages) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(messages);

        editor.putString(MESSAGES, jsonFavorites);

        editor.commit();
    }

    public void addMessages(Context context, String message) {
        List<String> messageList = getMessages(context);
        if (messageList == null)
            messageList = new ArrayList<>();
        messageList.add(message);
        saveMessages(context, messageList);
    }

    public void removeMessage(Context context, String message) {
        ArrayList<String> messageList = getMessages(context);
        if (messageList != null) {
            messageList.remove(message);
            saveMessages(context, messageList);
        }
    }

    public ArrayList<String> getMessages(Context context) {
        SharedPreferences settings;
        List<String> messageList;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(MESSAGES)) {
            String jsonFavorites = settings.getString(MESSAGES, null);
            Gson gson = new Gson();
            String[] favoriteItems = gson.fromJson(jsonFavorites,
                    String[].class);

            messageList = Arrays.asList(favoriteItems);
            messageList = new ArrayList<String>(messageList);
        } else
            return null;

        return (ArrayList<String>) messageList;
    }
}
