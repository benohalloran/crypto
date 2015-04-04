package io.ohalloran.crypto.data;

/**
 * Created by Grace on 4/3/2015.
 */

import com.orm.SugarRecord;

import java.util.Arrays;

public class Message extends SugarRecord<Message> {

    public String messageText;
    public byte[] imageData;
    public String sender;
    public String recip;

    public Message() {
    }

    public Message(String message, String send, String recip) {
        this.messageText = message;
        this.sender = send;
        this.recip = recip;
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
}
