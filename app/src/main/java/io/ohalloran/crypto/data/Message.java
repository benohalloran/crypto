package io.ohalloran.crypto.data;

/**
 * Created by Grace on 4/3/2015.
 */

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.Date;

public class Message implements Comparable<Message> {
    final ParseObject parse;
    byte[] imgData = null;

    public Message(ParseObject data) {
        parse = data;
    }

    public byte[] getImageData() {
        try {
            if (imgData == null)
                return imgData = parse.getParseFile("img_blob").getData();
            else
                return parse.getParseFile("img_blob").getData();
        } catch (ParseException e) {
            Log.e("Message Error", "Error decoding file", e);
        }
        return null;
    }

    public String getPosterID() {
        return parse.getString("poster_id");
    }

    public String getID() {
        return parse.getObjectId();
    }

    public Date getUpdatedAt() {
        return parse.getUpdatedAt();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Message)
            return ((Message) o).getID().equals(getID());
        return false;
    }

    @Override
    public int compareTo(@NonNull Message message) {
        return parse.getUpdatedAt().compareTo(message.parse.getUpdatedAt());
    }
}
