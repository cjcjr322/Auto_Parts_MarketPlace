package com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.external;

import com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.listing.Listing;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExternalLinkService {

    public record Link(String name, String url) {}

    public List<Link> forQuery(String query) {
        String text = query == null ? "" : query.trim();
        if (text.isEmpty()) return List.of();
        String q = URLEncoder.encode(text, StandardCharsets.UTF_8);

        List<Link> links = new ArrayList<>();
        links.add(new Link("Авито", "https://www.avito.ru/rossiya?q=" + q));
        links.add(new Link("Drom (барахолка)", "https://baza.drom.ru/search?query=" + q));

        boolean looksLikePartNumber = text.matches("[A-Za-z0-9\\-]{4,}") && text.matches(".*\\d.*");
        if (looksLikePartNumber) {
            links.add(new Link("Exist.ru", "https://exist.ru/Price/?pcode=" + q));
            links.add(new Link("Emex", "https://emex.ru/products/" + q));
        }
        return links;
    }

    public List<Link> forListing(Listing l) {
        String query = !l.getPartNumber().isBlank()
                ? l.getPartNumber()
                : (l.getTitle() + " " + l.getCarInfo()).trim();
        return forQuery(query);
    }
}