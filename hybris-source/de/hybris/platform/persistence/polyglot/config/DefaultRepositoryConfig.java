package de.hybris.platform.persistence.polyglot.config;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.persistence.polyglot.ItemStateRepository;
import de.hybris.platform.persistence.polyglot.PolyglotPersistenceException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DefaultRepositoryConfig implements RepositoryConfig
{
    private final Map<Integer, Set<MoreSpecificCondition>> typesConfig;
    private final ItemStateRepository repository;


    public DefaultRepositoryConfig(Map<Integer, Set<MoreSpecificCondition>> typesConfig, ItemStateRepository repository)
    {
        this.typesConfig = typesConfig;
        this.repository = repository;
    }


    public PolyglotRepoSupportType isSupportedBy(TypeInfo typeInfo)
    {
        if(!this.typesConfig.containsKey(Integer.valueOf(typeInfo.getTypeCode())))
        {
            return PolyglotRepoSupportType.NONE;
        }
        Set<MoreSpecificCondition> configSpecificConditions = this.typesConfig.get(Integer.valueOf(typeInfo.getTypeCode()));
        if(configSpecificConditions.isEmpty())
        {
            return PolyglotRepoSupportType.FULL;
        }
        return checkSpecificConditions(typeInfo, configSpecificConditions);
    }


    private PolyglotRepoSupportType checkSpecificConditions(TypeInfo typeInfo, Set<MoreSpecificCondition> configSpecificConditions)
    {
        if(typeInfo.isMoreSpecificUnpredictable())
        {
            return PolyglotRepoSupportType.PARTIAL;
        }
        List<MoreSpecificCondition> eligibleItems = (List<MoreSpecificCondition>)configSpecificConditions.stream().filter(moreSpecificCondition -> (!moreSpecificCondition.getSourceIdentity().isKnown() || moreSpecificCondition.getSourceIdentity().possiblyMatches(typeInfo.getIdentity())))
                        .collect(Collectors.toList());
        if(eligibleItems.isEmpty())
        {
            return PolyglotRepoSupportType.NONE;
        }
        List<PolyglotRepoSupportType> supportTypeList = getSupportTypeListForEligibleItems(typeInfo, eligibleItems);
        if(supportTypeList.size() == 1)
        {
            if(supportTypeList.get(0) == PolyglotRepoSupportType.FULL)
            {
                areAllItemsTheSameInstance(typeInfo, eligibleItems);
            }
            return supportTypeList.get(0);
        }
        return PolyglotRepoSupportType.PARTIAL;
    }


    private List<PolyglotRepoSupportType> getSupportTypeListForEligibleItems(TypeInfo typeInfo, List<MoreSpecificCondition> eligibleItems)
    {
        boolean itemWasPresent = false;
        boolean otherAttributesMayExist = (eligibleItems.size() > 1);
        Map<String, PolyglotRepoSupportType> results = new HashMap<>();
        PolyglotRepoSupportType firstExistingItemSupportType = null;
        for(int i = 1; i <= eligibleItems.size(); i++)
        {
            MoreSpecificCondition specificCondition = eligibleItems.get(i - 1);
            TypeInfo moreSpecificTypeInfo = typeInfo.getMoreSpecificTypeInfo(specificCondition
                            .getQualifier(), otherAttributesMayExist);
            Optional<PolyglotRepoSupportType> polyglotRepoSupportType = getRepoSupportTypeForGivenItem(moreSpecificTypeInfo);
            if(polyglotRepoSupportType.isPresent())
            {
                itemWasPresent = true;
                firstExistingItemSupportType = getPolyglotRepoSupportType(firstExistingItemSupportType, polyglotRepoSupportType
                                .get());
            }
            else if(i == eligibleItems.size() - 1 && !itemWasPresent)
            {
                otherAttributesMayExist = false;
            }
            polyglotRepoSupportType.ifPresent(supportType -> results.compute(specificCondition.getQualifier(), ()));
        }
        return (List<PolyglotRepoSupportType>)results.values().stream().distinct().collect(Collectors.toList());
    }


    private PolyglotRepoSupportType getPolyglotRepoSupportType(PolyglotRepoSupportType firstExistingItemSupportType, PolyglotRepoSupportType polyglotRepoSupportType)
    {
        if(firstExistingItemSupportType == null)
        {
            firstExistingItemSupportType = polyglotRepoSupportType;
        }
        else if(firstExistingItemSupportType != polyglotRepoSupportType)
        {
            throw new PolyglotPersistenceException("Items are not allowed to have attributes with different repository support type");
        }
        return firstExistingItemSupportType;
    }


    private void areAllItemsTheSameInstance(TypeInfo typeInfo, List<MoreSpecificCondition> eligibleItems)
    {
        Item previousItem = null;
        for(MoreSpecificCondition item : eligibleItems)
        {
            TypeInfo itemTypeInfo = typeInfo.getMoreSpecificTypeInfo(item.getQualifier(), true);
            if(itemTypeInfo != null)
            {
                if(!(itemTypeInfo.getTypeItem() instanceof Item))
                {
                    return;
                }
                if(itemTypeInfo.getTypeItem() != previousItem && previousItem != null)
                {
                    throw new PolyglotPersistenceException("Items are not allowed to have different parents set at the same time");
                }
                previousItem = (Item)itemTypeInfo.getTypeItem();
            }
        }
    }


    private Optional<PolyglotRepoSupportType> getRepoSupportTypeForGivenItem(TypeInfo moreSpecificTypeInfo)
    {
        Optional<PolyglotRepoSupportType> polyglotRepoSupportType;
        if(moreSpecificTypeInfo == null)
        {
            polyglotRepoSupportType = Optional.empty();
        }
        else
        {
            polyglotRepoSupportType = Optional.<PolyglotRepoSupportType>ofNullable(isSupportedBy(moreSpecificTypeInfo)).or(() -> Optional.of(PolyglotRepoSupportType.NONE));
        }
        return polyglotRepoSupportType;
    }


    public ItemStateRepository getRepository()
    {
        return this.repository;
    }


    public Map<Integer, Set<MoreSpecificCondition>> getConditions()
    {
        return (Map<Integer, Set<MoreSpecificCondition>>)ImmutableMap.copyOf(this.typesConfig);
    }
}
