package com.notely.api.notely.listener;

import com.notely.api.notely.entity.Category;
import com.notely.api.notely.service.classes.EncryptionService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Component
public class CategoryEncryptionListener implements ApplicationContextAware {

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
    public void encrypt(Category category) {
        if (category.getUser() != null && category.getUser().getFirebaseUid() != null) {
            EncryptionService encryptionService = getEncryptionService();
            String uid = category.getUser().getFirebaseUid();

            if (category.getName() != null && !category.getName().isEmpty() && !isEncrypted(category.getName())) {
                category.setName(encryptionService.encrypt(category.getName(), uid));
            }
        }
    }

    @PostLoad
    public void decrypt(Category category) {
        if (category.getUser() != null && category.getUser().getFirebaseUid() != null) {
            EncryptionService encryptionService = getEncryptionService();
            String uid = category.getUser().getFirebaseUid();

            try {
                if (category.getName() != null && isEncrypted(category.getName())) {
                    category.setName(encryptionService.decrypt(category.getName(), uid));
                }
            } catch (Exception e) {
                System.err.println("Failed to decrypt category: " + e.getMessage());
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
