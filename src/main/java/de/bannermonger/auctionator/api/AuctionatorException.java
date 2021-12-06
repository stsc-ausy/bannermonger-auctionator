package de.bannermonger.auctionator.api;

public class AuctionatorException extends RuntimeException {

    public AuctionatorException(String message) {
        super(message);
    }

    public AuctionatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuctionatorException(Throwable cause) {
        super(cause);
    }
}
