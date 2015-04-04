package io.ohalloran.crypto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

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
        String s = "alkheuafuakecbkaecueahdaskjxclasmciwqlnalcnelufvkealjfnkdbkanc eakdnuawednawbdkaebfulaebndfkaew a  dkuhawkduhjaweldfnaelkfhlafd ad adhfwajdaebfmae dawdlawndkwakdlaenfa efaef adbnlawndkaenf,aenflawbdlwadliaenfkbealfnawd  kdfnawdlaenfkaebf   alkdnawkdbawkldf73igr8273y51947198rgqifqkbfn";
        s = s.trim();

        testEncodeDecode(image,s);




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
    private void testEncodeDecode(Bitmap image, String message){
        Bitmap image2 = Cryption.mobiEncode(image,message);
        Boolean b = image.sameAs(image2);
        Log.d("encode test", "The images are equal?: " + b);
        ImageView pic1 = (ImageView) findViewById(R.id.pic1);
        ImageView pic2 = (ImageView) findViewById(R.id.pic2);

        byte[] encodedBytes= message.getBytes();

        byte[] decBytes= Cryption.mobiDecode(image2).getBytes();
        pic1.setImageBitmap(image);
        pic2.setImageBitmap(image2);
        //for(byte by: decodedBytes){
        //    if(by!=0) {
                //Log.d("bytes", by + "");
        //    }
        //}

        //for(byte by : decBytes){
            //Log.wtf("waaat",by+"");
        //}
        try{
            Log.d("EncodedBytes", new String(encodedBytes,"UTF-8"));
            Log.d("DecodedBytes", new String(decBytes,"UTF-8"));
            for(int i = 0; i<encodedBytes.length; i++){
                if(encodedBytes[i] != decBytes[i]){
                    Log.d("Not equal here", i +"");
                    //image.getPixels(pixels,);
                    //Log.d("Pixel Value here", )
                }
            }
        }
        catch (Exception e){
            Log.e("testEncodeDecode", "unsupported standard");
        }


        /*for(int i=0; i<encodedBytes.length;i++){
            Log.d("EncodedBytes",encodedBytes[i] + "");
        }
        for(int i=0; i<decodedBytes.length;i++){
            Log.d("DecodedBytes",decodedBytes[i] + "");
        }*/

    }

    private void testAll(String message){
        String t = "this is a test";
        PublicKey puk = null;
        PrivateKey prk = null;

        try{
            KeyPair kp = Cryption.getKeyPair(1024);
            puk = kp.getPublic();
            prk = kp.getPrivate();
        }
        catch (Exception e){
            Log.e("Key Pair Gen", "Error in Key Pair generation");
        }

    }



}
