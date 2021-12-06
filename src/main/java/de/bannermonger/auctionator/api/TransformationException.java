package de.bannermonger.auctionator.api;

public class TransformationException extends AuctionatorException {

    public TransformationException(String message) {
        super(message);
    }

    public TransformationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransformationException(Throwable cause) {
        super(cause);
    }
}
