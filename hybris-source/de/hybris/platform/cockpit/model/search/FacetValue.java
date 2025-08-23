package de.hybris.platform.cockpit.model.search;

import java.util.Collection;

public interface FacetValue
{
    String getQualifier();


    int getHits();


    Collection<FacetValue> getSubvalues();
}
