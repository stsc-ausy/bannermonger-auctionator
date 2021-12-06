package de.bannermonger.auctionator.impl;

import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bannermonger.auctionator.api.BannerOfferRepository;
import de.bannermonger.auctionator.api.BannerSpecificationRepository;
import de.bannermonger.auctionator.api.TransformationException;
import de.bannermonger.auctionator.model.BannerOffer;
import de.bannermonger.auctionator.model.BannerSpecification;
import de.bannermonger.auctionator.model.BannerSpecificationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BannerOfferHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BannerOfferHandler.class);

    private final ObjectMapper objectMapper;
    private final BannerOfferRepository bannerOfferRepository;
    private final BannerSpecificationRepository bannerSpecificationRepository;

    public BannerOfferHandler(
            ObjectMapper objectMapper,
            BannerOfferRepository bannerOfferRepository,
            BannerSpecificationRepository bannerSpecificationRepository) {
        this.objectMapper = objectMapper;
        this.bannerOfferRepository = bannerOfferRepository;
        this.bannerSpecificationRepository = bannerSpecificationRepository;
    }

    @Transactional
    @JmsListener(destination = "${bannermonger.auctionator.bannerOfferIn}")
    void handleBannerOffer(String message) {
        LOGGER.info("Received BannerOffer: {}", message);
        try {
            BannerOffer bo = objectMapper.readValue(message, BannerOffer.class);
            LOGGER.debug("Converted {} to {}", message, bo);
            Optional<BannerOffer> byOfferId = bannerOfferRepository.findByOfferId(bo.getOfferId());
            if (byOfferId.isEmpty()) {
                Optional<BannerSpecification> bso =
                        bannerSpecificationRepository.findByReference(bo.getReference());
                if (bso.isPresent()) {
                    BannerSpecification bs = bso.get();
                    bs.setStatus(BannerSpecificationStatus.OFFER_RECEIVED);
                    bo.setBannerSpecification(bs);
                    bannerOfferRepository.save(bo);
                    LOGGER.debug("Saved new BannerOffer {}", bo);
                } else {
                    LOGGER.error("Offer received for unknown specification " + bo.getReference());
                }
            } else {
                LOGGER.error("Offer with id " +  bo.getOfferId() + " already processed");
            }
        } catch (JsonProcessingException e) {
            throw new TransformationException("Unable to deserialize '" + message + "'", e);
        }
    }

}
