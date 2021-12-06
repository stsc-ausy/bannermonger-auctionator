package de.bannermonger.auctionator.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "banner_offer")
public class BannerOffer implements Serializable {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @JsonIgnore
    private UUID objectId;

    @Column(name = "offer_id", nullable = false)
    private String offerId;

    @Column(name = "reference", nullable = false)
    private String reference;

    @Column(name = "issuer", nullable = false)
    private String issuer;

    @Enumerated(EnumType.STRING)
    @Column(name = "price_model", nullable = false)
    private PriceModel priceModel;

    @Column(name = "guaranteed_relevance", nullable = false)
    private BigDecimal guaranteedRelevance;

    @Column(name = "guaranteed_page_views", nullable = false)
    private BigInteger guaranteedPageViews;

    @Column(name = "price_in_euro", nullable = false)
    private BigDecimal basePriceInEuro;

    @Column(name = "offer_validity", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime offerValidUntil;

    @ManyToOne
    @JoinColumn(name = "banner_spec_id")
    @JsonIgnore
    private BannerSpecification bannerSpecification;

    public UUID getObjectId() {
        return objectId;
    }

    public void setObjectId(UUID objectId) {
        this.objectId = objectId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public PriceModel getPriceModel() {
        return priceModel;
    }

    public void setPriceModel(PriceModel priceModel) {
        this.priceModel = priceModel;
    }

    public BigDecimal getGuaranteedRelevance() {
        return guaranteedRelevance;
    }

    public void setGuaranteedRelevance(BigDecimal guaranteedRelevance) {
        this.guaranteedRelevance = guaranteedRelevance;
    }

    public BigInteger getGuaranteedPageViews() {
        return guaranteedPageViews;
    }

    public void setGuaranteedPageViews(BigInteger guaranteedPageViews) {
        this.guaranteedPageViews = guaranteedPageViews;
    }

    public BigDecimal getBasePriceInEuro() {
        return basePriceInEuro;
    }

    public void setBasePriceInEuro(BigDecimal basePriceInEuro) {
        this.basePriceInEuro = basePriceInEuro;
    }

    public LocalDateTime getOfferValidUntil() {
        return offerValidUntil;
    }

    public void setOfferValidUntil(LocalDateTime offerValidUntil) {
        this.offerValidUntil = offerValidUntil;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public BannerSpecification getBannerSpecification() {
        return bannerSpecification;
    }

    public void setBannerSpecification(BannerSpecification bannerSpecification) {
        this.bannerSpecification = bannerSpecification;
    }

    @Override
    public String toString() {
        return "BannerOffer{" +
                "objectId=" + objectId +
                ", offerId='" + offerId + '\'' +
                ", reference='" + reference + '\'' +
                ", issuer='" + issuer + '\'' +
                ", priceModel=" + priceModel +
                ", guaranteedRelevance=" + guaranteedRelevance +
                ", guaranteedPageViews=" + guaranteedPageViews +
                ", basePriceInEuro=" + basePriceInEuro +
                ", offerValidUntil=" + offerValidUntil +
                '}';
    }
}
