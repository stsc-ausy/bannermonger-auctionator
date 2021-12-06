package de.bannermonger.auctionator.api;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import de.bannermonger.auctionator.model.BannerOffer;
import de.bannermonger.auctionator.model.BannerSpecification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerOfferRepository extends JpaRepository<BannerOffer, UUID> {

    Optional<BannerOffer> findByOfferId(String offerId);

    List<BannerOffer> findByBannerSpecification(BannerSpecification specification);

}
