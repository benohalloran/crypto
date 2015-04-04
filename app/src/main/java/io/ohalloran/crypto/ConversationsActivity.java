package io.ohalloran.crypto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import io.ohalloran.crypto.coding.Cryption;

//single conversation
public class ConversationsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.bird2);
        //Log.d("encryption", Cryption.pictureDecode(image));
        Bitmap image2 = Cryption.pictureEncode("",image);
        Boolean b = image.sameAs(image2);
        Log.d("encode test", "The images are equal?: " + b);


    }
    private void testRSA(){
        String t = "this is a test";
        PublicKey puk = null;
        PrivateKey prk = null;

        try{
            KeyPair kp = Cryption.getKeyPair(1024);
            puk = kp.getPublic();
            prk = kp.getPrivate();
            byte [] enc = Cryption.RSAEncrypt(t,puk);
            Log.d("byte enc", new String(enc));
            String dec = Cryption.RSADecrypt(enc,prk);
            Log.d("byte dec", dec);


        }
        catch (Exception e){
            Log.e("Key Pair Gen", "Error in Key Pair generation");
        }
    }



}
