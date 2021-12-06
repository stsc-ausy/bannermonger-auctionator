package de.bannermonger.auctionator.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import de.bannermonger.auctionator.model.BannerSpecification;
import de.bannermonger.auctionator.model.BannerSpecificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BannerSpecificationRepository extends JpaRepository<BannerSpecification, UUID> {

    Optional<BannerSpecification> findByReference(String reference);

    @Query("select bs from BannerSpecification bs where bs.validThrough < ?1 and bs.status <> ?2")
    List<BannerSpecification> findDueAtNotStatus(LocalDateTime timestamp, BannerSpecificationStatus status);

}
