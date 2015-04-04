package io.ohalloran.crypto.data;

/**
 * Created by Grace on 4/3/2015.
 */

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import io.ohalloran.crypto.R;

public class Person {
    public static final Person ME = new Person("J Smith");
    private static final HashMap<Integer, Person> personMap = new HashMap<>();
    private static int globalID = 0;

    public final String name;
    public final int personID;

    private Person(String name) {
        this.name = name;
        personID = globalID++;

    }

    public static Person newPerson(String name) {
        Person p = new Person(name);
        personMap.put(p.personID, p);
        return p;
    }

    public Message newMessage(String message, Person other, Date time, boolean sender) {
        Message m;
        if (sender)
            m = new Message(other, this, message, R.mipmap.ic_launcher);
        else
            m = new Message(this, other, message, R.mipmap.ic_launcher);
        int i = 0;
        m.timeSent = time;
        return m;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (personID != person.personID) return false;
        if (name != null ? !name.equals(person.name) : person.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + personID;
        return result;
    }

    public static Collection<Person> people() {
        return personMap.values();
    }

    public static Person getPersonFromID(int id) {
        return personMap.get(id);
    }
}