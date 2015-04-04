package io.ohalloran.crypto.coding;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.Cipher;

import io.ohalloran.crypto.it.mobistego.business.LSB2bit;
import io.ohalloran.crypto.it.mobistego.utils.Constants;
import io.ohalloran.crypto.it.mobistego.utils.Utility;


/**
 * Created by Ben on 4/3/2015.
 */



public class Cryption {

    public static String stringToRSA(String message, PublicKey p){
        try {
            return new String(RSAEncrypt(message, p), "UTF-8");
        }
        catch (Exception e){
            Log.e("RSA", "Failure in string to RSA", e);
        }
        return null;
    }

    public static String RSAtoString(String enc, PrivateKey p){
        try {
            return RSADecrypt(enc.getBytes("UTF-8"), p);
        }
        catch (Exception e){
            Log.e("RSA", "Failure in string to RSA", e);
        }
        return null;
    }

    public static KeyPair getKeyPair(int init)throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        KeyPairGenerator pg = KeyPairGenerator.getInstance("RSA");
        pg.initialize(init);
        return pg.generateKeyPair();
    }
    //http://stackoverflow.com/questions/12471999/rsa-encryption-decryption-in-android
    public static byte[] RSAEncrypt(final String plain, PublicKey p) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, p);
        byte[] encryptedBytes = cipher.doFinal(plain.getBytes());
        return encryptedBytes;
    }

    public static String RSADecrypt(final byte[] encryptedBytes, PrivateKey p) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, p);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        String decrypted = new String(decryptedBytes);
        return decrypted;
    }

    //Implemented using the open source app MobiStego
    //All thanks go to the AMAZING author of this app
    public static String mobiDecode(Bitmap bits){
        List<Bitmap> srcEncodedList = Utility.splitImage(bits);
        String decoded = LSB2bit.decodeMessage(srcEncodedList);
        for(Bitmap bitm:srcEncodedList)
            bitm.recycle();
        //if(!Utility.isEmpty(decoded)) {
            //Log.d("Not good", "No message detected");
        //}

        return decoded.substring(3);
    }

    public static Bitmap mobiEncode(Bitmap bitm, String str){
        {
            //str += END_MESSAGE_COSTANT;
            //str = START_MESSAGE_COSTANT + str;
            int width = bitm.getWidth();
            int height = bitm.getHeight();

            int[] oneD = new int[width * height];
            bitm.getPixels(oneD, 0, width, 0, 0, width, height);
            int density = bitm.getDensity();
            byte[] encodedImage = LSB2bit.encodeMessage(oneD, width, height, str);

            int[] oneDMod = Utility.byteArrayToIntArray(encodedImage);

            Bitmap destBitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);

            destBitmap.setDensity(density);
            int masterIndex = 0;
            for (int j = 0; j < height; j++)
                for (int i = 0; i < width; i++) {
                    destBitmap.setPixel(i, j, Color.argb(0xFF,
                            oneDMod[masterIndex] >> 16 & 0xFF,
                            oneDMod[masterIndex] >> 8 & 0xFF,
                            oneDMod[masterIndex++] & 0xFF));
                }
            return (destBitmap);
        }
    }
}