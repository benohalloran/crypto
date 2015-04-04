package io.ohalloran.crypto.data;

/**
 * Created by Grace on 4/3/2015.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Message implements Comparable<Message> {
    public final Person recepient;
    public final Person sender;
    private int resourceId;
    public final String messageText;
    public final int messageId;

    public final static Map<Couple, List<Message>> messages = new HashMap<>();
    private static int globalId = 0;
    public Date timeSent;

    public Message(Person to1, Person from1, String msg, int resourceId1) {
        recepient = to1;
        sender = from1;
        resourceId = resourceId1;
        this.messageText = msg;
        messageId = globalId++;
        assert recepient == Person.ME || sender == Person.ME;

        Couple key = new Couple(recepient, sender);
        List<Message> list = messages.get(key);
        if (list == null)
            messages.put(key, list = new ArrayList<>());

        list.add(this);
    }

    public static List<Message> getMessages(Person id) {
        List<Message> mapped = messages.get(new Couple(id, Person.ME));
        assert mapped != null;
        if (mapped != null)
            Collections.sort(mapped);
        return mapped;
    }

    @Override
    public int compareTo(Message message) {
        return timeSent.compareTo(message.timeSent);
    }


    private static class Couple {
        Person a, b;

        public Couple(Person a, Person b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public int hashCode() {
            return a.hashCode() + b.hashCode();
        }
    }

}
