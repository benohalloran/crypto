package io.ohalloran.crypto.data;

/**
 * Created by Grace on 4/3/2015.
 */

import android.support.annotation.NonNull;

import com.parse.ParseObject;

import java.util.Date;

public class Message implements Comparable<Message> {
    final ParseObject parse;
    byte[] imgData = null;

    public Message(ParseObject data) {
        parse = data;
    }

    public byte[] getImageData() {
        if (imgData == null)
            return imgData = parse.getBytes("img_blob");
        return imgData;
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
