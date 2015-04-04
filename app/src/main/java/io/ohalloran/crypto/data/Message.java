package io.ohalloran.crypto.data;

/**
 * Created by Grace on 4/3/2015.
 */
import java.sql.Time;
import java.util.*;

public class Message implements  Comparable<Message>{
    private Person to;
    private Person from;
    private int resourceId;
    private int messageId;
    private Date test;

    private static Map<Integer, Message> messages = new HashMap<Integer, Message>();
private static int globalId = 0;

    private Message(Person to1, Person from1, int resourceId1){
        to = to1;
        from = from1;
        resourceId = resourceId1;
        messageId = globalId++;
        messages.put(messageId, this);
    }

   public static Message getMessageFromID(int id){
       return messages.get(id);
   }


    @Override
    public int compareTo(Message message) {
        return test.compareTo(message.test);
    }
}
