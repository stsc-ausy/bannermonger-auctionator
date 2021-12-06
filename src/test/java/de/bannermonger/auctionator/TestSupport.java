package de.bannermonger.auctionator;

import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class TestSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestSupport.class);

    private final JmsTemplate jmsTemplate;
    private final String bannerQuoteRequestOut;
    private final String bannerOfferIn;
    private final String bannerOrderOut;

    TestSupport(
            JmsTemplate jmsTemplate,
            @Value("${bannermonger.auctionator.bannerQuoteRequestOut}") String bannerQuoteRequestOut,
            @Value("${bannermonger.auctionator.bannerOfferIn}")String bannerOfferIn,
            @Value("${bannermonger.auctionator.bannerOrderOut}")String bannerOrderOut) {
        this.jmsTemplate = jmsTemplate;
        this.bannerQuoteRequestOut = bannerQuoteRequestOut;
        this.bannerOfferIn = bannerOfferIn;
        this.bannerOrderOut = bannerOrderOut;
    }

    @Transactional
    public String receiveQuoteRequest() throws Exception {
        TextMessage message = (TextMessage) jmsTemplate.receive(bannerQuoteRequestOut);
        String body = message.getText();
        LOGGER.info("Received from {}: {}", bannerQuoteRequestOut, body);
        return body;
    }

    @Transactional
    public void sendBannerOffer(String message) {
         jmsTemplate.send(bannerOfferIn, s -> s.createTextMessage(message));
         LOGGER.info("Sent to {}: {}", bannerOfferIn, message);
    }

    @Transactional
    public String receiveBannerOrder() throws Exception {
        TextMessage message = (TextMessage) jmsTemplate.receive(bannerOrderOut);
        String body = message.getText();
        LOGGER.info("Received from {}: {}", bannerOrderOut, body);
        return body;
    }

}
