package de.bannermonger.auctionator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bannermonger.auctionator.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestSupport testSupport;
    
    @Autowired
    private ObjectMapper objectMapper;

    private WebTarget client;

    @BeforeEach
    void setUp() {
        String serviceUrl = "http://localhost:" + port + "/api/auctionator";
        client = ClientBuilder.newClient().target(serviceUrl);
    }

    @Test
    void UseCaseTwoValidOffers() throws Exception {
        String reference = "000000000001";
        String body = createBannerSpec(reference);
        client.path("/bannerspec").request().post(Entity.text(body));
        testSupport.receiveQuoteRequest();
        List<String> offers = List.of(
                createOffer(reference, "spiegelei", new BigDecimal("0.82"), PriceModel.FLAT, new BigDecimal("10500.00")),
                createOffer(reference, "ficus.de", new BigDecimal("0.85"), PriceModel.PER_VIEW, new BigDecimal("0.10")),
                createOffer(reference, "alexläufer", new BigDecimal("0.89"), PriceModel.PER_VIEW, new BigDecimal("0.12"))
        );
        for (String offer : offers) {
            testSupport.sendBannerOffer(offer);
        }
        Thread.sleep(5000);
        testSupport.receiveBannerOrder();
    }

    @Test
    void UseCaseBannerSpecDuplicate() throws Exception {
        String reference = "0123456789";
        String body = createBannerSpec(reference);
        client.path("/bannerspec").request().post(Entity.text(body));
    }

    private String createBannerSpec(String reference) throws Exception {
        BannerSpecification bs = new BannerSpecification();
        bs.setReference(reference);
        bs.setBannerSize(BannerSize.MEDIUM);
        bs.setImageUrl("https://adserver.de/000000000001.jpg");
        bs.setRequiredRelevance(new BigDecimal("0.80"));
        bs.setMinPageViews(new BigInteger("100000"));
        bs.setMaxPriceInEuro(new BigDecimal("15000.00"));
        TargetAudience t = new TargetAudience();
        t.setGender(Gender.ALL);
        t.setAgeGroup(AgeGroup.ALL);
        t.setTopics(List.of("sport", "entertainment", "news"));
        bs.setTargetAudience(t);
        LocalDateTime v = LocalDateTime.now().plus(5, ChronoUnit.SECONDS);
        bs.setValidThrough(v);
        return objectMapper.writeValueAsString(bs);
    }

    private String createOffer(String reference, String issuer, BigDecimal relevance, PriceModel priceModel, BigDecimal price) throws Exception {
        BannerOffer offer = new BannerOffer();
        offer.setReference(reference);
        offer.setIssuer(issuer);
        offer.setOfferId(reference + "_" + issuer);
        offer.setPriceModel(priceModel);
        offer.setBasePriceInEuro(price);
        offer.setOfferValidUntil(LocalDateTime.now().plus(5, ChronoUnit.SECONDS));
        offer.setGuaranteedPageViews(new BigInteger("150000"));
        offer.setGuaranteedRelevance(relevance);
        return objectMapper.writeValueAsString(offer);
    }
}
