package de.bannermonger.auctionator.model;

public enum BannerSize {
    MEDIUM(300, 250),
    LEADERBOARD(728, 90),
    SKYSCRAPER(160, 600),
    HALFPAGE(300, 600),
    BILLBOARD(970, 250),
    PORTRAIT(300, 1050),
    SQUARE(250, 250);

    private int width;

    private int height;

    BannerSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
