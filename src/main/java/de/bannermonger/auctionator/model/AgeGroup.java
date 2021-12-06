package de.bannermonger.auctionator.model;

public enum AgeGroup {

    ALL(12, 99),
    ADOLESCENT(12,21),
    YOUNG_ADULTS(18, 38),
    BEST_AGER(35, 55),
    SILVER_SURFER(55, 99);

    private int minAge;

    private int maxAge;

    AgeGroup(int minAge, int maxAge) {
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public int getMinAge() {
        return minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }
}
