package de.hybris.platform.personalizationservices.dao.impl;

import de.hybris.platform.personalizationservices.dao.CxDaoStrategy;
import de.hybris.platform.personalizationservices.dao.CxDaoStrategySelector;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import org.apache.commons.collections4.CollectionUtils;

public class DefaultCxDaoStrategySelector implements CxDaoStrategySelector
{
    public Optional<CxDaoStrategy> selectStrategy(Collection<? extends CxDaoStrategy> strategies, Map<String, String> params)
    {
        if(CollectionUtils.isEmpty(strategies))
        {
            return Optional.empty();
        }
        Set<String> keySet = params.keySet();
        return (Optional<CxDaoStrategy>)strategies.stream().filter(s -> isValid(s, keySet)).map(s -> s)
                        .collect(bestStrategyCollector(this::strategyComparator));
    }


    protected boolean isValid(CxDaoStrategy strategy, Set<String> keySet)
    {
        Collection<String> requiredParameters = strategy.getRequiredParameters();
        return keySet.containsAll(requiredParameters);
    }


    protected int strategyComparator(CxDaoStrategy a, CxDaoStrategy b)
    {
        return a.getRequiredParameters().size() - b.getRequiredParameters().size();
    }


    protected <T> Collector<T, List<T>, Optional<T>> bestStrategyCollector(Comparator<? super T> comp)
    {
        return Collector.of(java.util.ArrayList::new, (list, t) -> addToList(list, t, comp), (list1, list2) -> selectBestList(list1, list2, comp), this::finalizeSelection, new Collector.Characteristics[0]);
    }


    protected <T> void addToList(List<T> list, T t, Comparator<? super T> comp)
    {
        if(list.isEmpty())
        {
            list.add(t);
        }
        else
        {
            int c = comp.compare(t, list.get(0));
            if(c > 0)
            {
                list.clear();
            }
            if(c >= 0)
            {
                list.add(t);
            }
        }
    }


    protected <T> List<T> selectBestList(List<T> list1, List<T> list2, Comparator<? super T> comp)
    {
        if(list1.isEmpty())
        {
            return list2;
        }
        if(list2.isEmpty())
        {
            return list1;
        }
        int r = comp.compare(list1.get(0), list2.get(0));
        if(r < 0)
        {
            return list2;
        }
        if(r > 0)
        {
            return list1;
        }
        list1.addAll(list2);
        return list1;
    }


    protected <T> Optional<T> finalizeSelection(List<T> results)
    {
        if(results.size() > 1)
        {
            throw new IllegalArgumentException("" + results
                            .size() + " CxDaoStrategy support the same number of provided parameters. Please change configuration.");
        }
        if(results.size() == 1)
        {
            return Optional.of(results.get(0));
        }
        return Optional.empty();
    }
}
