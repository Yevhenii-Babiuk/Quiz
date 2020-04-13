package com.qucat.quiz.config;

import com.qucat.quiz.services.QucatSessionAuthenticator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.mail.Session;
import java.io.IOException;
import java.util.Properties;


@Slf4j
@Configuration
public class MailConfiguration {

    private final String CONFIG_PROPERTIES = "mail/application-mail-config.properties";

    @Bean
    public Session emailSession(QucatSessionAuthenticator sessionAuthenticator) {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream(CONFIG_PROPERTIES));
        } catch (IOException e) {
            log.error("Failed to load email session properties", e);
        }
        return Session.getInstance(properties, sessionAuthenticator);
    }
}
