package io.ohalloran.crypto.data;

/**
 * Created by Grace on 4/3/2015.
 */

import android.support.annotation.NonNull;
import android.util.Log;

import com.orm.SugarRecord;

import java.util.Arrays;
import java.util.Date;

public class Message extends SugarRecord<Message> implements Comparable<Message> {

    public String messageText;
    public byte[] imageData;
    public String sender;
    public String recip;
    public String date;

    public Message() {
    }

    public Message(String message, String send, String recip) {
        this.messageText = message;
        this.sender = send;
        this.recip = recip;
        date = makeDate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (!Arrays.equals(imageData, message.imageData)) return false;
        if (messageText != null ? !messageText.equals(message.messageText) : message.messageText != null)
            return false;
        if (!recip.equals(message.recip)) return false;
        if (!sender.equals(message.sender)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = messageText != null ? messageText.hashCode() : 0;
        result = 31 * result + (imageData != null ? Arrays.hashCode(imageData) : 0);
        result = 31 * result + sender.hashCode();
        result = 31 * result + recip.hashCode();
        return result;
    }

    public String makeDate() {
        Date time = new Date(2015, (int) (Math.random() * 100) % 12, (int) (Math.random() * 100) % 30,
                (int) (Math.random() * 100) % 60, (int) (Math.random() * 100) % 60,
                (int) (Math.random() * 100) % 60);
        return time.toString();
    }

    @Override
    public int compareTo(@NonNull Message message) {
        /*int YEAR = 5;
        int MONTH = 1;
        int DAY = 2;
        int TIME = 3;
        String[] myPieces = date.split(" ");
        String[] otherPieces = message.date.split(" ");*/
        try {
            return new Date(date).compareTo(new Date(message.date));
        } catch (Exception e) {
            Log.e("Message", "Error sorting", e);
            return 0;
        }
    }
}
