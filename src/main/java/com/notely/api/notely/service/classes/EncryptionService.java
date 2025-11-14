package com.notely.api.notely.service.classes;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for encrypting and decrypting user data.
 * Uses AES-256-GCM encryption with per-user keys derived from Firebase UID.
 * This ensures that even database administrators cannot read user notes/categories.
 */
@Service
public class EncryptionService {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;

    @Value("${app.encryption.master-key:}")
    private String masterKey;

    /**
     * Derives a user-specific encryption key from their Firebase UID.
     * This ensures each user has a unique key while allowing key recovery.
     */
    private byte[] deriveUserKey(String firebaseUid) throws Exception {
        if (masterKey == null || masterKey.isEmpty()) {
            throw new IllegalStateException("Encryption master key not configured. Set app.encryption.master-key in application.properties");
        }

        // Combine master key with user's Firebase UID
        String combined = masterKey + firebaseUid;
        
        // Use SHA-256 to derive a consistent 256-bit key
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = digest.digest(combined.getBytes(StandardCharsets.UTF_8));
        
        return keyBytes;
    }

    /**
     * Encrypts data using AES-256-GCM with a user-specific key.
     * 
     * @param plaintext The data to encrypt
     * @param firebaseUid The user's Firebase UID for key derivation
     * @return Base64-encoded encrypted data (includes IV)
     */
    public String encrypt(String plaintext, String firebaseUid) {
        if (plaintext == null || plaintext.isEmpty()) {
            return plaintext;
        }

        try {
            byte[] userKey = deriveUserKey(firebaseUid);
            SecretKeySpec secretKey = new SecretKeySpec(userKey, ALGORITHM);

            // Generate random IV for each encryption
            byte[] iv = new byte[GCM_IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            byte[] encryptedData = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            // Combine IV and encrypted data
            byte[] encryptedWithIv = new byte[GCM_IV_LENGTH + encryptedData.length];
            System.arraycopy(iv, 0, encryptedWithIv, 0, GCM_IV_LENGTH);
            System.arraycopy(encryptedData, 0, encryptedWithIv, GCM_IV_LENGTH, encryptedData.length);

            return Base64.getEncoder().encodeToString(encryptedWithIv);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    /**
     * Decrypts data using AES-256-GCM with a user-specific key.
     * 
     * @param encryptedData Base64-encoded encrypted data (includes IV)
     * @param firebaseUid The user's Firebase UID for key derivation
     * @return Decrypted plaintext
     */
    public String decrypt(String encryptedData, String firebaseUid) {
        if (encryptedData == null || encryptedData.isEmpty()) {
            return encryptedData;
        }

        try {
            byte[] userKey = deriveUserKey(firebaseUid);
            SecretKeySpec secretKey = new SecretKeySpec(userKey, ALGORITHM);

            byte[] encryptedWithIv = Base64.getDecoder().decode(encryptedData);

            // Extract IV and encrypted data
            byte[] iv = new byte[GCM_IV_LENGTH];
            System.arraycopy(encryptedWithIv, 0, iv, 0, GCM_IV_LENGTH);
            
            byte[] encrypted = new byte[encryptedWithIv.length - GCM_IV_LENGTH];
            System.arraycopy(encryptedWithIv, GCM_IV_LENGTH, encrypted, 0, encrypted.length);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

            byte[] decryptedData = cipher.doFinal(encrypted);
            return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}

