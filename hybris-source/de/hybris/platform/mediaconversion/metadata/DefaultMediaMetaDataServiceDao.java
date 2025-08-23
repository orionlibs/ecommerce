package de.hybris.platform.mediaconversion.metadata;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.model.MediaMetaDataModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Required;

public class DefaultMediaMetaDataServiceDao implements MediaMetaDataServiceDao
{
    private FlexibleSearchService flexibleSearchService;


    public Collection<MediaMetaDataModel> findMetaData(MediaModel media, String group)
    {
        Map<String, Object> params = new TreeMap<>();
        params.put("media", media);
        if(group != null)
        {
            params.put("group", group);
        }
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {pk} FROM {MediaMetaData} WHERE {media} = ?media" + ((group == null) ? "" : " AND {groupName} = ?group"), params);
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
