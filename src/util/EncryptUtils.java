package util;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by SPREADTRUM\jiannan.liu on 17-3-31.
 */
public class EncryptUtils {


    private static EncryptUtils ourInstance = new EncryptUtils();

    private EncryptUtils() {
    }

    public static String getSalt() {
        SecureRandom sr;
        byte[] salt = new byte[16];
        try {
            sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
            sr.nextBytes(salt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toHex(salt);
    }

    public static String getIV() {
        SecureRandom sr;
        byte[] iv = new byte[16];
        try {
            sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
            sr.nextBytes(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toHex(iv);
    }

    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    public static EncryptUtils getInstance() {
        return ourInstance;
    }
}
