package com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.listing;

import com.Auto_Parts_MarketPlace.Auto_Parts_MarketPlace.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ListingRepository extends JpaRepository<Listing, Long> {

    @Query("""
        select l from Listing l
        where l.active = true and l.type = :type
          and (lower(l.title) like lower(concat('%', :q, '%'))
            or lower(l.partNumber) like lower(concat('%', :q, '%'))
            or lower(l.carInfo) like lower(concat('%', :q, '%')))
        order by l.createdAt desc
        """)
    List<Listing> search(@Param("type") ListingType type, @Param("q") String q);

    List<Listing> findByOwnerOrderByCreatedAtDesc(User owner);
}