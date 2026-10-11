package com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.external;

import com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.listing.Listing;
import com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.listing.ListingRepository;
import com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.listing.ListingType;
import com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.notify.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DailyCheckJob {

    private static final Logger log = LoggerFactory.getLogger(DailyCheckJob.class);

    private final ListingRepository listings;
    private final NotificationService notifications;
    private final List<ExternalSource> sources;

    public DailyCheckJob(ListingRepository listings,
                         NotificationService notifications,
                         ObjectProvider<ExternalSource> sources) {
        this.listings = listings;
        this.notifications = notifications;
        this.sources = sources.orderedStream().toList();
    }

    @Scheduled(cron = "${app.check.cron:0 0 9 * * *}")
    public void run() {
        List<Listing> wants = listings.findByTypeAndActiveTrue(ListingType.WANT);
        log.info("Ежедневная проверка: открытых запросов {}", wants.size());
        for (Listing want : wants) {
            try {
                checkOne(want);
            } catch (Exception e) {
                log.warn("Ошибка проверки запроса {}: {}", want.getId(), e.getMessage());
            }
        }
    }

    private void checkOne(Listing want) {
        // 1. Наши объявления «Продаю»
        List<Listing> matches = listings.findMatches(
                ListingType.SELL, want.getOwner(), want.getPartNumber(), want.getTitle());
        for (Listing found : matches) {
            notifications.notifyMatch(want, found);
        }
        // 2. Внешние источники (пока не подключены ни одного)
        for (ExternalSource source : sources) {
            try {
                for (ExternalOffer o : source.find(want)) {
                    notifications.notifyExternal(want, source.name(), o.title(), o.url(), o.price());
                }
            } catch (Exception e) {
                log.warn("Источник {} недоступен: {}", source.name(), e.getMessage());
            }
        }
    }
}