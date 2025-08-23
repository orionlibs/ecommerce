package de.hybris.platform.personalizationsearch.strategies.impl;

import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsMergeConfiguration;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileActivationGroup;
import de.hybris.platform.adaptivesearch.enums.AsBoostItemsMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsBoostRulesMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsFacetsMergeMode;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.services.AsSearchProfileService;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileActivationStrategy;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationsearch.data.CxSearchProfileActionResult;
import de.hybris.platform.personalizationservices.action.CxActionResultService;
import de.hybris.platform.personalizationservices.data.CxAbstractActionResult;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class CxSearchProfileActivationStrategy implements AsSearchProfileActivationStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(CxSearchProfileActivationStrategy.class);
    public static final String ACTIVATION_GROUP_ID = "personalization";
    private UserService userService;
    private AsSearchProfileService asSearchProfileService;
    private CxActionResultService cxActionResultService;


    public AsSearchProfileActivationGroup getSearchProfileActivationGroup(AsSearchProfileContext context)
    {
        List<CxSearchProfileActionResult> actionResults = collectActionResults(context);
        Map<MultiKey, List<CxSearchProfileActionResult>> groupedActionResults = groupActionResults(actionResults);
        List<AsSearchProfileActivationGroup> groups = new ArrayList<>();
        for(List<CxSearchProfileActionResult> groupActionResults : groupedActionResults.values())
        {
            List<AbstractAsSearchProfileModel> searchProfiles = findSearchProfiles(context, groupActionResults);
            if(CollectionUtils.isNotEmpty(searchProfiles))
            {
                groups.add(createGroup(searchProfiles));
            }
        }
        Collections.reverse(groups);
        AsSearchProfileActivationGroup mainGroup = createMainGroup(groups);
        return mainGroup;
    }


    protected AsSearchProfileActivationGroup createMainGroup(List<AsSearchProfileActivationGroup> groups)
    {
        AsMergeConfiguration mergeConfiguration = new AsMergeConfiguration();
        mergeConfiguration.setBoostItemsMergeMode(AsBoostItemsMergeMode.ADD_BEFORE);
        mergeConfiguration.setFacetsMergeMode(AsFacetsMergeMode.ADD_BEFORE);
        mergeConfiguration.setBoostRulesMergeMode(AsBoostRulesMergeMode.ADD);
        mergeConfiguration.setResultBoostItemsMergeMode(AsBoostItemsMergeMode.ADD_AFTER);
        mergeConfiguration.setResultFacetsMergeMode(AsFacetsMergeMode.ADD_AFTER);
        mergeConfiguration.setResultBoostRulesMergeMode(AsBoostRulesMergeMode.ADD);
        AsSearchProfileActivationGroup group = new AsSearchProfileActivationGroup();
        group.setId("personalization");
        group.setMergeConfiguration(mergeConfiguration);
        group.setSearchProfiles(Collections.emptyList());
        group.setGroups(groups);
        return group;
    }


    protected AsSearchProfileActivationGroup createGroup(List<AbstractAsSearchProfileModel> searchProfiles)
    {
        AsSearchProfileActivationGroup group = new AsSearchProfileActivationGroup();
        group.setSearchProfiles(searchProfiles);
        group.setGroups(Collections.emptyList());
        return group;
    }


    protected List<CxSearchProfileActionResult> collectActionResults(AsSearchProfileContext context)
    {
        UserModel user = this.userService.getCurrentUser();
        Collection<CatalogVersionModel> catalogVersions = context.getSessionCatalogVersions();
        if(CollectionUtils.isEmpty(catalogVersions))
        {
            return Collections.emptyList();
        }
        List<CxSearchProfileActionResult> searchProfileActionResults = new ArrayList<>();
        for(CatalogVersionModel catalogVersion : catalogVersions)
        {
            List<CxAbstractActionResult> actionResults = this.cxActionResultService.getActionResults(user, catalogVersion);
            for(CxAbstractActionResult actionResult : actionResults)
            {
                if(actionResult instanceof CxSearchProfileActionResult)
                {
                    searchProfileActionResults.add((CxSearchProfileActionResult)actionResult);
                }
            }
        }
        return searchProfileActionResults;
    }


    protected Map<MultiKey, List<CxSearchProfileActionResult>> groupActionResults(List<CxSearchProfileActionResult> actionResults)
    {
        return (Map<MultiKey, List<CxSearchProfileActionResult>>)actionResults.stream()
                        .collect(Collectors.groupingBy(this::generateGroupKey, java.util.LinkedHashMap::new, Collectors.toList()));
    }


    protected MultiKey generateGroupKey(CxSearchProfileActionResult actionResult)
    {
        return new MultiKey(actionResult.getCustomizationCode(), actionResult.getVariationCode());
    }


    protected List<AbstractAsSearchProfileModel> findSearchProfiles(AsSearchProfileContext context, List<CxSearchProfileActionResult> actionResults)
    {
        return (List<AbstractAsSearchProfileModel>)actionResults.stream().map(actionResult -> findSearchProfile(context, actionResult)).filter(Optional::isPresent)
                        .map(Optional::get).collect(Collectors.toList());
    }


    protected Optional<AbstractAsSearchProfileModel> findSearchProfile(AsSearchProfileContext context, CxSearchProfileActionResult actionResult)
    {
        Optional<CatalogVersionModel> catalogVersion = context.getSessionCatalogVersions().stream().filter(sessionCatalogVersion -> Objects.equals(actionResult.getSearchProfileCatalog(), sessionCatalogVersion.getCatalog().getId())).findFirst();
        if(!catalogVersion.isPresent())
        {
            return Optional.empty();
        }
        Optional<AbstractAsSearchProfileModel> searchProfile = this.asSearchProfileService.getSearchProfileForCode(catalogVersion.get(), actionResult.getSearchProfileCode());
        if(!searchProfile.isPresent())
        {
            return Optional.empty();
        }
        if(!Objects.equals(context.getIndexType(), ((AbstractAsSearchProfileModel)searchProfile.get()).getIndexType()))
        {
            LOG.warn("Invalid action: customization={}, variation={}, action={}, searchProfileCode={}, searchProfileCatalog={}", new Object[] {actionResult
                            .getCustomizationCode(), actionResult.getVariationCode(), actionResult.getActionCode(), actionResult
                            .getSearchProfileCode(), actionResult.getSearchProfileCatalog()});
            return Optional.empty();
        }
        return searchProfile;
    }


    public UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public AsSearchProfileService getAsSearchProfileService()
    {
        return this.asSearchProfileService;
    }


    @Required
    public void setAsSearchProfileService(AsSearchProfileService asSearchProfileService)
    {
        this.asSearchProfileService = asSearchProfileService;
    }


    public CxActionResultService getCxActionResultService()
    {
        return this.cxActionResultService;
    }


    @Required
    public void setCxActionResultService(CxActionResultService cxActionResultService)
    {
        this.cxActionResultService = cxActionResultService;
    }
}
