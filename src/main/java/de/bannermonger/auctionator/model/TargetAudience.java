package de.bannermonger.auctionator.model;

import java.util.List;
import javax.persistence.*;

@Embeddable
public class TargetAudience {

    @Enumerated(EnumType.STRING)
    @Column(name = "target_gender", nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_age_group", nullable = false)
    private AgeGroup ageGroup;

    @ElementCollection
    @CollectionTable(name = "banner_spec_topic", joinColumns = {@JoinColumn(name = "banner_spec_id")})
    @Column(name = "topic")
    private List<String> topics;

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public AgeGroup getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(AgeGroup ageGroup) {
        this.ageGroup = ageGroup;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    @Override
    public String toString() {
        return "TargetAudience{" +
                "gender=" + gender +
                ", ageGroup=" + ageGroup +
                ", topics=" + topics +
                '}';
    }
}
