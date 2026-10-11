package com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.listing;

import com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.user.User;
import com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ListingService {

    private final ListingRepository listings;
    private final UserRepository users;

    public ListingService(ListingRepository listings, UserRepository users) {
        this.listings = listings;
        this.users = users;
    }

    private User userByEmail(String email) {
        return users.findByEmail(email).orElseThrow();
    }

    public Listing create(String ownerEmail, ListingType type, String title, String partNumber,
                          String carInfo, String description, Integer price, String city) {
        Listing l = new Listing();
        l.setOwner(userByEmail(ownerEmail));
        l.setType(type);
        l.setTitle(title.trim());
        l.setPartNumber(partNumber.trim());
        l.setCarInfo(carInfo.trim());
        l.setDescription(description == null ? null : description.trim());
        l.setPrice(price);
        l.setCity(city == null ? null : city.trim());
        return listings.save(l);
    }

    public List<Listing> search(ListingType type, String q) {
        return listings.search(type, q.trim());
    }

    public Listing get(Long id) {
        return listings.findById(id)
                .filter(Listing::isActive)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<Listing> mine(String email) {
        return listings.findByOwnerOrderByCreatedAtDesc(userByEmail(email));
    }

    public void delete(Long id, String email) {
        Listing l = listings.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!l.getOwner().getEmail().equals(email)) {
            throw new AccessDeniedException("Чужое объявление");
        }
        listings.delete(l);
    }
}