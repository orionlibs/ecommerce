package de.hybris.platform.site.impl;

import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.services.DestinationService;
import de.hybris.platform.apiregistryservices.strategies.ConsumedDestinationLocatorStrategy;
import de.hybris.platform.site.BaseSiteService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultBaseSiteConsumedDestinationLocatorStrategy implements ConsumedDestinationLocatorStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBaseSiteConsumedDestinationLocatorStrategy.class);
    public static final String BASE_SITE = "baseSite";
    private DestinationService<AbstractDestinationModel> destinationService;
    private BaseSiteService baseSiteService;


    public ConsumedDestinationModel lookup(String clientTypeName)
    {
        List<AbstractDestinationModel> destinations = getDestinationService().getAllDestinations();
        String currentBaseSiteId = getCurrentBaseSiteId();
        Objects.requireNonNull(ConsumedDestinationModel.class);
        Optional<AbstractDestinationModel> destination = destinations.stream().filter(ConsumedDestinationModel.class::isInstance).filter(dest -> (dest.getAdditionalProperties().containsKey("clientClassName") && ((String)dest.getAdditionalProperties().get("clientClassName")).equals(clientTypeName)))
                        .filter(dest -> dest.getAdditionalProperties().containsKey("baseSite") ? ((String)dest.getAdditionalProperties().get("baseSite")).equals(currentBaseSiteId) : ((currentBaseSiteId == null))).findFirst();
        if(!destination.isPresent())
        {
            LOG.warn("Failed to find consumed destination for the given id [{}] and the current base site [{}]", clientTypeName, currentBaseSiteId);
            return null;
        }
        return (ConsumedDestinationModel)destination.get();
    }


    protected String getCurrentBaseSiteId()
    {
        return (getBaseSiteService().getCurrentBaseSite() != null) ? getBaseSiteService().getCurrentBaseSite().getUid() : null;
    }


    protected DestinationService<AbstractDestinationModel> getDestinationService()
    {
        return this.destinationService;
    }


    @Required
    public void setDestinationService(DestinationService<AbstractDestinationModel> destinationService)
    {
        this.destinationService = destinationService;
    }


    protected BaseSiteService getBaseSiteService()
    {
        return this.baseSiteService;
    }


    @Required
    public void setBaseSiteService(BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }
}
