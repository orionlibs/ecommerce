package de.hybris.platform.adaptivesearch.strategies;

import java.io.Serializable;

public interface AsCacheKey extends Serializable
{
    AsCacheScope getScope();
}
