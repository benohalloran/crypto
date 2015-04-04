package io.ohalloran.crypto.it.mobistego.business;
/**
 * Copyright (C) 2009  Pasquale Paola

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */


import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import io.ohalloran.crypto.it.mobistego.utils.Utility;

/**
 * Core algorithm of MobiStego.
 *
 * @author <a href="mailto:pasquale.paola@gmail.com">Pasquale Paola</a>
 */

public class LSB2bit {

    private static final String TAG = LSB2bit.class.getName();
    private static int[] binary = {16, 8, 0};
    private static byte[] andByte = {(byte) 0xC0, 0x30, 0x0C, 0x03};
    private static int[] toShift = {6, 4, 2, 0};
    public static String END_MESSAGE_COSTANT = "####@@";
    public static String START_MESSAGE_COSTANT = "@@####";

    /**
     * This method represent the core of LSB on 2 bit (Encoding).
     *
     * @param oneDPix The <b>rgb</b> array.
     * @param imgCols Image width.
     * @param imgRows Image height.
     * @param str Message to encode.
     * @return Encoded message image.
     */
    public static byte[] encodeMessage(int[] oneDPix, int imgCols, int imgRows,
                                       String str) {

        int channels = 3;
        int shiftIndex = 4;
        //Array.newInstance(Byte.class, imgRows * imgCols * channels);
        byte[] result = new byte[imgRows * imgCols * channels];

        str += END_MESSAGE_COSTANT;
        str = START_MESSAGE_COSTANT + str;
        byte[] msg = str.getBytes();

        MessageEncodingStatus message = new MessageEncodingStatus();
        message.setMessage(str);
        message.setByteArrayMessage(msg);
        message.setCurrentMessageIndex(0);
        message.setMessageEncoded(false);


        int resultIndex = 0;

        for (int row = 0; row < imgRows; row++) {
            for (int col = 0; col < imgCols; col++) {
                int element = row * imgCols + col;
                byte tmp = 0;

                for (int channelIndex = 0; channelIndex < channels; channelIndex++) {
                    if (!message.isMessageEncoded()) {
                        tmp = (byte) ((((oneDPix[element] >> binary[channelIndex]) & 0xFF) & 0xFC) | ((message.getByteArrayMessage()[message.getCurrentMessageIndex()] >> toShift[(shiftIndex++)
                                % toShift.length]) & 0x3));// 6
                        if (shiftIndex % toShift.length == 0) {
                            message.incrementMessageIndex();

                        }
                        if (message.getCurrentMessageIndex() == message.getByteArrayMessage().length) {
                            message.setMessageEncoded(true);

                        }
                    } else {
                        tmp = (byte) ((((oneDPix[element] >> binary[channelIndex]) & 0xFF)));
                    }
                    result[resultIndex++] = tmp;

                }

            }

        }
        return result;

    }
    /**
     * This is the decoding method of LSB on 2 bit.
     *
     * @param oneDPix The byte array image.
     * @param imgCols Image width.
     * @param imgRows Image height.
     * @param mesg    The decoded message.
     */
    public static void decodeMessage(byte[] oneDPix, int imgCols,
                                      int imgRows, MessageDecodingStatus mesg) {

        Vector<Byte> v = new Vector<Byte>();


        int shiftIndex = 4;
        byte tmp = 0x00;
        for (int i = 0; i < oneDPix.length; i++) {
            tmp = (byte) (tmp | ((oneDPix[i] << toShift[shiftIndex
                    % toShift.length]) & andByte[shiftIndex++ % toShift.length]));
            if (shiftIndex % toShift.length == 0) {
                v.addElement(new Byte(tmp));
                byte[] nonso = {(v.elementAt(v.size() - 1)).byteValue()};
                String str = new String(nonso);
                // if (END_MESSAGE_COSTANT.equals(str)) {
                if (mesg.getMessage().endsWith(END_MESSAGE_COSTANT)) {
                    Log.i("TEST", "Decoding ended");
                    mesg.setEnded(true);
                    break;
                } else {
                    mesg.setMessage(mesg.getMessage() + str);
                    if (mesg.getMessage().length() == START_MESSAGE_COSTANT.length()
                            && !START_MESSAGE_COSTANT.equals(mesg.getMessage())) {
                        mesg.setMessage(null);
                        mesg.setEnded(true);
                        break;
                    }
                }

                tmp = 0x00;
            }

        }
        if (mesg.getMessage() != null)
            mesg.setMessage(mesg.getMessage().substring(START_MESSAGE_COSTANT.length(), mesg.getMessage()
                    .length()
                    - END_MESSAGE_COSTANT.length()));


    }

    /**
     * Calculate the numbers of pixel needed
     *
     * @param message Message to encode
     * @return The number of pixel
     */
    public static int numberOfPixelForMessage(String message) {
        int result = -1;
        if (message != null) {
            message += END_MESSAGE_COSTANT;
            message = START_MESSAGE_COSTANT + message;
            result = message.getBytes().length * 4 / 3;
        }

        return result;
    }

    public interface ProgressHandler {

        public void setTotal(int tot);

        public void increment(int inc);

        public void finished();
    }

    public static class MessageDecodingStatus {

        private String message;
        private boolean ended;

        public MessageDecodingStatus() {
            message = "";
            ended = false;
        }

        public boolean isEnded() {
            return ended;
        }

        public void setEnded(boolean ended) {
            this.ended = ended;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
//            Log.i("TEST",message);
        }


    }

    public static class MessageEncodingStatus {
        private boolean messageEncoded;
        private int currentMessageIndex;
        private byte[] byteArrayMessage;
        private String message;

        public void incrementMessageIndex() {
            currentMessageIndex++;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isMessageEncoded() {
            return messageEncoded;
        }

        public void setMessageEncoded(boolean messageEncoded) {
            this.messageEncoded = messageEncoded;
        }

        public int getCurrentMessageIndex() {
            return currentMessageIndex;
        }

        public void setCurrentMessageIndex(int currentMessageIndex) {
            this.currentMessageIndex = currentMessageIndex;
        }

        public byte[] getByteArrayMessage() {
            return byteArrayMessage;
        }

        public void setByteArrayMessage(byte[] byteArrayMessage) {
            this.byteArrayMessage = byteArrayMessage;
        }
    }
}
