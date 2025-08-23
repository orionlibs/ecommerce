package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.AsRuntimeException;
import de.hybris.platform.adaptivesearch.constants.AdaptivesearchConstants;
import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsConfigurableSearchConfiguration;
import de.hybris.platform.adaptivesearch.data.AsGenericSearchProfile;
import de.hybris.platform.adaptivesearch.data.AsReference;
import de.hybris.platform.adaptivesearch.model.AsSimpleSearchProfileModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Required;

public class AsGenericSimpleSearchProfileLoadStrategy extends AbstractAsSearchProfileLoadStrategy<AsSimpleSearchProfileModel, AsGenericSearchProfile>
{
    protected static final String AVAILABLE_SEARCH_CONFIGURATIONS_QUERY = "SELECT {sc.pk}, {sc.modifiedtime} FROM {AsSimpleSearchConfiguration AS sc} WHERE {sc.searchProfile} = ?searchProfile";
    private FlexibleSearchService flexibleSearchService;
    private Converter<AsSimpleSearchProfileModel, AsGenericSearchProfile> asGenericSearchProfileConverter;


    public AsGenericSearchProfile load(AsSearchProfileContext context, AsSimpleSearchProfileModel source)
    {
        AsGenericSearchProfile target = (AsGenericSearchProfile)this.asGenericSearchProfileConverter.convert(source);
        target.setQualifierType(null);
        target.setAvailableSearchConfigurations(buildAvailableSearchConfigurations(source));
        target.setSearchConfigurations(Collections.emptyMap());
        return target;
    }


    protected Map<String, AsReference> buildAvailableSearchConfigurations(AsSimpleSearchProfileModel source)
    {
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery("SELECT {sc.pk}, {sc.modifiedtime} FROM {AsSimpleSearchConfiguration AS sc} WHERE {sc.searchProfile} = ?searchProfile", Collections.singletonMap("searchProfile", source));
        searchQuery.setResultClassList(Arrays.asList((Class<?>[][])new Class[] {PK.class, Date.class}));
        SearchResult<List<Object>> searchResult = this.flexibleSearchService.search(searchQuery);
        int searchResultCount = searchResult.getCount();
        if(searchResultCount == 0)
        {
            return Collections.emptyMap();
        }
        if(searchResultCount == 1)
        {
            List<Object> values = searchResult.getResult().get(0);
            PK pk = (PK)values.get(0);
            long version = ((Date)values.get(1)).getTime();
            AsReference searchConfigurationReference = new AsReference();
            searchConfigurationReference.setPk(pk);
            searchConfigurationReference.setVersion(version);
            return Collections.singletonMap(AdaptivesearchConstants.DEFAULT_QUALIFIER, searchConfigurationReference);
        }
        throw new AsRuntimeException("Simple search profile with pk " + source
                        .getPk() + " has more than one search configuration");
    }


    public AsGenericSearchProfile map(AsSearchProfileContext context, AsGenericSearchProfile source)
    {
        Map<String, AsConfigurableSearchConfiguration> searchConfigurations = loadSearchConfigurations(context, source);
        if(MapUtils.isEmpty(searchConfigurations))
        {
            return source;
        }
        AsGenericSearchProfile target = new AsGenericSearchProfile();
        BeanUtils.copyProperties(source, target);
        target.setSearchConfigurations(searchConfigurations);
        return target;
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


    public Converter<AsSimpleSearchProfileModel, AsGenericSearchProfile> getAsGenericSearchProfileConverter()
    {
        return this.asGenericSearchProfileConverter;
    }


    @Required
    public void setAsGenericSearchProfileConverter(Converter<AsSimpleSearchProfileModel, AsGenericSearchProfile> asGenericSearchProfileConverter)
    {
        this.asGenericSearchProfileConverter = asGenericSearchProfileConverter;
    }
}
