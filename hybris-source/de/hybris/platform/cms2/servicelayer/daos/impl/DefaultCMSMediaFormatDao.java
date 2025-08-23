package de.hybris.platform.cms2.servicelayer.daos.impl;

import com.google.common.base.Strings;
import de.hybris.platform.cms2.servicelayer.daos.CMSMediaFormatDao;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import java.util.stream.Collectors;

public class DefaultCMSMediaFormatDao extends AbstractItemDao implements CMSMediaFormatDao
{
    public Collection<MediaFormatModel> getAllMediaFormats()
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT {").append("pk").append("} ");
        query.append("FROM {").append("MediaFormat").append("}");
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
        SearchResult<MediaFormatModel> result = search(fQuery);
        return result.getResult();
    }


    public MediaFormatModel getMediaFormatByQualifier(String qualifier) throws IllegalArgumentException, UnknownIdentifierException, AmbiguousIdentifierException
    {
        ServicesUtil.validateParameterNotNull(qualifier, "Qualifier cannot be null");
        StringBuilder query = new StringBuilder();
        query.append("SELECT {").append("pk").append("} ");
        query.append("FROM {").append("MediaFormat").append("} ");
        query.append("WHERE {").append("qualifier").append("} = ?").append("qualifier");
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
        fQuery.addQueryParameter("qualifier", qualifier);
        SearchResult<MediaFormatModel> result = search(fQuery);
        ServicesUtil.validateIfSingleResult(result.getResult(), "No media format with given qualifier [" + qualifier + "] was found", "More than one media format with given qualifier [" + qualifier + "] was found");
        return result.getResult().iterator().next();
    }


    public Collection<MediaFormatModel> getMediaFormatsByQualifiers(Collection<String> qualifiers) throws IllegalArgumentException
    {
        ServicesUtil.validateParameterNotNull(qualifiers, "Qualifiers cannot be null");
        String condition = qualifiers.stream().filter(qualifier -> !Strings.isNullOrEmpty(qualifier)).map(qualifier -> "'" + qualifier + "'").collect(Collectors.joining(","));
        StringBuilder query = new StringBuilder();
        query.append("SELECT {").append("pk").append("} ");
        query.append("FROM {").append("MediaFormat").append("} ");
        query.append("WHERE {").append("qualifier").append("} IN (" + condition + ")");
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
        SearchResult<MediaFormatModel> result = search(fQuery);
        return result.getResult();
    }
}
