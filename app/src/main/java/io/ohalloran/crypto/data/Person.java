package io.ohalloran.crypto.data;

/**
 * Created by Grace on 4/3/2015.
 */
import java.util.*;
public class Person {
    private static final Person ME = new Person("J Smith");
    private static final HashMap<Integer, Person> personMap = new HashMap<>();
    private static int globalID = 0;

    private String name;
    private int personID;

    public Person(String name)    {
        this.name = name;
        personID = globalID++;
        personMap.put(this.personID, this);
    }

    public static Person getPersonFromID(int id){
        return personMap.get(id);
    }
}