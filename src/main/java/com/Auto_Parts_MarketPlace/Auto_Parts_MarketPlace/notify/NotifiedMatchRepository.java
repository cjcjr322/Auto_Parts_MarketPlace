package com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.notify;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotifiedMatchRepository extends JpaRepository<NotifiedMatch, Long> {
    boolean existsByWantIdAndMatchKey(Long wantId, String matchKey);
}