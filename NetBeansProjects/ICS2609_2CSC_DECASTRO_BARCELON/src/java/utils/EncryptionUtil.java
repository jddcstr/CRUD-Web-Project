package utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class EncryptionUtil {

    public static String encrypt(String password, String key, String algorithm) throws Exception {
        String baseAlgorithm = algorithm.contains("/") ? algorithm.split("/")[0] : algorithm;
        
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), baseAlgorithm);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        
        byte[] encryptedBytes = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedPassword, String key, String algorithm) throws Exception {
        String baseAlgorithm = algorithm.contains("/") ? algorithm.split("/")[0] : algorithm;
        
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), baseAlgorithm);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedPassword);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}