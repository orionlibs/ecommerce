package de.hybris.platform.cockpit.services.search.impl;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.search.ExtendedSearchResult;
import de.hybris.platform.cockpit.model.search.Facet;
import de.hybris.platform.cockpit.model.search.FacetsResult;
import de.hybris.platform.cockpit.model.search.Operator;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.services.PropertyBasedService;
import de.hybris.platform.cockpit.services.search.SearchProvider;
import de.hybris.platform.cockpit.util.TypeTools;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractSearchProvider extends PropertyBasedService implements SearchProvider
{
    protected static final Operator SIMPLE_OPERATOR = Operator.CONTAINS;
    private ConditionTranslatorRegistry conditionTranslatorRegistry = null;


    public Set<Facet> getAllSupportedFacets()
    {
        return Collections.emptySet();
    }


    public Set<Facet> getAllSupportedFacets(Collection<SearchType> rootTypes)
    {
        Set<Facet> ret = new LinkedHashSet<>();
        for(SearchType rt : rootTypes)
        {
            for(ObjectType superT : TypeTools.getAllSupertypes((ObjectType)rt))
            {
                ret.addAll(((SearchType)superT).getFacets());
            }
        }
        for(Collection<SearchType> types = rootTypes, next = new LinkedHashSet<>(); types != null && !types.isEmpty(); types = next, next = new LinkedHashSet<>())
        {
            for(SearchType t : types)
            {
                ret.addAll(t.getFacets());
                next.addAll(t.getSubtypes());
            }
        }
        return ret;
    }


    public abstract ExtendedSearchResult search(Query paramQuery);


    public abstract FacetsResult queryFacets(ExtendedSearchResult paramExtendedSearchResult, Set<Facet> paramSet);


    @Required
    public void setConditionTranslatorRegistry(ConditionTranslatorRegistry conditionTranslatorRegistry)
    {
        this.conditionTranslatorRegistry = conditionTranslatorRegistry;
    }


    public ConditionTranslatorRegistry getConditionTranslatorRegistry()
    {
        return this.conditionTranslatorRegistry;
    }
}
