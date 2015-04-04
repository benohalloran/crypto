package io.ohalloran.crypto.coding;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

        //import it.mobistego.utils.Util;
        import java.util.Vector;

/**
 *
 * @author Pasquale
 */
public class LS2B {

    private static int[] binary = {16, 8, 0};
    private static byte[] andByte = {(byte) 0xC0, 0x30, 0x0C, 0x03};
    private static int[] toShift = {6, 4, 2, 0};

    public static byte[] codingWith2Bit(int[] oneDPix, int imgCols, int imgRows,
                                        String str) {

        byte[] msg = str.getBytes();
        int channels = 3;
        int shiftIndex = 4;
        byte[] result = new byte[imgRows * imgCols * channels];
        int msgIndex = 0;
        int resultIndex = 0;
        boolean msgEnded = false;
        for (int row = 0; row < imgRows; row++) {

            for (int col = 0; col < imgCols; col++) {
                int element = row * imgCols + col;
                byte tmp = 0;

                for (int channelIndex = 0; channelIndex < channels; channelIndex++) {
                    if (!msgEnded) {
                        tmp = (byte) ((((oneDPix[element] >> binary[channelIndex]) & 0xFF) & 0xFC) | ((msg[msgIndex] >> toShift[(shiftIndex++) % toShift.length]) & 0x3));// 6
                        if (shiftIndex % toShift.length == 0) {
                            msgIndex++;
                        }
                        if (msgIndex == msg.length) {
                            msgEnded = true;
                        }
                    } else {
                        tmp = (byte) ((((oneDPix[element] >> binary[channelIndex]) & 0xFF)));
                    }
                    result[resultIndex++] = tmp;
                }

            }

        }
        // reconvertToThreeDimInsomma(perImg, imgCols, imgRows, false);
        return result;

    }

    public static String reconvertToThreeDimInsomma(byte[] oneDPix, int imgCols,
                                                    int imgRows, boolean processAlpha) {

        Vector li = new Vector();

        String builder = "";
        int shiftIndex = 4;
        byte tmp = 0x00;
        for (int i = 0; i < oneDPix.length; i++) {
            tmp = (byte) (tmp | ((oneDPix[i] << toShift[shiftIndex % toShift.length]) & andByte[shiftIndex++ % toShift.length]));
            if (shiftIndex % toShift.length == 0) {
                li.addElement(new Byte(tmp));
                byte[] nonso = {((Byte) li.elementAt(li.size() - 1)).byteValue()};
                String str = new String(nonso);
                if (str.equals("#")) {
                    break;
                } else {
                    builder = builder + str;
                }

                tmp = 0x00;
            }

        }
        return builder;

    }
}
