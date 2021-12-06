package de.bannermonger.auctionator;

import de.bannermonger.auctionator.impl.BannerSpecificationService;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableJms
@EnableJpaRepositories
@EnableScheduling
class AuctionatorConfig extends ResourceConfig {

    public AuctionatorConfig() {
        super();
        register(BannerSpecificationService.class);
    }

}
