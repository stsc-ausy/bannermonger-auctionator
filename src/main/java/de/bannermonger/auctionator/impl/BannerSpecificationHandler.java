package de.bannermonger.auctionator.impl;

import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bannermonger.auctionator.api.BannerQuoteRequestRepository;
import de.bannermonger.auctionator.api.BannerSpecificationRepository;
import de.bannermonger.auctionator.api.DuplicateException;
import de.bannermonger.auctionator.api.TransformationException;
import de.bannermonger.auctionator.model.BannerQuoteRequest;
import de.bannermonger.auctionator.model.BannerSpecification;
import de.bannermonger.auctionator.model.BannerSpecificationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BannerSpecificationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BannerSpecificationHandler.class);
    private final BannerSpecificationRepository bannerSpecificationRepository;
    private final BannerQuoteRequestRepository bannerQuoteRequestRepository;
    private final JmsTemplate template;
    private final String destination;
    private final ObjectMapper objectMapper;

    public BannerSpecificationHandler(
            BannerSpecificationRepository bannerSpecificationRepository,
            BannerQuoteRequestRepository bannerQuoteRequestRepository,
            JmsTemplate template,
            @Value("${bannermonger.auctionator.bannerQuoteRequestOut}") String destination,
            ObjectMapper objectMapper) {
        this.bannerSpecificationRepository = bannerSpecificationRepository;
        this.bannerQuoteRequestRepository = bannerQuoteRequestRepository;
        this.template = template;
        this.destination = destination;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void handleBannerSpecification(BannerSpecification bannerSpecification) {
        Optional<BannerSpecification> dup = bannerSpecificationRepository.findByReference(bannerSpecification.getReference());
        if (dup.isEmpty()) {
            bannerSpecification.setStatus(BannerSpecificationStatus.QUOTE_REQUESTED);
            bannerSpecificationRepository.save(bannerSpecification);
            LOGGER.debug("Saved new BannerSpecification {}", bannerSpecification);
            BannerQuoteRequest qr = new BannerQuoteRequest();
            qr.setReference(bannerSpecification.getReference());
            qr.setHeight(bannerSpecification.getBannerSize().getHeight());
            qr.setWidth(bannerSpecification.getBannerSize().getWidth());
            qr.setMinPageViews(bannerSpecification.getMinPageViews());
            qr.setGender(bannerSpecification.getTargetAudience().getGender());
            qr.setMinAge(bannerSpecification.getTargetAudience().getAgeGroup().getMinAge());
            qr.setMaxAge(bannerSpecification.getTargetAudience().getAgeGroup().getMaxAge());
            qr.setDeadLine(bannerSpecification.getValidThrough());
            qr.setBannerSpecification(bannerSpecification);
            String t = bannerSpecification
                    .getTargetAudience()
                    .getTopics()
                    .stream()
                    .reduce("", (s1, s2) -> s1 == "" ? s2 : s1 + ',' + s2);
            qr.setTags(t);
            bannerQuoteRequestRepository.save(qr);
            LOGGER.debug("Saved new BannerQuoteRequest {}", qr);
            try {
                String m = objectMapper.writeValueAsString(qr);
                LOGGER.debug("Converted {} to '{}'", qr, m);
                template.send(destination, session -> session.createTextMessage(m));
                LOGGER.info("Sent to {}: {}", destination, m);
            } catch (JsonProcessingException e) {
                throw new TransformationException("Unable to serialize " + qr.toString(), e);
            }
        } else {
            throw new DuplicateException("Banner specification with reference "
                    + bannerSpecification.getReference() + " already processed");
        }
    }

}
