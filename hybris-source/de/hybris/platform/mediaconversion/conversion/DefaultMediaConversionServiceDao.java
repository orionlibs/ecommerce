package de.hybris.platform.mediaconversion.conversion;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Required;

public class DefaultMediaConversionServiceDao implements MediaConversionServiceDao
{
    private FlexibleSearchService flexibleSearchService;


    public Collection<ConversionMediaFormatModel> allConversionFormats()
    {
        SearchResult<ConversionMediaFormatModel> ret = getFlexibleSearchService().search("SELECT {pk} FROM {ConversionMediaFormat}");
        return ret.getResult();
    }


    public Collection<MediaModel> getConvertedMedias(MediaContainerModel container)
    {
        Map<String, Object> params = new TreeMap<>();
        params.put("container", container);
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {pk} FROM {Media} WHERE {mediaContainer} = ?container AND {originalDataPK} IS NOT NULL", params);
        SearchResult<MediaModel> result = getFlexibleSearchService().search(query);
        return result.getResult();
    }


    public MediaModel retrieveMaster(MediaContainerModel model)
    {
        Map<String, Object> params = new TreeMap<>();
        params.put("container", model);
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {m.pk} FROM {Media as m} WHERE {m.mediaContainer} = ?container AND {m.original} IS NULL AND {m.originalDataPK} IS NULL", params);
        return (MediaModel)getFlexibleSearchService().searchUnique(query);
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
