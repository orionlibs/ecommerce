package de.hybris.platform.servicelayer.search.paginated;

import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.genericsearch.GenericSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

public interface AbstractQueryHelper
{
    <T extends FlexibleSearchQuery> T getUpdatedFlexibleSearchQuery(FlexibleSearchQuery paramFlexibleSearchQuery, String paramString);


    <T extends GenericSearchQuery> T getUpdatedGenericSearchQuery(GenericSearchQuery paramGenericSearchQuery, GenericQuery paramGenericQuery);
}
