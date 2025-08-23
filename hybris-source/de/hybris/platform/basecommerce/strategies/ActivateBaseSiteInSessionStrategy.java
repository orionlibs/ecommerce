package de.hybris.platform.basecommerce.strategies;

public interface ActivateBaseSiteInSessionStrategy<T extends de.hybris.platform.basecommerce.model.site.BaseSiteModel>
{
    void activate(T paramT);
}
