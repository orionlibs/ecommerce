package de.hybris.platform.adaptivesearch.util;

public interface ContextAwarePopulator<S, T, C>
{
    void populate(S paramS, T paramT, C paramC);
}
