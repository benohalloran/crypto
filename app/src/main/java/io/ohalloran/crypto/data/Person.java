package io.ohalloran.crypto.data;

import com.orm.SugarRecord;

/**
 * Created by Grace on 4/3/2015.
 */

public class Person extends SugarRecord<Person> {
    public String name;

    public Person() {
    }

    public Person(String n) {
        this.name = n;
    }

}