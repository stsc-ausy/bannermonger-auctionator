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
@Table(name = "banner_spec")
public class BannerSpecification implements Serializable {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @JsonIgnore
    private UUID objectId;

    @Column(name = "reference", nullable = false)
    private String reference;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @JsonIgnore
    private BannerSpecificationStatus status;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "banner_size", nullable = false)
    private BannerSize bannerSize;

    @Column(name = "relevance_required", nullable = false)
    private BigDecimal requiredRelevance;

    @Column(name = "max_price_euro", nullable = false)
    private BigDecimal maxPriceInEuro;

    @Column(name = "min_page_views", nullable = false)
    private BigInteger minPageViews;

    @Column(name = "valid_through", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime validThrough;

    @Embedded
    private TargetAudience targetAudience;

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BannerSize getBannerSize() {
        return bannerSize;
    }

    public void setBannerSize(BannerSize bannerSize) {
        this.bannerSize = bannerSize;
    }

    public BigDecimal getRequiredRelevance() {
        return requiredRelevance;
    }

    public void setRequiredRelevance(BigDecimal requiredRelevance) {
        this.requiredRelevance = requiredRelevance;
    }

    public BigDecimal getMaxPriceInEuro() {
        return maxPriceInEuro;
    }

    public void setMaxPriceInEuro(BigDecimal maxPriceInEuro) {
        this.maxPriceInEuro = maxPriceInEuro;
    }

    public TargetAudience getTargetAudience() {
        return targetAudience;
    }

    public void setTargetAudience(TargetAudience targetAudience) {
        this.targetAudience = targetAudience;
    }

    public BigInteger getMinPageViews() {
        return minPageViews;
    }

    public void setMinPageViews(BigInteger minPageViews) {
        this.minPageViews = minPageViews;
    }

    public LocalDateTime getValidThrough() {
        return validThrough;
    }

    public void setValidThrough(LocalDateTime validThrough) {
        this.validThrough = validThrough;
    }

    public BannerSpecificationStatus getStatus() {
        return status;
    }

    public void setStatus(BannerSpecificationStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BannerSpecification{" +
                "objectId=" + objectId +
                ", reference='" + reference + '\'' +
                ", status=" + status +
                ", imageUrl='" + imageUrl + '\'' +
                ", bannerSize=" + bannerSize +
                ", requiredRelevance=" + requiredRelevance +
                ", maxPriceInEuro=" + maxPriceInEuro +
                ", minPageViews=" + minPageViews +
                ", validThrough=" + validThrough +
                ", targetAudience=" + targetAudience +
                '}';
    }
}
