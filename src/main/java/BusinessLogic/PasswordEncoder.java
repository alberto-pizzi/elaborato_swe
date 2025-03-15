package main.java.BusinessLogic;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordEncoder {

    //encode a password with simple SHA-256 algorithm as non-reversible hash
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = digest.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedBytes); // it returns Base64 hash coded. A string with 44 char.
    }

    public static boolean verifyPassword(String notEncodedPassword, String storedHash) throws NoSuchAlgorithmException {
        String hashedPassword = hashPassword(notEncodedPassword);
        return hashedPassword.equals(storedHash); // compare a string with hash
    }

}
