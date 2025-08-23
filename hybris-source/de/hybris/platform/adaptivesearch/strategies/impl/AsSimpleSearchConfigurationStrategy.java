package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsSearchConfigurationInfoData;
import de.hybris.platform.adaptivesearch.model.AsSimpleSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsSimpleSearchProfileModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

public class AsSimpleSearchConfigurationStrategy extends AbstractAsSearchConfigurationStrategy<AsSimpleSearchProfileModel, AsSimpleSearchConfigurationModel>
{
    protected static final String CONTEXT_TYPE_KEY = "adaptivesearch.simplesearchprofile.contexttype";
    protected static final String CONTEXT_DESCRIPTION_KEY = "adaptivesearch.simplesearchprofile.contextdescription";


    public Optional<AsSimpleSearchConfigurationModel> getForContext(AsSearchProfileContext context, AsSimpleSearchProfileModel searchProfile)
    {
        Map<String, Object> filters = new HashMap<>();
        filters.put("searchProfile", searchProfile);
        List<AsSimpleSearchConfigurationModel> searchConfigurations = getAsSearchConfigurationDao().findSearchConfigurations(AsSimpleSearchConfigurationModel.class, filters);
        if(CollectionUtils.isEmpty(searchConfigurations))
        {
            return Optional.empty();
        }
        List<AsSimpleSearchConfigurationModel> validSearchConfigurations = (List<AsSimpleSearchConfigurationModel>)searchConfigurations.stream().filter(searchConfiguration -> !searchConfiguration.isCorrupted()).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(validSearchConfigurations))
        {
            return Optional.empty();
        }
        if(validSearchConfigurations.size() > 1)
        {
            throw new AmbiguousIdentifierException("More than one search configuration found");
        }
        return Optional.of(validSearchConfigurations.get(0));
    }


    public AsSimpleSearchConfigurationModel getOrCreateForContext(AsSearchProfileContext context, AsSimpleSearchProfileModel searchProfile)
    {
        Optional<AsSimpleSearchConfigurationModel> searchConfiguration = getForContext(context, searchProfile);
        if(searchConfiguration.isPresent())
        {
            return searchConfiguration.get();
        }
        return createSearchConfiguration(searchProfile);
    }


    public AsSearchConfigurationInfoData getInfoForContext(AsSearchProfileContext context, AsSimpleSearchProfileModel searchProfile)
    {
        String contextType = getL10nService().getLocalizedString("adaptivesearch.simplesearchprofile.contexttype");
        String contextLabel = buildContextLabel();
        String contextDescription = getL10nService().getLocalizedString("adaptivesearch.simplesearchprofile.contextdescription");
        AsSearchConfigurationInfoData searchConfigurationInfo = new AsSearchConfigurationInfoData();
        searchConfigurationInfo.setType("AsSimpleSearchProfile");
        searchConfigurationInfo.setContextType(contextType);
        searchConfigurationInfo.setContextLabel(contextLabel);
        searchConfigurationInfo.setContextDescription(contextDescription);
        return searchConfigurationInfo;
    }


    protected String buildContextLabel()
    {
        return getL10nService().getLocalizedString("adaptivesearch.globalcategory");
    }


    protected AsSimpleSearchConfigurationModel createSearchConfiguration(AsSimpleSearchProfileModel searchProfile)
    {
        AsSimpleSearchConfigurationModel searchConfiguration = (AsSimpleSearchConfigurationModel)getModelService().create(AsSimpleSearchConfigurationModel.class);
        searchConfiguration.setSearchProfile(searchProfile);
        searchConfiguration.setCatalogVersion(searchProfile.getCatalogVersion());
        return searchConfiguration;
    }


    public Set<String> getQualifiers(AsSimpleSearchProfileModel searchProfile)
    {
        Set<String> set = new HashSet<>();
        set.add(null);
        return set;
    }
}
