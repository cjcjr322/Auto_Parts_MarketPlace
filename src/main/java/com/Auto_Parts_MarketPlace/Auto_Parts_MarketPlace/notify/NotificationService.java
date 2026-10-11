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
    private final NotifiedMatchRepository notified;
    private final String from;
    private final String baseUrl;

    public NotificationService(JavaMailSender mailSender,
                               NotifiedMatchRepository notified,
                               @Value("${app.mail.from}") String from,
                               @Value("${app.base-url}") String baseUrl) {
        this.mailSender = mailSender;
        this.notified = notified;
        this.from = from;
        this.baseUrl = baseUrl;
    }

    /** Письмо автору запроса «Ищу»: на нашем сайте появилась подходящая запчасть. */
    public void notifyMatch(Listing want, Listing found) {
        String key = "listing:" + found.getId();
        if (notified.existsByWantIdAndMatchKey(want.getId(), key)) return;
        String text = """
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
                baseUrl, found.getId());
        send(want, key, "Найдена запчасть: " + found.getTitle(), text);
    }

    /** Письмо автору запроса «Ищу»: запчасть найдена на стороннем сайте. */
    public void notifyExternal(Listing want, String sourceName, String title, String url, String price) {
        String key = "ext:" + url;
        if (notified.existsByWantIdAndMatchKey(want.getId(), key)) return;
        String text = """
                Здравствуйте, %s!

                Вы искали: %s

                Найдено на сайте «%s»: %s
                Цена: %s

                Открыть: %s
                """.formatted(
                want.getOwner().getName(),
                want.getTitle(),
                sourceName, title,
                price == null ? "не указана" : price,
                url);
        send(want, key, "Найдена запчасть: " + title, text);
    }

    private void send(Listing want, String key, String subject, String text) {
        try {
            SimpleMailMessage m = new SimpleMailMessage();
            m.setFrom(from);
            m.setTo(want.getOwner().getEmail());
            m.setSubject(subject);
            m.setText(text);
            mailSender.send(m);
            notified.save(new NotifiedMatch(want.getId(), key));
        } catch (Exception e) {
            // ошибка почты не должна ломать работу сайта; при следующей проверке попробуем снова
            log.warn("Не удалось отправить письмо: {}", e.getMessage());
        }
    }
}