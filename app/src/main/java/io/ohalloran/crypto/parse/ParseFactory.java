package io.ohalloran.crypto.parse;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import io.ohalloran.crypto.data.Message;
import io.ohalloran.crypto.data.Person;

/**
 * Created by Ben on 4/4/2015.
 */
public class ParseFactory {
    static List<Message> messages = new ArrayList<>();
    static List<Person> persons = new ArrayList<>();

    public static void init(Context c) {
        Parse.enableLocalDatastore(c);
        Parse.initialize(c, "ddaRGKSBc1SFY9bkFTfl7W4lAmvdqadz63N3tcHk",
                "aJYPiEZx6mKA7ezzlKQLemgGSYzrlRZS47k5Nwwv");
    }

    public static void refresh(final OnParseUpdateListener listener) {
        final List<Message> localMessages = new ArrayList<>();
        final List<Person> localPersons = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("image");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                for (ParseObject parse : scoreList)
                    localMessages.add(new Message(parse));
                ParseFactory.messages = localMessages;
                if (listener != null)
                    listener.onComplete();
            }
        });

        ParseQuery<ParseObject> q = ParseQuery.getQuery("user");
        q.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                for (ParseObject parse : scoreList)
                    localPersons.add(new Person(parse));
                ParseFactory.persons = localPersons;
                if (listener != null)
                    listener.onComplete();
            }
        });


    }

    public static void postEncodedBytes(Person poster, byte[] img) {
        //TODO implement
    }

    public static Person getPersonWhoPosted(Message m) {
        for (Person p : persons)
            if (p.getID().equals(m.getPosterID()))
                return p;
        return null;
    }

    public static List<Person> getPeople() {
        return persons;
    }

    public static Person getByID(String personId) {
        for (Person p : persons)
            if (p.getID().equals(personId))
                return p;
        Log.wtf("Parse", "Couldn't find person with id " + personId);
        return null;
    }

    public static Person getLocalUser() {
        String userName = getLocalUserName();
        for (Person p : persons)
            if (p.userName().equals(userName))
                return p;
        Log.wtf("Parse", "Couldn't local player with username " +
                userName + " on " + Build.MANUFACTURER);
        return null;
    }

    private static String getLocalUserName() {
        if (Build.MANUFACTURER.equals("HTC"))
            return "Squidward";
        else if (Build.MANUFACTURER.equalsIgnoreCase("Asus"))
            return "Spongebob";
        else
            return "Mr. Krabs";
    }

    public static interface OnParseUpdateListener {
        public void onComplete();
    }

    public static List<Message> getMessages() {
        return messages;
    }
}
