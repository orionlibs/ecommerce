package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsSearchConfigurationInfoData;
import de.hybris.platform.adaptivesearch.model.AsCategoryAwareSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsCategoryAwareSearchProfileModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

public class AsCategoryAwareSearchConfigurationStrategy extends AbstractAsSearchConfigurationStrategy<AsCategoryAwareSearchProfileModel, AsCategoryAwareSearchConfigurationModel>
{
    protected static final String CONTEXT_TYPE_KEY = "adaptivesearch.categoryawaresearchprofile.contexttype";
    protected static final String CONTEXT_DESCRIPTION_KEY = "adaptivesearch.categoryawaresearchprofile.contextdescription";


    public Optional<AsCategoryAwareSearchConfigurationModel> getForContext(AsSearchProfileContext context, AsCategoryAwareSearchProfileModel searchProfile)
    {
        CategoryModel category = resolveCategory(context);
        Map<String, Object> filters = new HashMap<>();
        filters.put("searchProfile", searchProfile);
        filters.put("category", category);
        List<AsCategoryAwareSearchConfigurationModel> searchConfigurations = getAsSearchConfigurationDao().findSearchConfigurations(AsCategoryAwareSearchConfigurationModel.class, filters);
        if(CollectionUtils.isEmpty(searchConfigurations))
        {
            return Optional.empty();
        }
        List<AsCategoryAwareSearchConfigurationModel> validSearchConfigurations = (List<AsCategoryAwareSearchConfigurationModel>)searchConfigurations.stream().filter(searchConfiguration -> !searchConfiguration.isCorrupted()).collect(Collectors.toList());
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


    public AsCategoryAwareSearchConfigurationModel getOrCreateForContext(AsSearchProfileContext context, AsCategoryAwareSearchProfileModel searchProfile)
    {
        Optional<AsCategoryAwareSearchConfigurationModel> searchConfiguration = getForContext(context, searchProfile);
        if(searchConfiguration.isPresent())
        {
            return searchConfiguration.get();
        }
        return createSearchConfiguration(context, searchProfile);
    }


    protected AsCategoryAwareSearchConfigurationModel createSearchConfiguration(AsSearchProfileContext context, AsCategoryAwareSearchProfileModel searchProfile)
    {
        CategoryModel category = resolveCategory(context);
        AsCategoryAwareSearchConfigurationModel searchConfiguration = (AsCategoryAwareSearchConfigurationModel)getModelService().create(AsCategoryAwareSearchConfigurationModel.class);
        searchConfiguration.setSearchProfile(searchProfile);
        searchConfiguration.setCatalogVersion(searchProfile.getCatalogVersion());
        searchConfiguration.setCategory(category);
        return searchConfiguration;
    }


    public AsSearchConfigurationInfoData getInfoForContext(AsSearchProfileContext context, AsCategoryAwareSearchProfileModel searchProfile)
    {
        String contextType = getL10nService().getLocalizedString("adaptivesearch.categoryawaresearchprofile.contexttype");
        String contextLabel = buildContextLabel(context);
        String contextDescription = getL10nService().getLocalizedString("adaptivesearch.categoryawaresearchprofile.contextdescription");
        AsSearchConfigurationInfoData searchConfigurationInfo = new AsSearchConfigurationInfoData();
        searchConfigurationInfo.setType("AsCategoryAwareSearchProfile");
        searchConfigurationInfo.setContextType(contextType);
        searchConfigurationInfo.setContextLabel(contextLabel);
        searchConfigurationInfo.setContextDescription(contextDescription);
        return searchConfigurationInfo;
    }


    protected String buildContextLabel(AsSearchProfileContext context)
    {
        String globalCategory = getL10nService().getLocalizedString("adaptivesearch.globalcategory");
        StringJoiner contextLabel = new StringJoiner(" / ");
        contextLabel.add(globalCategory);
        if(CollectionUtils.isNotEmpty(context.getCategoryPath()))
        {
            for(CategoryModel category : context.getCategoryPath())
            {
                if(StringUtils.isNotBlank(category.getName()))
                {
                    contextLabel.add(category.getName());
                    continue;
                }
                contextLabel.add("[" + category.getCode() + "]");
            }
        }
        return contextLabel.toString();
    }


    protected CategoryModel resolveCategory(AsSearchProfileContext context)
    {
        List<CategoryModel> categoryPath = context.getCategoryPath();
        if(CollectionUtils.isNotEmpty(categoryPath))
        {
            return categoryPath.get(categoryPath.size() - 1);
        }
        return null;
    }


    public Set<String> getQualifiers(AsCategoryAwareSearchProfileModel searchProfile)
    {
        Set<String> qualifiers = new HashSet<>();
        for(AsCategoryAwareSearchConfigurationModel conf : searchProfile.getSearchConfigurations())
        {
            if(conf.getCategory() != null)
            {
                qualifiers.add(conf.getCategory().getCode());
                continue;
            }
            qualifiers.add(null);
        }
        return qualifiers;
    }
}
