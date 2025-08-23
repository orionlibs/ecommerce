package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.constants.AdaptivesearchConstants;
import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsConfigurableSearchConfiguration;
import de.hybris.platform.adaptivesearch.data.AsGenericSearchProfile;
import de.hybris.platform.adaptivesearch.data.AsReference;
import de.hybris.platform.adaptivesearch.model.AsCategoryAwareSearchProfileModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Required;

public class AsGenericCategoryAwareSearchProfileLoadStrategy extends AbstractAsSearchProfileLoadStrategy<AsCategoryAwareSearchProfileModel, AsGenericSearchProfile>
{
    protected static final String AVAILABLE_SEARCH_CONFIGURATIONS_QUERY = "SELECT {sc.pk}, {sc.modifiedtime}, {sc.uniqueIdx}, {c.code} FROM {AsCategoryAwareSearchConfiguration AS sc LEFT JOIN Category AS c ON {sc.category} = {c.pk}} WHERE {sc.searchProfile} = ?searchProfile";
    private FlexibleSearchService flexibleSearchService;
    private Converter<AsCategoryAwareSearchProfileModel, AsGenericSearchProfile> asGenericSearchProfileConverter;


    public AsGenericSearchProfile load(AsSearchProfileContext context, AsCategoryAwareSearchProfileModel source)
    {
        AsGenericSearchProfile target = (AsGenericSearchProfile)this.asGenericSearchProfileConverter.convert(source);
        target.setQualifierType("category");
        target.setAvailableSearchConfigurations(buildAvailableSearchConfigurations(source));
        target.setSearchConfigurations(Collections.emptyMap());
        return target;
    }


    protected Map<String, AsReference> buildAvailableSearchConfigurations(AsCategoryAwareSearchProfileModel source)
    {
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery("SELECT {sc.pk}, {sc.modifiedtime}, {sc.uniqueIdx}, {c.code} FROM {AsCategoryAwareSearchConfiguration AS sc LEFT JOIN Category AS c ON {sc.category} = {c.pk}} WHERE {sc.searchProfile} = ?searchProfile",
                        Collections.singletonMap("searchProfile", source));
        searchQuery.setResultClassList(Arrays.asList((Class<?>[][])new Class[] {PK.class, Date.class, String.class, String.class}));
        SearchResult<List<Object>> searchResult = this.flexibleSearchService.search(searchQuery);
        int searchResultCount = searchResult.getCount();
        if(searchResultCount == 0)
        {
            return Collections.emptyMap();
        }
        Map<String, AsReference> target = new HashMap<>();
        for(List<Object> values : (Iterable<List<Object>>)searchResult.getResult())
        {
            PK pk = (PK)values.get(0);
            long version = ((Date)values.get(1)).getTime();
            String uniqueIdx = (String)values.get(2);
            String qualifier = (String)values.get(3);
            if(isValidSearchConfiguration(qualifier, uniqueIdx))
            {
                AsReference searchConfigurationReference = new AsReference();
                searchConfigurationReference.setPk(pk);
                searchConfigurationReference.setVersion(version);
                target.put((qualifier == null) ? AdaptivesearchConstants.DEFAULT_QUALIFIER : qualifier, searchConfigurationReference);
            }
        }
        return target;
    }


    protected boolean isValidSearchConfiguration(String qualifier, String uniqueIdx)
    {
        if(qualifier != null)
        {
            return true;
        }
        return StringUtils.equals("null", StringUtils.substringAfterLast(uniqueIdx, "_"));
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


    public Converter<AsCategoryAwareSearchProfileModel, AsGenericSearchProfile> getAsGenericSearchProfileConverter()
    {
        return this.asGenericSearchProfileConverter;
    }


    @Required
    public void setAsGenericSearchProfileConverter(Converter<AsCategoryAwareSearchProfileModel, AsGenericSearchProfile> asGenericSearchProfileConverter)
    {
        this.asGenericSearchProfileConverter = asGenericSearchProfileConverter;
    }
}
