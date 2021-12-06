package de.bannermonger.auctionator.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "banner_quote_request")
public class BannerQuoteRequest implements Serializable {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @JsonIgnore
    private UUID objectId;

    @Column(name = "reference", nullable = false)
    private String reference;

    @Column(name = "image_width", nullable = false)
    private int width;

    @Column(name = "image_height", nullable = false)
    private int height;

    @Column(name = "min_page_views", nullable = false)
    private BigInteger minPageViews;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "min_age", nullable = false)
    private int minAge;

    @Column(name = "max_age", nullable = false)
    private int maxAge;

    @Column(name = "tags")
    private String tags;

    @Column(name = "deadline", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime deadLine;

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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public BigInteger getMinPageViews() {
        return minPageViews;
    }

    public void setMinPageViews(BigInteger minPageViews) {
        this.minPageViews = minPageViews;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public LocalDateTime getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDateTime deadLine) {
        this.deadLine = deadLine;
    }

    public BannerSpecification getBannerSpecification() {
        return bannerSpecification;
    }

    public void setBannerSpecification(BannerSpecification bannerSpecification) {
        this.bannerSpecification = bannerSpecification;
    }

    @Override
    public String toString() {
        return "BannerQuoteRequest{" +
                "objectId=" + objectId +
                ", reference='" + reference + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", minPageViews=" + minPageViews +
                ", gender=" + gender +
                ", minAge=" + minAge +
                ", maxAge=" + maxAge +
                ", tags='" + tags + '\'' +
                ", deadLine=" + deadLine +
                '}';
    }
}
