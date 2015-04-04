package io.ohalloran.crypto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
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
    public static String END_MESSAGE_COSTANT = "#!@";
    public static String START_MESSAGE_COSTANT = "@!#";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.bird2);
        //Log.d("encryption", Cryption.pictureDecode(image));
        String s = "alkheuafuake.kacnac,a";
        //testRSA(s);
        //testEncodeDecode(image,s);
        testAll(image,s);




    }
    private void testRSA(String t){
        PublicKey puk = null;
        PrivateKey prk = null;

        try{
            KeyPair kp = Cryption.getKeyPair(1024);
            puk = kp.getPublic();
            prk = kp.getPrivate();
            byte [] enc = Cryption.RSAEncrypt(t,puk);

            Log.d("byte enc", Base64.encodeToString(enc, Base64.DEFAULT));
            String dec = Cryption.RSADecrypt(enc,prk);
            Log.d("byte dec", dec);



            String encs = Cryption.stringToRSA(t,puk);

            Log.d("enclength", enc.length + "");
            Log.d("encslength", encs.length() + "");

            String s = "";

            for(byte b : enc){
                s +=b + " ";
            }

            Log.d("encbyte", s);

            s = "";

            for(byte b : encs.getBytes()){
                s +=b + " ";
            }

            Log.d("encsbyte", s);


            Log.d("String enc",encs);

            String decs = Cryption.RSAtoString(encs,prk);

            Log.d("String dec", decs);
        }
        catch (Exception e){
            Log.e("Key Pair Gen", "Error in Key Pair generation",e);
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
    }

    private void testAll(Bitmap image, String m){
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
        String cipher = Cryption.stringToRSA(m,puk);

        Bitmap image2 = Cryption.mobiEncode(image,cipher);
        Boolean b = image.sameAs(image2);
        Log.d("encode test", "The images are equal?: " + b);
        ImageView pic1 = (ImageView) findViewById(R.id.pic1);
        ImageView pic2 = (ImageView) findViewById(R.id.pic2);

        String decodeCipher= Cryption.mobiDecode(image2);

        String decodeMessage = Cryption.RSAtoString(decodeCipher, prk);

        Log.d("Encoded Message", m);
        Log.d("Decoded Message", decodeMessage);

        pic1.setImageBitmap(image);
        pic2.setImageBitmap(image2);

    }



}
