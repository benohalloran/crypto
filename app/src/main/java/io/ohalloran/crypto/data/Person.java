package io.ohalloran.crypto.data;

import com.parse.ParseObject;

/**
 * Created by Grace on 4/3/2015.
 */

public class Person {
    private ParseObject data;

    String id, userName, privKey, pubKey;
    String[] friendKeys = null;
    String[] friends = null;

    public Person(ParseObject p) {
        data = p;
    }

    public String getID() {
        if (id == null)
            return id = data.getObjectId();
        return id;
    }

    public String userName() {
        if (userName == null)
            return userName = data.getString("username");
        return userName;
    }

    public String private_key() {
        if (privKey == null)
            return privKey = data.getString("private_key");
        return privKey;
    }
    public String public_key(){
        if(pubKey == null)
            return pubKey = data.getString("public_key");
        return pubKey;

    }
    public String[] friends(){
        if (friends != null)
            return friends;
        try {
            return friends = data.getString("friends").split(";");
        } catch (NullPointerException e) {
            return friends = new String[0];
        }
    }
    public String[] friendKeys() {
        if (friendKeys != null)
            return friendKeys;
        try {
            return friendKeys = data.getString("friend_keys").split(";");
        } catch (NullPointerException e) {
            return friendKeys = new String[0];
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Person)
            return ((Person) o).getID().equals(getID());
        return false;
    }

    @Override
    public String toString() {
        return userName();
    }


}
