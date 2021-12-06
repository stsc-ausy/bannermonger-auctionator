package de.bannermonger.auctionator.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "banner_order")
public class BannerOrder implements Serializable {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @JsonIgnore
    private UUID objectId;

    @Column(name = "offer_id", nullable = false)
    private String offerId;

    @Column(name = "reference", nullable = false)
    private String reference;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "ordered_at", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime orderedAt;

    @OneToOne
    @JoinColumn(name = "banner_spec_id")
    @JsonIgnore
    private BannerSpecification bannerSpecification;

    public UUID getObjectId() {
        return objectId;
    }

    public void setObjectId(UUID objectId) {
        this.objectId = objectId;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(LocalDateTime orderedAt) {
        this.orderedAt = orderedAt;
    }

    public BannerSpecification getBannerSpecification() {
        return bannerSpecification;
    }

    public void setBannerSpecification(BannerSpecification bannerSpecification) {
        this.bannerSpecification = bannerSpecification;
    }
}
