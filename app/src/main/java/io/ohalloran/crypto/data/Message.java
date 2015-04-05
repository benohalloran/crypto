package io.ohalloran.crypto.data;

/**
 * Created by Grace on 4/3/2015.
 */

import android.support.annotation.NonNull;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.Date;

public class Message implements Comparable<Message> {
    final ParseObject parse;
    byte[] imgData = null;

    String posterId, myId = null;
    Date myDate = null;


    public Message(ParseObject data) {
        parse = data;
    }

    public byte[] getImageData() {
        try {
            if (imgData == null)
                return imgData = parse.getParseFile("img_blob").getData();
            else
                return imgData;
        } catch (ParseException e) {
            Log.e("Message Error", "Error decoding file", e);
        }
        return null;
    }

    public String getPosterID() {
        if (posterId == null)
            return posterId = parse.getString("poster_id");
        return posterId;
    }

    public String getID() {
        if (myId == null)
            return myId = parse.getObjectId();
        return myId;
    }

    public Date getUpdatedAt() {
        if (myDate == null)
            return myDate = parse.getUpdatedAt();
        return myDate;
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
