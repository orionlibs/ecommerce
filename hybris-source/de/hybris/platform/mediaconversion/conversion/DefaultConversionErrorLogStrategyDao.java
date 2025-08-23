package de.hybris.platform.mediaconversion.conversion;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.mediaconversion.model.ConversionErrorLogModel;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Required;

public class DefaultConversionErrorLogStrategyDao implements ConversionErrorLogStrategyDao
{
    private FlexibleSearchService flexibleSearchService;


    public Collection<ConversionErrorLogModel> findAllErrorLogs(MediaContainerModel container, ConversionMediaFormatModel format)
    {
        Map<String, Object> params = new TreeMap<>();
        params.put("container", container);
        params.put("format", format);
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {pk} FROM {ConversionErrorLog} WHERE {container} = ?container AND {targetFormat} = ?format", params);
        return getFlexibleSearchService().search(query).getResult();
    }


    public FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
