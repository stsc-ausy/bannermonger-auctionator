package de.bannermonger.auctionator.api;

import java.util.UUID;

import de.bannermonger.auctionator.model.BannerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerOrderRepository extends JpaRepository<BannerOrder, UUID> {
}
