package com.notely.api.notely.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.service-account.file:notely-cc9a6-firebase-adminsdk-fbsvc-54ca71e0a3.json}")
    private String firebaseServiceAccountFile;

    @Bean
    public FirebaseApp initializeFirebaseApp() throws IOException {
        InputStream serviceAccount = null;
        
        try {
            // First, try to load from the same package as this class (config package)
            // This works if the file is in src/main/java/com/notely/api/notely/config/
            InputStream classResource = this.getClass().getClassLoader()
                    .getResourceAsStream("com/notely/api/notely/config/" + firebaseServiceAccountFile);
            if (classResource != null) {
                serviceAccount = classResource;
                System.out.println("Firebase service account loaded from config package: com/notely/api/notely/config/" + firebaseServiceAccountFile);
            } else {
                // Try loading from classpath root (src/main/resources)
                ClassPathResource rootResource = new ClassPathResource(firebaseServiceAccountFile);
                if (rootResource.exists()) {
                    serviceAccount = rootResource.getInputStream();
                    System.out.println("Firebase service account loaded from classpath root: " + firebaseServiceAccountFile);
                } else {
                    // Try loading from config directory relative to project root
                    File configFile = new File("config/" + firebaseServiceAccountFile);
                    if (configFile.exists() && configFile.isFile()) {
                        serviceAccount = new FileInputStream(configFile);
                        System.out.println("Firebase service account loaded from config directory: " + configFile.getAbsolutePath());
                    } else {
                        // Try loading from file system (absolute or relative path)
                        File file = new File(firebaseServiceAccountFile);
                        if (file.exists() && file.isFile()) {
                            serviceAccount = new FileInputStream(file);
                            System.out.println("Firebase service account loaded from file system: " + firebaseServiceAccountFile);
                        } else {
                            throw new IOException(
                                "Firebase service account file not found!\n" +
                                "Tried locations:\n" +
                                "  1. Config package: com/notely/api/notely/config/" + firebaseServiceAccountFile + "\n" +
                                "  2. Classpath root: " + firebaseServiceAccountFile + "\n" +
                                "  3. Config directory: config/" + firebaseServiceAccountFile + "\n" +
                                "  4. File path: " + firebaseServiceAccountFile + "\n" +
                                "Please ensure the file exists in one of these locations."
                            );
                        }
                    }
                }
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                return FirebaseApp.initializeApp(options);
            } else {
                return FirebaseApp.getInstance();
            }
        } finally {
            if (serviceAccount != null) {
                serviceAccount.close();
            }
        }
    }
}
