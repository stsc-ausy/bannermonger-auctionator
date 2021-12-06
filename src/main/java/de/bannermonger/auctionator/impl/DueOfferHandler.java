package de.bannermonger.auctionator.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bannermonger.auctionator.api.BannerOfferRepository;
import de.bannermonger.auctionator.api.BannerOrderRepository;
import de.bannermonger.auctionator.api.BannerSpecificationRepository;
import de.bannermonger.auctionator.api.TransformationException;
import de.bannermonger.auctionator.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DueOfferHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DueOfferHandler.class);

    private final BannerSpecificationRepository bannerSpecificationRepository;
    private final BannerOfferRepository bannerOfferRepository;
    private final BannerOrderRepository bannerOrderRepository;
    private final JmsTemplate template;
    private final String destination;
    private final ObjectMapper objectMapper;

    public DueOfferHandler(
            BannerSpecificationRepository bannerSpecificationRepository,
            BannerOfferRepository bannerOfferRepository,
            BannerOrderRepository bannerOrderRepository,
            JmsTemplate template,
            @Value("${bannermonger.auctionator.bannerOrderOut}") String destination,
            ObjectMapper objectMapper) {
        this.bannerSpecificationRepository = bannerSpecificationRepository;
        this.bannerOfferRepository = bannerOfferRepository;
        this.bannerOrderRepository = bannerOrderRepository;
        this.template = template;
        this.destination = destination;
        this.objectMapper = objectMapper;
    }

    @Scheduled(
            initialDelayString = "${bannermonger.auctionator.offerProcessing.initialDelay}",
            fixedDelayString = "${bannermonger.auctionator.offerProcessing.fixedDelay}"
    )
    @Transactional
    public void handleEvent() {
        List<BannerSpecification> due = bannerSpecificationRepository
                .findDueAtNotStatus(LocalDateTime.now(), BannerSpecificationStatus.NO_OFFER_AVAILABLE);
        for (BannerSpecification bs : due) {
            List<BannerOffer> bos = bannerOfferRepository
                    .findByBannerSpecification(bs);
            List<BannerOffer> valid = new ArrayList<>();
            for (BannerOffer offer : bos) {
                boolean isValid = offer.getGuaranteedRelevance().compareTo(bs.getRequiredRelevance()) >= 0
                        && offer.getGuaranteedPageViews().compareTo(bs.getMinPageViews()) >= 0
                        && calcPrice(offer).compareTo(bs.getMaxPriceInEuro()) <= 0;
                if (isValid) {
                    LOGGER.info("Valid offer for specification {}: {}", bs.getReference(), offer);
                    valid.add(offer);
                } else {
                    LOGGER.info("Does not qualify for specification {}: {}", bs.getReference(), offer);
                }
            }
            valid.sort(new BannerOfferComparator());

            if (!valid.isEmpty()) {
                BannerOffer bannerOffer = valid.get(0);
                BannerOrder bo = new BannerOrder();
                bo.setReference(bannerOffer.getReference());
                bo.setOfferId(bannerOffer.getOfferId());
                bo.setImageUrl(bs.getImageUrl());
                bo.setProvider(bannerOffer.getIssuer());
                bo.setOrderedAt(LocalDateTime.now());
                bo.setBannerSpecification(bs);
                bannerOrderRepository.save(bo);
                LOGGER.debug("Saved new BannerOrder {}", bo);
                try {
                    String m = objectMapper.writeValueAsString(bo);
                    LOGGER.debug("Converted {} to '{}'", bo, m);
                    template.send(destination, session -> session.createTextMessage(m));
                    LOGGER.info("Sent to {}: {}", destination, m);
                } catch (JsonProcessingException e) {
                    throw new TransformationException("Unable to serialize " + bo.toString(), e);
                }
                bs.setStatus(BannerSpecificationStatus.ORDERED);
            } else {
                bs.setStatus(BannerSpecificationStatus.NO_OFFER_AVAILABLE);
                LOGGER.info("No offers available for banner spec {}", bs.getReference());
            }
        }
    }

    private BigDecimal calcPrice(BannerOffer bo) {
        return PriceModel.PER_VIEW == bo.getPriceModel()
                ? bo.getBasePriceInEuro().multiply(new BigDecimal(bo.getGuaranteedPageViews()))
                : bo.getBasePriceInEuro();
    }

    private class BannerOfferComparator implements Comparator<BannerOffer> {
        @Override
        public int compare(BannerOffer bo1, BannerOffer bo2) {
            return calcPrice(bo1).compareTo(calcPrice(bo2));
        }
    }
}
