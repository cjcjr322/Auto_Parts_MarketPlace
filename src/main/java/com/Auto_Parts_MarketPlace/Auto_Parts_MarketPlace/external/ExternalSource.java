package com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.external;

import com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.listing.Listing;

import java.util.List;

public interface ExternalSource {
    String name();

    /** Вернуть предложения, которые есть в наличии для запроса «Ищу». */
    List<ExternalOffer> find(Listing want);
}