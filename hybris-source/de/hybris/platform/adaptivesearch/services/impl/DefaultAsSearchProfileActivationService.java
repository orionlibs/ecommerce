package de.hybris.platform.adaptivesearch.services.impl;

import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileActivationGroup;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.services.AsSearchProfileActivationService;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileActivationMapping;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileActivationStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileRegistry;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsSearchProfileActivationService implements AsSearchProfileActivationService
{
    protected static final String CURRENT_SEARCH_PROFILES = "asSearchProfiles";
    private ModelService modelService;
    private SessionService sessionService;
    private AsSearchProfileRegistry asSearchProfileRegistry;


    public void setCurrentSearchProfiles(List<AbstractAsSearchProfileModel> searchProfiles)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("searchProfiles", searchProfiles);
        List<PK> pks = (List<PK>)searchProfiles.stream().map(AbstractItemModel::getPk).collect(Collectors.toList());
        this.sessionService.setAttribute("asSearchProfiles", pks);
    }


    public Optional<List<AbstractAsSearchProfileModel>> getCurrentSearchProfiles()
    {
        List<PK> pks = (List<PK>)this.sessionService.getAttribute("asSearchProfiles");
        if(pks == null)
        {
            return Optional.empty();
        }
        Objects.requireNonNull(this.modelService);
        List<AbstractAsSearchProfileModel> searchProfiles = (List<AbstractAsSearchProfileModel>)pks.stream().map(this.modelService::get).collect(Collectors.toList());
        return Optional.of(searchProfiles);
    }


    public void clearCurrentSearchProfiles()
    {
        this.sessionService.removeAttribute("asSearchProfiles");
    }


    public List<AsSearchProfileActivationGroup> getSearchProfileActivationGroupsForContext(AsSearchProfileContext context)
    {
        Optional<List<AbstractAsSearchProfileModel>> currentSearchProfiles = getCurrentSearchProfiles();
        if(currentSearchProfiles.isPresent())
        {
            AsSearchProfileActivationGroup group = new AsSearchProfileActivationGroup();
            group.setSearchProfiles(currentSearchProfiles.get());
            return Collections.singletonList(group);
        }
        List<AsSearchProfileActivationGroup> searchProfileGroups = new ArrayList<>();
        for(AsSearchProfileActivationMapping activationMapping : this.asSearchProfileRegistry
                        .getSearchProfileActivationMappings())
        {
            AsSearchProfileActivationStrategy activationStrategy = activationMapping.getActivationStrategy();
            AsSearchProfileActivationGroup activationGroup = processSearchProfileActivationGroup(context, activationStrategy
                            .getSearchProfileActivationGroup(context));
            if(activationGroup != null)
            {
                searchProfileGroups.add(activationGroup);
            }
        }
        return searchProfileGroups;
    }


    protected AsSearchProfileActivationGroup processSearchProfileActivationGroup(AsSearchProfileContext context, AsSearchProfileActivationGroup source)
    {
        if(source == null)
        {
            return null;
        }
        List<AbstractAsSearchProfileModel> searchProfiles = (List<AbstractAsSearchProfileModel>)CollectionUtils.emptyIfNull(source.getSearchProfiles()).stream().filter(searchProfile -> canActivateSearchProfile(context, searchProfile)).collect(Collectors.toList());
        List<AsSearchProfileActivationGroup> groups = (List<AsSearchProfileActivationGroup>)CollectionUtils.emptyIfNull(source.getGroups()).stream().map(group -> processSearchProfileActivationGroup(context, group)).filter(Objects::nonNull).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(searchProfiles) && CollectionUtils.isEmpty(groups))
        {
            return null;
        }
        AsSearchProfileActivationGroup target = new AsSearchProfileActivationGroup();
        target.setId(source.getId());
        target.setMergeConfiguration(source.getMergeConfiguration());
        target.setSearchProfiles(searchProfiles);
        target.setGroups(groups);
        return target;
    }


    protected boolean canActivateSearchProfile(AsSearchProfileContext context, AbstractAsSearchProfileModel searchProfile)
    {
        if(StringUtils.isBlank(searchProfile.getQueryContext()))
        {
            return true;
        }
        return (CollectionUtils.isNotEmpty(context.getQueryContexts()) && context
                        .getQueryContexts().contains(searchProfile.getQueryContext()));
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public AsSearchProfileRegistry getAsSearchProfileRegistry()
    {
        return this.asSearchProfileRegistry;
    }


    @Required
    public void setAsSearchProfileRegistry(AsSearchProfileRegistry asSearchProfileRegistry)
    {
        this.asSearchProfileRegistry = asSearchProfileRegistry;
    }
}
