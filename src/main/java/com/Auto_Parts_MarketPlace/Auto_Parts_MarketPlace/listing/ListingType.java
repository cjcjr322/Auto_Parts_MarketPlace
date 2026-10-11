package com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.listing;

public enum ListingType {
    SELL("Продаю"),
    WANT("Ищу");

    private final String label;

    ListingType(String label) { this.label = label; }

    public String getLabel() { return label; }
}