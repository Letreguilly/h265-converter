package fr.letreguilly.utils.helper;

public class NumberUtils {

    public static long bytesToLong(byte[] b) {
        long result = 0;
        long factor = 1;

        for (int i = 0; i < b.length; i++) {
            /*result <<= 8;
            result |= (b[i] & 0xFF);*/
            result = result + (b[i] * factor);
            factor *= 256;
        }

        return result;
    }

    public static long stringToLong (String s) {
        long result = 0;
        byte[] stringBytes = s.getBytes();
        result = bytesToLong(stringBytes);

        return result;
    }
}
