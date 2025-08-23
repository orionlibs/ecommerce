package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.daos.AsSearchProfileActivationSetDao;
import de.hybris.platform.adaptivesearch.data.AsSearchProfileActivationGroup;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.model.AsSearchProfileActivationSetModel;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileActivationStrategy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsSearchProfileActivationStrategy implements AsSearchProfileActivationStrategy
{
    public static final String ACTIVATION_GROUP_ID = "default";
    private AsSearchProfileActivationSetDao asSearchProfileActivationSetDao;


    public AsSearchProfileActivationGroup getSearchProfileActivationGroup(AsSearchProfileContext context)
    {
        List<AbstractAsSearchProfileModel> activeSearchProfiles = collectActiveSearchProfiles(context);
        AsSearchProfileActivationGroup group = new AsSearchProfileActivationGroup();
        group.setId("default");
        group.setSearchProfiles(activeSearchProfiles);
        return group;
    }


    protected List<AbstractAsSearchProfileModel> collectActiveSearchProfiles(AsSearchProfileContext context)
    {
        List<AsSearchProfileActivationSetModel> activationSets = getActivationSets(context);
        if(CollectionUtils.isEmpty(activationSets))
        {
            return Collections.emptyList();
        }
        return (List<AbstractAsSearchProfileModel>)activationSets.stream().sorted(this::compareActivationSets)
                        .flatMap(activationSet -> activationSet.getSearchProfiles().stream()).collect(Collectors.toList());
    }


    protected List<AsSearchProfileActivationSetModel> getActivationSets(AsSearchProfileContext context)
    {
        List<AsSearchProfileActivationSetModel> activationSets = new ArrayList<>();
        Optional<AsSearchProfileActivationSetModel> activationSetResult = this.asSearchProfileActivationSetDao.findSearchProfileActivationSetByIndexType(null, context.getIndexType());
        Objects.requireNonNull(activationSets);
        activationSetResult.ifPresent(activationSets::add);
        if(CollectionUtils.isNotEmpty(context.getCatalogVersions()))
        {
            List<AsSearchProfileActivationSetModel> searchProfileActivationSets = this.asSearchProfileActivationSetDao.findSearchProfileActivationSetsByCatalogVersionsAndIndexType(context.getCatalogVersions(), context
                            .getIndexType());
            activationSets.addAll(searchProfileActivationSets);
        }
        return activationSets;
    }


    protected int compareActivationSets(AsSearchProfileActivationSetModel activationSet1, AsSearchProfileActivationSetModel activationSet2)
    {
        return activationSet2.getPriority().compareTo(activationSet1.getPriority());
    }


    public AsSearchProfileActivationSetDao getAsSearchProfileActivationSetDao()
    {
        return this.asSearchProfileActivationSetDao;
    }


    @Required
    public void setAsSearchProfileActivationSetDao(AsSearchProfileActivationSetDao asSearchProfileActivationSetDao)
    {
        this.asSearchProfileActivationSetDao = asSearchProfileActivationSetDao;
    }
}
