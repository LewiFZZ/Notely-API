package com.notely.api.notely.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp initializeFirebaseApp() throws IOException {

        InputStream serviceAccount = new ClassPathResource("notely-cc9a6-firebase-adminsdk-xxxxx-xxxxxxxxxx.json").getInputStream(); // ¡IMPORTANTE: Renombra con el nombre de tu archivo!


        FirebaseOptions options = FirebaseOptions.builder()

                .setCredentials(GoogleCredentials.fromStream(serviceAccount))

                .build();


        if (FirebaseApp.getApps().isEmpty()) { // Previene la inicialización múltiple si ya existe

            return FirebaseApp.initializeApp(options);

        } else {

            return FirebaseApp.getInstance();

        }

    }

}
