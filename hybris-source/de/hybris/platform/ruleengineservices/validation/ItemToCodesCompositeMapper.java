package de.hybris.platform.ruleengineservices.validation;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class ItemToCodesCompositeMapper<T> implements Function<T, Set<String>>
{
    private List<Function<T, Set<String>>> mappers;


    public Set<String> apply(T item)
    {
        return (Set<String>)getMappers().stream().map(m -> (Set)m.apply(item)).flatMap(Collection::stream).collect(Collectors.toSet());
    }


    protected List<Function<T, Set<String>>> getMappers()
    {
        return this.mappers;
    }


    @Required
    public void setMappers(List<Function<T, Set<String>>> mappers)
    {
        this.mappers = mappers;
    }
}
