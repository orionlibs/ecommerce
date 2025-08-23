package de.hybris.platform.adaptivesearch.strategies;

public interface AsCloneStrategy
{
    <T extends de.hybris.platform.core.model.ItemModel> T clone(T paramT);
}
