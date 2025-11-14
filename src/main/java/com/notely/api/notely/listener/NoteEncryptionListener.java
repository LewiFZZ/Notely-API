package com.notely.api.notely.listener;

import com.notely.api.notely.entity.Note;
import com.notely.api.notely.service.classes.EncryptionService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Component
public class NoteEncryptionListener implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    private static EncryptionService getEncryptionService() {
        return applicationContext.getBean(EncryptionService.class);
    }

    @PrePersist
    @PreUpdate
    public void encrypt(Note note) {
        if (note.getUser() != null && note.getUser().getFirebaseUid() != null) {
            EncryptionService encryptionService = getEncryptionService();
            String uid = note.getUser().getFirebaseUid();

            if (note.getName() != null && !note.getName().isEmpty() && !isEncrypted(note.getName())) {
                note.setName(encryptionService.encrypt(note.getName(), uid));
            }

            if (note.getContent() != null && !note.getContent().isEmpty() && !isEncrypted(note.getContent())) {
                note.setContent(encryptionService.encrypt(note.getContent(), uid));
            }
        }
    }

    @PostLoad
    public void decrypt(Note note) {
        if (note.getUser() != null && note.getUser().getFirebaseUid() != null) {
            EncryptionService encryptionService = getEncryptionService();
            String uid = note.getUser().getFirebaseUid();

            try {
                if (note.getName() != null && isEncrypted(note.getName())) {
                    note.setName(encryptionService.decrypt(note.getName(), uid));
                }
                if (note.getContent() != null && isEncrypted(note.getContent())) {
                    note.setContent(encryptionService.decrypt(note.getContent(), uid));
                }
            } catch (Exception e) {
                System.err.println("Failed to decrypt note: " + e.getMessage());
            }
        }
    }

    private boolean isEncrypted(String value) {
        if (value == null || value.length() < 20) return false;
        try {
            java.util.Base64.getDecoder().decode(value);
            return value.length() > 30;
        } catch (Exception e) {
            return false;
        }
    }
}
