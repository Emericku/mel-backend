package fr.polytech.melusine.components;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailManager {

    private JavaMailSender emailSender;


    public EmailManager(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void resendPassword(String email, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Melusine - Nouveau mot de passe");
        message.setText("Votre nouveau mot de passe : \n" + password + "\n");
        emailSender.send(message);
    }

}
