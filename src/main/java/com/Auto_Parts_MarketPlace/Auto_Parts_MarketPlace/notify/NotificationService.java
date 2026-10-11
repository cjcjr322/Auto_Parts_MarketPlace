package com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.notify;

import com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.listing.Listing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final JavaMailSender mailSender;
    private final String from;
    private final String baseUrl;

    public NotificationService(JavaMailSender mailSender,
                               @Value("${app.mail.from}") String from,
                               @Value("${app.base-url}") String baseUrl) {
        this.mailSender = mailSender;
        this.from = from;
        this.baseUrl = baseUrl;
    }

    /** Письмо автору запроса «Ищу»: появилась подходящая запчасть. */
    public void notifyMatch(Listing want, Listing found) {
        try {
            SimpleMailMessage m = new SimpleMailMessage();
            m.setFrom(from);
            m.setTo(want.getOwner().getEmail());
            m.setSubject("Найдена запчасть: " + found.getTitle());
            m.setText("""
                    Здравствуйте, %s!

                    Вы искали: %s

                    На сайте появилось объявление: %s
                    Автомобиль: %s
                    Цена: %s

                    Открыть объявление: %s/listing/%d
                    """.formatted(
                    want.getOwner().getName(),
                    want.getTitle(),
                    found.getTitle(),
                    found.getCarInfo().isEmpty() ? "не указан" : found.getCarInfo(),
                    found.getPrice() == null ? "не указана" : found.getPrice() + " ₽",
                    baseUrl, found.getId()));
            mailSender.send(m);
        } catch (Exception e) {
            // ошибка почты не должна ломать публикацию объявления
            log.warn("Не удалось отправить письмо: {}", e.getMessage());
        }
    }
}