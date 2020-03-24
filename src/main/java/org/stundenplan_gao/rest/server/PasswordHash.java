package org.stundenplan_gao.rest.server;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class PasswordHash {
    /**
     * Compute a cryptographically secure hash of a password with given salt
     *
     * @param password the password to hash
     * @param salt the salt to add to the password
     * @return
     */
    public static String computeHash(String password, String salt) {
        //Convert the password to a byte array
        byte[] passwordBytes = password.getBytes();
        //Convert the salt to a byte array
        byte[] saltBytes = Base64.getUrlDecoder().decode(salt);
        //Concatenate the two arrays
        byte[] data = Arrays.copyOf(saltBytes, saltBytes.length + passwordBytes.length);
        System.arraycopy(passwordBytes, 0, data, saltBytes.length, passwordBytes.length);
        try {
            //Compute the SHA-512 hash of the byte array
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] result = digest.digest(data);
            //Convert to base64 and return
            return Base64.getUrlEncoder().encodeToString(result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    //Salt has the same length of hash output for maximum security (64 bytes = 512 bits)
    private static final int SALT_LENGTH = 64;
    /**
     * generates a salt used for cyptographic purposes
     *
     * @return a base64 encoded set of 64 random bytes
     */
    public static String generateSalt() {
        //Generate random byte array
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        //Convert to base64 and return
        return bytesToBase64(salt);
    }

    public static byte[] base64ToBytes(String base64) {
        return Base64.getUrlDecoder().decode(base64);
    }

    public static String bytesToBase64(byte[] bytes) {
        return Base64.getUrlEncoder().encodeToString(bytes);
    }
}
