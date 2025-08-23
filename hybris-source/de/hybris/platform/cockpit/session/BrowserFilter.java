package de.hybris.platform.cockpit.session;

import de.hybris.platform.cockpit.model.search.Query;

public interface BrowserFilter
{
    String getLabel();


    boolean exclude(Object paramObject);


    void filterQuery(Query paramQuery);
}
