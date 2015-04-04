package io.ohalloran.crypto.coding;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
/**
 * Created by Ben on 4/3/2015.
 */
public class Cryption {

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
        //System.out.println("EEncrypted?????" + org.apache.commons.codec.binary.Hex.encodeHexString(encryptedBytes));
        return encryptedBytes;
    }

    public static String RSADecrypt(final byte[] encryptedBytes, PrivateKey p) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, p);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        String decrypted = new String(decryptedBytes);
        //System.out.println("DDecrypted?????" + decrypted);
        return decrypted;
    }

    public static byte[] pictureDecode(Bitmap map) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        map.compress(Bitmap.CompressFormat.PNG, 100, stream);

        int pixels[] =new int[map.getHeight() * map.getWidth()];
        map.getPixels(pixels,0,map.getWidth(),0,0,map.getWidth(),map.getHeight());
        byte[] by = new byte[map.getWidth() * map.getHeight()];

        int i =0;
        for(int pixel:pixels){
            //Log.d("pixels",pixel + "");
            int top =       ((pixel&0x03000000)>>6*4)<<6;
            int midtop =    ((pixel&0x00030000)>>4*4)<<4;
            int midbot =    ((pixel&0x00000300)>>2*4)<<2;
            int bot =       ((pixel&0x00000003));
            //Log.d("bytes",(byte) (top + midtop + midbot + bot) + "");
            by[i] = (byte) ( bot+ midbot+ midtop+top);

            i++;
        }

        return by;
    }
    //does not work yet
    public static Bitmap pictureEncode(String msg, Bitmap map) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        map.compress(Bitmap.CompressFormat.PNG, 100, stream);

        int pixels[] =new int[map.getHeight() * map.getWidth()];
        map.getPixels(pixels,0,map.getWidth(),0,0,map.getWidth(),map.getHeight());

        int bitmask = 0xFCFCFCFC;

        //Log.wtf("THINGS", (bitmask == ~3) + "");
        for(int i = 0; i < pixels.length; i++){
            pixels[i] = pixels[i] & bitmask;
        }

        byte bmsg[] = msg.getBytes();
        int i = 0;
        for(byte by :bmsg){
            int top = (by&0xC0)>>6;
            int midtop = (by&0x30)>>4;
            int midbot = (by&0x0C)>>2;
            int bot = by&0x03;
            pixels[i] = pixels[i] ^ (top <<(6*4));
            pixels[i] = pixels[i] ^ (midtop <<(4*4));
            pixels[i] = pixels[i] ^ (midbot <<(2*4));
            pixels[i] = pixels[i] ^ bot;
            i++;
        }
        pixels[i] = pixels[i] | (0x03 <<(6*4));
        pixels[i] = pixels[i] | (0x03 <<(4*4));
        pixels[i] = pixels[i] | (0x03 <<(2*4));
        pixels[i] = pixels[i] | 0x03;

        return Bitmap.createBitmap(pixels,0,map.getWidth(),map.getWidth(),map.getHeight(), Bitmap.Config.ARGB_8888);

    }

    public static Boolean[] getBitArray(byte[] bmsg){
        Boolean bitmsg[] = new Boolean[bmsg.length*8];
        int j = 0;
        for(byte b:bmsg){
            for(int i = 7; i>=0; i--){
                int bm = (0x00000001 << i) & b;
                bitmsg[j] = bm!=0;
                j++;
            }
        }
        return bitmsg;
    }
}