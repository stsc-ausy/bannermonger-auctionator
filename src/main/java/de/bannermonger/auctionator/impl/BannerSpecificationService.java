package de.bannermonger.auctionator.impl;

import java.net.URI;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bannermonger.auctionator.api.AuctionatorException;
import de.bannermonger.auctionator.api.TransformationException;
import de.bannermonger.auctionator.model.BannerSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Path("/auctionator/bannerspec")
public class BannerSpecificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BannerSpecificationService.class);

    @Autowired
    private BannerSpecificationHandler handler;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Context
    private UriInfo uriInfo;

    @POST
    @Consumes("text/plain")
    public Response postSpecification(String body) {
        LOGGER.info("Received BannerSpecification: {}", body);
        try {
            try {
                BannerSpecification bs = objectMapper.readValue(body, BannerSpecification.class);
                LOGGER.debug("Converted {} to {}", body, bs);
                handler.handleBannerSpecification(bs);
                UriBuilder ub = uriInfo.getAbsolutePathBuilder();
                URI uri = ub.path(bs.getReference()).build();
                return Response.created(uri).build();
            } catch (JsonProcessingException e) {
                throw new TransformationException("Unable to deserialize '" + body + "'", e);
            }
        } catch (AuctionatorException e) {
            LOGGER.error("Invalid banner specification", e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

}
