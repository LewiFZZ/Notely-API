package com.notely.api.notely.listener;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.notely.api.notely.entity.Category;
import com.notely.api.notely.entity.Note;
import com.notely.api.notely.service.classes.EncryptionService;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

/**
 * JPA Entity Listener that automatically encrypts/decrypts sensitive fields.
 * This ensures data is encrypted in the database but transparent to the application.
 */
@Component
public class EncryptionListener implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        EncryptionListener.applicationContext = applicationContext;
    }

    private static EncryptionService getEncryptionService() {
        return applicationContext.getBean(EncryptionService.class);
    }

    @PrePersist
    @PreUpdate
    public void encryptNote(Note note) {
        if (note.getUser() != null && note.getUser().getFirebaseUid() != null) {
            String firebaseUid = note.getUser().getFirebaseUid();
            
            EncryptionService encryptionService = getEncryptionService();
            
            // Encrypt name and content before saving
            if (note.getName() != null && !note.getName().isEmpty()) {
                // Check if already encrypted (starts with base64 pattern and is longer)
                if (!isEncrypted(note.getName())) {
                    note.setName(encryptionService.encrypt(note.getName(), firebaseUid));
                }
            }
            
            if (note.getContent() != null && !note.getContent().isEmpty()) {
                if (!isEncrypted(note.getContent())) {
                    note.setContent(encryptionService.encrypt(note.getContent(), firebaseUid));
                }
            }
        }
    }

    @PostLoad
    public void decryptNote(Note note) {
        if (note.getUser() != null && note.getUser().getFirebaseUid() != null) {
            EncryptionService encryptionService = getEncryptionService();
            String firebaseUid = note.getUser().getFirebaseUid();
            
            // Decrypt name and content after loading
            if (note.getName() != null && !note.getName().isEmpty()) {
                if (isEncrypted(note.getName())) {
                    try {
                        note.setName(encryptionService.decrypt(note.getName(), firebaseUid));
                    } catch (Exception e) {
                        // If decryption fails, keep encrypted value (might be corrupted data)
                        System.err.println("Failed to decrypt note name: " + e.getMessage());
                    }
                }
            }
            
            if (note.getContent() != null && !note.getContent().isEmpty()) {
                if (isEncrypted(note.getContent())) {
                    try {
                        note.setContent(encryptionService.decrypt(note.getContent(), firebaseUid));
                    } catch (Exception e) {
                        System.err.println("Failed to decrypt note content: " + e.getMessage());
                    }
                }
            }
        }
    }

    @PrePersist
    @PreUpdate
    public void encryptCategory(Category category) {
        if (category.getUser() != null && category.getUser().getFirebaseUid() != null) {
            EncryptionService encryptionService = getEncryptionService();
            String firebaseUid = category.getUser().getFirebaseUid();
            
            // Encrypt name before saving
            if (category.getName() != null && !category.getName().isEmpty()) {
                if (!isEncrypted(category.getName())) {
                    category.setName(encryptionService.encrypt(category.getName(), firebaseUid));
                }
            }
        }
    }

    @PostLoad
    public void decryptCategory(Category category) {
        if (category.getUser() != null && category.getUser().getFirebaseUid() != null) {
            EncryptionService encryptionService = getEncryptionService();
            String firebaseUid = category.getUser().getFirebaseUid();
            
            // Decrypt name after loading
            if (category.getName() != null && !category.getName().isEmpty()) {
                if (isEncrypted(category.getName())) {
                    try {
                        category.setName(encryptionService.decrypt(category.getName(), firebaseUid));
                    } catch (Exception e) {
                        System.err.println("Failed to decrypt category name: " + e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Simple check to determine if a string is encrypted.
     * Encrypted strings are Base64-encoded and typically longer.
     */
    private boolean isEncrypted(String value) {
        if (value == null || value.length() < 20) {
            return false; // Too short to be encrypted
        }
        
        // Encrypted values are Base64, which only contains A-Z, a-z, 0-9, +, /, =
        // This is a heuristic - not perfect but works for most cases
        try {
            // Try to decode as Base64
            java.util.Base64.getDecoder().decode(value);
            // If it decodes successfully and is reasonably long, assume encrypted
            return value.length() > 30;
        } catch (Exception e) {
            return false;
        }
    }
}

