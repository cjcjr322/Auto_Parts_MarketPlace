package com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.notify;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notified_matches",
        uniqueConstraints = @UniqueConstraint(columnNames = {"want_id", "match_key"}))
public class NotifiedMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "want_id", nullable = false)
    private Long wantId;

    @Column(name = "match_key", nullable = false, length = 1000)
    private String matchKey;

    @Column(nullable = false)
    private LocalDateTime sentAt = LocalDateTime.now();

    protected NotifiedMatch() {}

    public NotifiedMatch(Long wantId, String matchKey) {
        this.wantId = wantId;
        this.matchKey = matchKey;
    }
}