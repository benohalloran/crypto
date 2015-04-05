package io.ohalloran.crypto;

import android.util.Base64;
import android.util.Log;

import com.orm.SugarApp;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import io.ohalloran.crypto.coding.Cryption;
import io.ohalloran.crypto.data.ParseFactory;
import io.ohalloran.crypto.data.Person;

/**
 * Created by Ben on 4/4/2015.
 */
public class MyApplication extends SugarApp implements ParseFactory.OnParseUpdateListener {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseFactory.init(getApplicationContext());

        ParseFactory.refresh(this);




        /*try {
            KeyPair krabs = Cryption.getKeyPair(1024);
            KeyPair sponge = Cryption.getKeyPair(1024);
            KeyPair squid = Cryption.getKeyPair(1024);

            Log.d("key krabs", krabs.getPrivate().toString());

            String kpb = Base64.encodeToString(krabs.getPublic().getEncoded(),Base64.DEFAULT);
            String sppb = Base64.encodeToString(sponge.getPublic().getEncoded(),Base64.DEFAULT);
            String sqpb = Base64.encodeToString(squid.getPublic().getEncoded(),Base64.DEFAULT);
            String kpr = Base64.encodeToString(krabs.getPrivate().getEncoded(),Base64.DEFAULT);
            String sppr = Base64.encodeToString(sponge.getPrivate().getEncoded(),Base64.DEFAULT);
            String sqpr = Base64.encodeToString(squid.getPrivate().getEncoded(),Base64.DEFAULT);



            Log.d("key krabs public", kpb);
            Log.d("key sponge public", sppb);
            Log.d("key squid public", sqpb + "");
            Log.d("key krabs private", kpr);
            Log.d("key sponge private", sppr);
            Log.d("key squid private",sqpr);

        }
        catch (Exception e){
            Log.e("generate keypair","error in gen keypair", e);
        }*/



    }

    public void onComplete() {
        Person krabs = ParseFactory.getLocalUser();

        for(Person person : ParseFactory.getPeople()){

        }

        String krabsPrivateKey = krabs.private_key();
        String krabsPublicKey = krabs.public_key();

        PublicKey pk = Cryption.getPublicKey(krabsPublicKey);
        PrivateKey rk = Cryption.getPrivateKey(krabsPrivateKey);

        String enc = Cryption.stringToRSA("Cant come to work today", pk);
        Log.d("cipher",enc);
        String dec = Cryption.RSAtoString(enc, rk);
        Log.d("dec",dec);
    }

}
