package io.ohalloran.crypto;

import com.orm.SugarApp;

import io.ohalloran.crypto.parse.ParseFactory;

/**
 * Created by Ben on 4/4/2015.
 */
public class MyApplication extends SugarApp {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseFactory.init(getApplicationContext());
    }
}
