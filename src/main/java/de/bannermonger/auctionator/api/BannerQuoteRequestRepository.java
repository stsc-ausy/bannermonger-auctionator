package de.bannermonger.auctionator.api;

import java.util.UUID;

import de.bannermonger.auctionator.model.BannerQuoteRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerQuoteRequestRepository extends JpaRepository<BannerQuoteRequest, UUID> {
}
