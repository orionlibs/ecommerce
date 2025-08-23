package de.hybris.platform.ruleengineservices.rao.providers.impl;

import com.google.common.collect.Sets;
import de.hybris.platform.regioncache.ConcurrentHashSet;
import de.hybris.platform.ruleengineservices.rao.providers.ExpandedRAOProvider;
import de.hybris.platform.ruleengineservices.rao.providers.RAOFactsExtractor;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.SetUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractExpandedRAOProvider<T, R> implements ExpandedRAOProvider<T>, InitializingBean
{
    protected Collection<String> validOptions;
    protected Collection<String> defaultOptions;
    protected Collection<String> minOptions;
    private List<RAOFactsExtractor> factExtractorList;
    private Map<String, BiConsumer<Set<Object>, R>> consumerMap;


    public Set expandFactModel(T modelFact)
    {
        return expandFactModel(modelFact, getDefaultOptions());
    }


    public Set expandFactModel(T modelFact, Collection<String> options)
    {
        Collection<String> filteredOptions = getFilteredOptions(options);
        R raoFact = createRAO(modelFact);
        Set<Object> expandedFactsSet = expandRAO(raoFact, filteredOptions);
        populateRaoFactsExtractorConsumers();
        addExtraRAOFacts(expandedFactsSet, raoFact, filteredOptions);
        return expandedFactsSet;
    }


    public void afterPropertiesSet() throws Exception
    {
        setConsumerMap(new ConcurrentHashMap<>());
    }


    protected void populateRaoFactsExtractorConsumers()
    {
        List<RAOFactsExtractor> myFactExtractorList = getFactExtractorList();
        if(CollectionUtils.isNotEmpty(myFactExtractorList))
        {
            Map<String, BiConsumer<Set<Object>, R>> myConsumerMap = getConsumerMap();
            myFactExtractorList.stream().filter(e -> !myConsumerMap.containsKey(e.getTriggeringOption()))
                            .forEach(this::addOptionConsumers);
        }
    }


    protected void addExtraRAOFacts(Set expandedFactsSet, R raoFact, Collection<String> filteredOptions)
    {
        Map<String, BiConsumer<Set<Object>, R>> myConsumerMap = getConsumerMap();
        if(MapUtils.isNotEmpty(myConsumerMap) && CollectionUtils.isNotEmpty(filteredOptions))
        {
            Objects.requireNonNull(myConsumerMap);
            filteredOptions.stream().filter(myConsumerMap::containsKey)
                            .forEach(o -> ((BiConsumer<Set, Object>)myConsumerMap.get(o)).accept(expandedFactsSet, raoFact));
        }
    }


    protected void addOptionConsumers(RAOFactsExtractor raoFactsExtractor)
    {
        String triggeringOption = raoFactsExtractor.getTriggeringOption();
        Map<String, BiConsumer<Set<Object>, R>> myConsumerMap = getConsumerMap();
        myConsumerMap.put(triggeringOption, (f, r) -> f.addAll(raoFactsExtractor.expandFact(r)));
    }


    protected Optional<BiConsumer<Set<Object>, R>> getConsumer(String option)
    {
        Map<String, BiConsumer<Set<Object>, R>> myConsumerMap = getConsumerMap();
        if(MapUtils.isEmpty(myConsumerMap) || Objects.isNull(myConsumerMap.get(option)))
        {
            return Optional.empty();
        }
        return Optional.of(myConsumerMap.get(option));
    }


    protected Set<Object> expandRAO(R rao, Collection<String> options)
    {
        Set<Object> facts = new LinkedHashSet();
        if(Objects.nonNull(rao))
        {
            options.stream().map(this::getConsumer).filter(Optional::isPresent).forEach(c -> ((BiConsumer<Set, Object>)c.get()).accept(facts, rao));
        }
        return facts;
    }


    protected Collection<String> getFilteredOptions(Collection<String> options)
    {
        Set<String> onlyValidOptions = new HashSet<>(options);
        Collection<String> localMinOptions = getMinOptions();
        if(CollectionUtils.isNotEmpty(localMinOptions))
        {
            onlyValidOptions.addAll(localMinOptions);
        }
        Collection<String> localValidOptions = getValidOptions();
        if(CollectionUtils.isNotEmpty(localValidOptions))
        {
            onlyValidOptions.retainAll(localValidOptions);
        }
        return onlyValidOptions;
    }


    protected Set<String> addExtraValidOptions(List<RAOFactsExtractor> raoExtractorList)
    {
        if(CollectionUtils.isNotEmpty(raoExtractorList))
        {
            return (Set<String>)raoExtractorList.stream().filter(e -> StringUtils.isNotEmpty(e.getTriggeringOption()))
                            .map(RAOFactsExtractor::getTriggeringOption).collect(Collectors.toSet());
        }
        return Sets.newHashSet();
    }


    protected Set<String> addExtraDefaultOptions(List<RAOFactsExtractor> raoExtractorList)
    {
        if(CollectionUtils.isNotEmpty(raoExtractorList))
        {
            return (Set<String>)raoExtractorList.stream().filter(RAOFactsExtractor::isDefault).map(RAOFactsExtractor::getTriggeringOption)
                            .collect(Collectors.toSet());
        }
        return Sets.newHashSet();
    }


    protected Set<String> addExtraMinOptions(List<RAOFactsExtractor> raoExtractorList)
    {
        if(CollectionUtils.isNotEmpty(raoExtractorList))
        {
            return (Set<String>)raoExtractorList.stream().filter(RAOFactsExtractor::isMinOption).map(RAOFactsExtractor::getTriggeringOption)
                            .collect(Collectors.toSet());
        }
        return SetUtils.EMPTY_SET;
    }


    protected List<RAOFactsExtractor> getFactExtractorList()
    {
        return this.factExtractorList;
    }


    @Required
    public void setFactExtractorList(List<RAOFactsExtractor> factExtractorList)
    {
        this.factExtractorList = factExtractorList;
    }


    protected Collection<String> getDefaultOptions()
    {
        Collection<String> combinedDefaultOptions = getConcurrentlySafeOptions(this.defaultOptions);
        combinedDefaultOptions.addAll(addExtraDefaultOptions(getFactExtractorList()));
        return combinedDefaultOptions;
    }


    protected Collection<String> getValidOptions()
    {
        Collection<String> combinedValidOptions = getConcurrentlySafeOptions(this.validOptions);
        combinedValidOptions.addAll(addExtraValidOptions(getFactExtractorList()));
        return combinedValidOptions;
    }


    protected Collection<String> getMinOptions()
    {
        Collection<String> combinedMinOptions = getConcurrentlySafeOptions(this.minOptions);
        combinedMinOptions.addAll(addExtraMinOptions(getFactExtractorList()));
        return combinedMinOptions;
    }


    protected Collection<String> getConcurrentlySafeOptions(Collection<String> options)
    {
        return (Collection<String>)new ConcurrentHashSet(options);
    }


    public void setConsumerMap(Map<String, BiConsumer<Set<Object>, R>> consumerMap)
    {
        this.consumerMap = consumerMap;
    }


    protected Map<String, BiConsumer<Set<Object>, R>> getConsumerMap()
    {
        return this.consumerMap;
    }


    public void setMinOptions(Collection<String> minOptions)
    {
        this.minOptions = minOptions;
    }


    public void setValidOptions(Collection<String> validOptions)
    {
        this.validOptions = validOptions;
    }


    public void setDefaultOptions(Collection<String> defaultOptions)
    {
        this.defaultOptions = defaultOptions;
    }


    protected abstract R createRAO(T paramT);
}
