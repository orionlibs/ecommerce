package de.hybris.platform.servicelayer.media.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.DerivedMediaModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Required;

public class DefaultMediaDao implements MediaDao
{
    private FlexibleSearchService flexibleSearchService;


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public List<MediaFolderModel> findFolder(String qualifier)
    {
        return findMediaFolderByQualifier(qualifier);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public List<MediaFormatModel> findFormat(String qualifier)
    {
        return findMediaFormatByQualifier(qualifier);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public List<MediaModel> findMedia(CatalogVersionModel catalogVersion, String code)
    {
        return findMediaByCode(catalogVersion, code);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public List<MediaModel> findMedia(String code)
    {
        return findMediaByCode(code);
    }


    public List<MediaModel> findMediaByCode(CatalogVersionModel catalogVersion, String code)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("catalogVersion", catalogVersion);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT {").append("pk").append("} ");
        builder.append("FROM {").append("Media").append("} ");
        builder.append("WHERE {").append("CatalogVersion").append("}=?catalogVersion ");
        builder.append("AND {").append("code").append("}=?code ");
        builder.append("ORDER BY {").append("pk").append("} ASC");
        SearchResult<MediaModel> result = this.flexibleSearchService.search(builder.toString(), params);
        return result.getResult();
    }


    public List<MediaModel> findMediaByCode(String code)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT {").append("pk").append("} ");
        builder.append("FROM {").append("Media").append("} ");
        builder.append("WHERE {").append("code").append("}=?code ");
        builder.append("ORDER BY {").append("pk").append("} ASC");
        SearchResult<MediaModel> result = this.flexibleSearchService.search(builder.toString(), params);
        return result.getResult();
    }


    public List<MediaFolderModel> findMediaFolderByQualifier(String qualifier)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("qualifier", qualifier);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT {").append("pk").append("} ");
        builder.append("FROM {").append("MediaFolder").append("} ");
        builder.append("WHERE {").append("qualifier").append("}=?qualifier ");
        builder.append("ORDER BY {").append("pk").append("} ASC");
        SearchResult<MediaFolderModel> result = this.flexibleSearchService.search(builder.toString(), params);
        return result.getResult();
    }


    public List<MediaFormatModel> findMediaFormatByQualifier(String qualifier)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("qualifier", qualifier);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT {").append("pk").append("} ");
        builder.append("FROM {").append("MediaFormat").append("} ");
        builder.append("WHERE {").append("qualifier").append("}=?qualifier ");
        builder.append("ORDER BY {").append("pk").append("} ASC");
        SearchResult<MediaFormatModel> result = this.flexibleSearchService.search(builder.toString(), params);
        return result.getResult();
    }


    public MediaModel findMediaByFormat(MediaContainerModel container, MediaFormatModel format)
    {
        try
        {
            Map<String, Object> params = new TreeMap<>();
            params.put("container", container);
            params.put("format", format);
            FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {pk} FROM {Media} WHERE {mediaContainer} = ?container AND {mediaFormat} = ?format", params);
            return (MediaModel)getFlexibleSearchService().searchUnique(query);
        }
        catch(AmbiguousIdentifierException e)
        {
            throw new ModelNotFoundException("Data inconsistency: Multiple medias with format '" + format + "' reside in container '" + container + "'.", e);
        }
    }


    public List<MediaModel> findForeignDataOwnerdByMedia(MediaModel media)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT {").append("pk").append("} FROM ").append("Media");
        builder.append("WHERE {").append("pk").append("}<>?mediaPk");
        return Collections.EMPTY_LIST;
    }


    public List<DerivedMediaModel> findMediaVersion(MediaModel baseMedia, String versionId)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT {dm.").append("pk").append("} ");
        builder.append("FROM {").append("DerivedMedia").append(" AS dm}");
        builder.append("WHERE {dm.").append("version").append("}=?version ");
        builder.append("AND {dm.").append("media").append("}=?media");
        Map<String, Object> params = new HashMap<>();
        params.put("version", versionId);
        params.put("media", baseMedia);
        SearchResult<DerivedMediaModel> result = this.flexibleSearchService.search(builder.toString(), params);
        return result.getResult();
    }
}
