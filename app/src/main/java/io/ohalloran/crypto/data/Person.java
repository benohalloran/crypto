package io.ohalloran.crypto.data;

import com.parse.ParseObject;

/**
 * Created by Grace on 4/3/2015.
 */

public class Person {
    private ParseObject data;

    public Person(ParseObject p) {
        data = p;
    }

    public String getID() {
        return data.getObjectId();
    }

    public String userName() {
        return data.getString("username");
    }

    public String private_key() {
        return data.getString("private_key");
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Person)
            return ((Person) o).getID().equals(getID());
        return false;
    }

    public String[] friendKeys() {
        return data.getString("friend_keys").split(";");
    }
}