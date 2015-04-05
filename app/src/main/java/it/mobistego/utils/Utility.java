package it.mobistego.utils;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import io.ohalloran.crypto.coding.Cryption;


/**
 * Created by paspao on 05/01/15.
 * <p/>
 * <p/>
 * Copyright (C) 2015  Pasquale Paola
 * <p/>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

public class Utility {

    private final static String TAG=Utility.class.getName();
    public static final int SQUARE_BLOCK = 512;

    /**
     * Convert the byte array to an int array.
     *
     * @param b The byte array.
     * @return The int array.
     */

    public static int[] byteArrayToIntArray(byte[] b) {
        Log.v("Size byte array", b.length + "");
        int size = b.length / 3;
        Log.v("Size Int array", size + "");
        System.runFinalization();
        System.gc();
        Log.v("FreeMemory", Runtime.getRuntime().freeMemory() + "");
        int[] result = new int[size];
        int off = 0;
        int index = 0;
        while (off < b.length) {
            result[index++] = byteArrayToInt(b, off);
            off = off + 3;
        }

        return result;
    }

    /**
     * Convert the byte array to an int.
     *
     * @param b The byte array
     * @return The integer
     */
    public static int byteArrayToInt(byte[] b, int offset) {
        int value = 0x00000000;
        for (int i = 0; i < 3; i++) {
            int shift = (3 - 1 - i) * 8;
            value |= (b[i + offset] & 0x000000FF) << shift;
        }
        value = value & 0x00FFFFFF;
        return value;
    }

    /**
     * Convert integer array representing [argb] values to byte array
     * representing [rgb] values
     *
     * @param array Integer array representing [argb] values.
     * @return byte Array representing [rgb] values.
     */

    public static byte[] convertArray(int[] array) {
        byte[] newarray = new byte[array.length * 3];

        for (int i = 0; i < array.length; i++) {

			/*
             * newarray[i * 3] = (byte) ((array[i]) & 0xFF); newarray[i * 3 + 1]
			 * = (byte)((array[i] >> 8)& 0xFF); newarray[i * 3 + 2] =
			 * (byte)((array[i] >> 16)& 0xFF);
			 */

            newarray[i * 3] = (byte) ((array[i] >> 16) & 0xFF);
            newarray[i * 3 + 1] = (byte) ((array[i] >> 8) & 0xFF);
            newarray[i * 3 + 2] = (byte) ((array[i]) & 0xFF);

        }
        return newarray;
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
        //ImageView pic1 = (ImageView) findViewById(R.id.pic1);
        //ImageView pic2 = (ImageView) findViewById(R.id.pic2);

        byte[] encodedBytes= message.getBytes();

        byte[] decBytes= Cryption.mobiDecode(image2).getBytes();
        //pic1.setImageBitmap(image);
        //pic2.setImageBitmap(image2);

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
        //ImageView pic1 = (ImageView) findViewById(R.id.pic1);
        //ImageView pic2 = (ImageView) findViewById(R.id.pic2);

        String decodeCipher= Cryption.mobiDecode(image2);

        String decodeMessage = Cryption.RSAtoString(decodeCipher, prk);

        Log.d("Encoded Message", m);
        Log.d("Decoded Message", decodeMessage);

        //pic1.setImageBitmap(image);
        //pic2.setImageBitmap(image2);

    }




}
