/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.ymkt.recommendationaddon.facades;

import com.hybris.ymkt.recommendation.dao.OfferInteractionContext;
import com.hybris.ymkt.recommendation.dao.OfferRecommendation;
import com.hybris.ymkt.recommendation.dao.OfferRecommendationContext;
import com.hybris.ymkt.recommendation.services.OfferDiscoveryService;
import com.hybris.ymkt.recommendation.services.OfferInteractionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

/**
 * Facade for offer recommendation controller.
 */
public class OfferRecommendationManagerFacade
{
    protected OfferDiscoveryService offerDiscoveryService;
    protected OfferInteractionService offerInteractionService;


    /**
     * Read {@link OfferRecommendation}s according to the {@link OfferRecommendationContext}.
     *
     * @param context
     *           {@link OfferRecommendationContext}
     * @return {@link List} of {@link OfferRecommendation}
     */
    public List<OfferRecommendation> getOfferRecommendations(final OfferRecommendationContext context)
    {
        return offerDiscoveryService.getOfferRecommendations(context);
    }


    /**
     * Persist {@link OfferInteractionContext}.
     *
     * @param offerInteractionContext
     *           {@link OfferInteractionContext} to be persisted.
     */
    public void saveOfferInteraction(final OfferInteractionContext offerInteractionContext)
    {
        offerInteractionService.saveOfferInteraction(offerInteractionContext);
    }


    @Required
    public void setOfferDiscoveryService(OfferDiscoveryService offerDiscoveryService)
    {
        this.offerDiscoveryService = offerDiscoveryService;
    }


    @Required
    public void setOfferInteractionService(OfferInteractionService offerInteractionService)
    {
        this.offerInteractionService = offerInteractionService;
    }
}
