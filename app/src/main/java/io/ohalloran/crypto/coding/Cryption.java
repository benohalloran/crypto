package io.ohalloran.crypto.coding;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;

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

import io.ohalloran.crypto.it.mobistego.beans.MobiStegoItem;
import io.ohalloran.crypto.it.mobistego.business.LSB2bit;
import io.ohalloran.crypto.it.mobistego.utils.Utility;


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

    public static String mobiDecode(Bitmap bits){
        List<Bitmap> srcEncodedList = Utility.splitImage(bits);
        String decoded = LSB2bit.decodeMessage(srcEncodedList);
        for(Bitmap bitm:srcEncodedList)
            bitm.recycle();
        if(!Utility.isEmpty(decoded)) {
            Log.d("Not good", "No message detected");
        }

        return decoded;
    }

    public static Bitmap mobiEncode(Bitmap bitm, String str){
        {
            int width = bitm.getWidth();
            int height = bitm.getHeight();

            byte[] msg = str.getBytes();

            LSB2bit.MessageEncodingStatus message = new LSB2bit.MessageEncodingStatus();
            message.setMessage(str);
            message.setByteArrayMessage(msg);
            message.setCurrentMessageIndex(0);
            message.setMessageEncoded(false);

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
                    // The unique way to write correctly the sourceBitmap, android bug!!!
                    destBitmap.setPixel(i, j, Color.argb(0xFF,
                            oneDMod[masterIndex] >> 16 & 0xFF,
                            oneDMod[masterIndex] >> 8 & 0xFF,
                            oneDMod[masterIndex++] & 0xFF));
                    /*if(masterIndex%partialProgr==0)
                        handler.post(mIncrementProgress);*/
                }
            return (destBitmap);
        }
    }
    public static byte[] pictureDecode(Bitmap map) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        map.compress(Bitmap.CompressFormat.PNG, 100, stream);

        int pixels[] =new int[map.getHeight() * map.getWidth()];

        for(int i = 0; i<pixels.length; i++){
            pixels[i] = 0;
        }

        map.getPixels(pixels,0,map.getWidth(),0,0,map.getWidth(),map.getHeight());
        byte[] by = new byte[map.getWidth() * map.getHeight()];

        for(int i = 0; i<by.length; i++){
            by[i] = 0;
        }

        int i =0;
        for(int pixel:pixels){
            if(i<2) {
                Log.d("pixel before decode", pixel + "");
            }
            int top =       ((pixel&0x03000000)>>>6*4);
            int midtop =    ((0x03<<8*2)&pixel)>>8*2;
            int midbot =    ((pixel&0x00000300)>>>2*4);
            int bot =       ((pixel&0x00000003));
            //Log.d("bytes",(byte) (top + midtop + midbot + bot) + ""); //FFFFFFFFFDBF907A
            by[i] = (byte) ((top<<6) + (midtop<<4) + (midbot<<2) + bot);//FFFFFFFFFDC0907A
            if(i<5) {
                /*
                Log.wtf("top", top + "");
                Log.wtf("midtop", midtop + "");
                Log.wtf("midbot", midbot + "");
                Log.wtf("bot", bot + "");
                Log.wtf("Decode", by[i] + "");
                */
            }
            i++;
        }

        return by;
    }
    //does not work yet
    public static Bitmap pictureEncode(byte[] bytes, Bitmap map) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        map.compress(Bitmap.CompressFormat.PNG, 100, stream);

        int pixels[] =new int[map.getHeight() * map.getWidth()];
        map.getPixels(pixels,0,map.getWidth(),0,0,map.getWidth(),map.getHeight());


        int bitmask =0xFCFCFCFC;


        //Log.wtf("THINGS", (bitmask == ~3) + "");
        for(int i = 0; i < pixels.length; i++){
            if(i<2){
                Log.wtf("pixel before mask", pixels[i] + "");

            }
            //pixels[i] = pixels[i] & bitmask;
            pixels[i] = bitmask & pixels[i];
            if(i<2){
                Log.wtf("pixel after mask", pixels[i] + "");

            }
        }

        byte bmsg[] = bytes;
        int i = 0;
        for(byte by :bmsg){

            int top = (by&0xC0)>>6;
            int midtop = (by&0x30)>>4;
            int midbot = (by&0x0C)>>2;
            int bot = by&0x03;

            /*
            Log.wtf("top",top+"");
            Log.wtf("midtop",midtop+"");
            Log.wtf("midbot",midbot+"");
            Log.wtf("bot",bot+"");
            Log.wtf("toppush",(top <<(6*4))+"");
            Log.wtf("midtoppush",(midtop <<(4*4))+"");
            Log.wtf("midbotpush",(midbot <<(2*4))+"");
            Log.wtf("botpush",(bot)+"");

            Log.wtf("encode",by+"");
            */
            pixels[i] = pixels[i] | (top <<(8*3))|(midtop <<(8*2))|(midbot <<(8*1))|bot;

            /*pixels[i] = pixels[i] | (midtop <<(8*2));
            pixels[i] = pixels[i] | (midbot <<(8*1));
            pixels[i] = pixels[i] | bot;
            */
            if(i<10){
                Log.wtf("pixel after encode", pixels[i] + "");

            }
            i++;
        }
        pixels[i] = pixels[i] | (0x03 <<(6*4));
        pixels[i] = pixels[i] | (0x03 <<(4*4));
        pixels[i] = pixels[i] | (0x03 <<(2*4));
        pixels[i] = pixels[i] | 0x03;


        return Bitmap.createBitmap(pixels,0,map.getWidth(),map.getWidth(),map.getHeight(), Bitmap.Config.ARGB_8888);

    }
    public static byte[] encodeBytePicture(byte[] picture, byte[] message){
        byte[] result  = new byte[picture.length];

        int i = 0;
        for(byte by : message){
            result[i] = (byte) ((((int)picture[i])&0xFC) | ((((int)by)&0xF0)>>4));
            i++;
            result[i] = (byte) ((((int)picture[i])&0xFC) | (((int)by)&0x0F));
            i++;
        }
        return result;
    }

    public static byte[] decodeBytePicture(byte[] picture){
        byte[] result = new byte[picture.length];
        int i = 0;
        for(byte by : picture){
            result[i] = (byte) ((int)by);
            i++;
            result[i] = (byte) ((((int)picture[i])&0xFC) | (((int)by)&0x0F));
            i++;
        }
        return result;
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
//FFBF917A  before mask
//FCBC9078  after mask
//FDBF907A  after encode
//FDC0907A  before decode