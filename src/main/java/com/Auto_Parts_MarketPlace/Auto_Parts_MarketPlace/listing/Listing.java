package com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.listing;

import com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "listings")
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ListingType type;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String partNumber = "";

    @Column(nullable = false)
    private String carInfo = "";

    @Column(length = 2000)
    private String description;

    private Integer price;

    private String city;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private boolean active = true;

    @ManyToOne(optional = false)
    private User owner;

    public Long getId() { return id; }
    public ListingType getType() { return type; }
    public void setType(ListingType type) { this.type = type; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getPartNumber() { return partNumber; }
    public void setPartNumber(String partNumber) { this.partNumber = partNumber; }
    public String getCarInfo() { return carInfo; }
    public void setCarInfo(String carInfo) { this.carInfo = carInfo; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
}