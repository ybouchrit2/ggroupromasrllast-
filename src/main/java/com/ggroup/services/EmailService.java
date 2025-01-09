package com.ggroup.services;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ggroup.models.Contact;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Value("${email.sender.address}") // قراءة البريد الإلكتروني من الملف الخاص بالإعدادات
    private String from;

    @Value("${email.sender.appPassword}") // قراءة كلمة مرور التطبيق من الملف الخاص بالإعدادات
    private String appPassword;

    private final String smtpHost = "smtp.gmail.com";
    private final int smtpPort = 587;
    private final Session session;
    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    // إعداد الجلسة مرة واحدة فقط
    public EmailService() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, appPassword);
            }
        });
    }

    // إرسال إشعار عن المدونة الجديدة إلى جهة الاتصال
    public void sendBlogNotification(Contact contact, String title, String description, String link)
            throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(contact.getEmail()));
        message.setSubject("Nuovo Blog da G. GROUP: " + title); // العنوان باللغة الإيطالية

        String emailContent = "<p>Caro/Cara " + contact.getName() + ",</p>" +
                "<p>Ti informiamo che è stato pubblicato un nuovo blog sul nostro sito intitolato: <strong>" + title
                + "</strong></p>" +
                "<p><strong>Descrizione del blog:</strong><br>" + description + "</p>" +
                "<p>Per visualizzare il blog completo, puoi visitare il seguente link:</p>" +
                "<p><a href='" + link + "'>Link al blog</a></p>" +
                "<p>Speriamo che tu possa goderti la lettura del blog e trovarlo utile.</p>" +
                "<p>Cordiali saluti,<br>Il team di G. GROUP S.R.L</p>";

        message.setContent(emailContent, "text/html; charset=UTF-8");

        // إرسال الرسالة باستخدام try-with-resources لضمان إغلاق الاتصال بشكل صحيح
        try (Transport transport = session.getTransport("smtp")) {
            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            logger.info("Email sent to: {}", contact.getEmail());
        } catch (MessagingException e) {
            logger.error("Failed to send email to: {}. Error: {}", contact.getEmail(), e.getMessage());
            throw e; // إعادة طرح الاستثناء في حال حدوث خطأ
        }
    }

    // إرسال رسالة شكر عبر البريد الإلكتروني إلى جهة الاتصال
    public void sendEmail(Contact contact) {
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(contact.getEmail()));
            message.setSubject("Grazie per averci contattato - G. GROUP S.R.L"); // العنوان باللغة الإيطالية

            String emailContent = "<p>Caro/Cara " + contact.getName() + ",</p>" +
                    "<p>Grazie per averci contattato tramite il nostro modulo di contatto.</p>" +
                    "<p>Abbiamo ricevuto il tuo messaggio e lo esamineremo con attenzione. Il nostro team si occuperà di fornirti il supporto necessario o di rispondere alla tua richiesta il prima possibile.</p>"
                    +
                    "<p><strong>Dettagli del tuo messaggio:</strong><br>" +
                    "- Email: " + contact.getEmail() + "<br>" +
                    "- Messaggio:<br>" + contact.getDescription() + "</p>" +
                    "<p><strong>Perché scegliere G. GROUP?</strong><br>" +
                    "Siamo specializzati in costruzioni, ferrovie e altri servizi, e siamo sempre impegnati a fornire le migliori soluzioni per i nostri clienti.</p>"
                    +
                    "<p>Se hai bisogno di ulteriore assistenza o hai domande urgenti, puoi contattarci direttamente ai seguenti recapiti:</p>"
                    +
                    "<ul>" +
                    "<li>Email: support@ggroup.com</li>" +
                    "<li>Telefono: +39 123 456 789</li>" +
                    "</ul>" +
                    "<p>Grazie ancora per il tuo interesse nei nostri servizi. Non vediamo l'ora di servirti presto!</p>"
                    +
                    "<p>Cordiali saluti,<br>Il team di G. GROUP S.R.L</p>";

            message.setContent(emailContent, "text/html; charset=UTF-8");

            // إرسال الرسالة باستخدام try-with-resources
            try (Transport transport = session.getTransport("smtp")) {
                transport.connect();
                transport.sendMessage(message, message.getAllRecipients());
                logger.info("Email sent successfully to: {}", contact.getEmail());
            }
        } catch (MessagingException e) {
            logger.error("Failed to send email to: {}. Error: {}", contact.getEmail(), e.getMessage());
        }
    }
}
