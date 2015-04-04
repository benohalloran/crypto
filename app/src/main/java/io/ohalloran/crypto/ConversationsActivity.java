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
        String s = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam lectus justo, maximus quis aliquet eu, tincidunt quis nunc. Fusce convallis tempus purus quis luctus. Nam pulvinar enim lectus, ac accumsan risus euismod eget. Fusce eu risus id sapien lacinia dictum eget ut elit. Pellentesque id dui eu diam laoreet tempor. Quisque at ipsum a quam consectetur iaculis. Vestibulum eget venenatis magna. Nulla facilisi. Cras imperdiet vel augue vitae feugiat. Morbi vehicula sollicitudin risus, ut faucibus erat vehicula at.";
        testEncodeDecode(image,s);




    }
    private void testBitOpp(){
        String s = "abc";
        byte b[] = s.getBytes();
        Boolean a[] =Cryption.getBitArray(b);
        for(Boolean bit: a){
            Log.d("byte",bit + "");
        }
        for(byte by: b){
            Log.d("byte",by +"");
        }
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
        Bitmap image2 = Cryption.pictureEncode(message,image);
        Boolean b = image.sameAs(image2);
        Log.d("encode test", "The images are equal?: " + b);
        //testBitOpp();
        ImageView pic1 = (ImageView) findViewById(R.id.pic1);
        ImageView pic2 = (ImageView) findViewById(R.id.pic2);
        pic1.setImageBitmap(image);
        pic2.setImageBitmap(image2);

        byte[] encodedBytes= message.getBytes();
        byte[] decodedBytes= Cryption.pictureDecode(image2);

        try{
            Log.d("EncodedBytes", new String(encodedBytes,"UTF-8"));
            Log.d("DecodedBytes", new String(decodedBytes,"UTF-8"));
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



}
