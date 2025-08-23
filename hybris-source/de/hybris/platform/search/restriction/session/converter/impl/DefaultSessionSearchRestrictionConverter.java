package de.hybris.platform.search.restriction.session.converter.impl;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.jalo.flexiblesearch.ContextQueryFilter;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.search.restriction.session.SessionSearchRestriction;
import de.hybris.platform.search.restriction.session.converter.SessionSearchRestrictionConverter;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSessionSearchRestrictionConverter implements SessionSearchRestrictionConverter
{
    private ModelService modelService;


    public SessionSearchRestriction convert(ContextQueryFilter filter)
    {
        ComposedTypeModel type = (ComposedTypeModel)this.modelService.get(filter.getRestrictionType());
        return new SessionSearchRestriction(filter.getCode(), filter.getQuery(), type);
    }


    public ContextQueryFilter convert(SessionSearchRestriction restriction)
    {
        ComposedType type = (ComposedType)this.modelService.getSource(restriction.getRestrictedType());
        return new ContextQueryFilter(restriction.getCode(), type, restriction.getQuery());
    }


    public Collection<SessionSearchRestriction> convertFromFilters(Collection<ContextQueryFilter> filters)
    {
        Collection<SessionSearchRestriction> result = new ArrayList<>();
        for(ContextQueryFilter filter : filters)
        {
            result.add(convert(filter));
        }
        return Collections.unmodifiableCollection(result);
    }


    public Collection<ContextQueryFilter> convertFromRestrictions(Collection<SessionSearchRestriction> restrictions)
    {
        Collection<ContextQueryFilter> result = new ArrayList<>();
        for(SessionSearchRestriction restriction : restrictions)
        {
            result.add(convert(restriction));
        }
        return Collections.unmodifiableCollection(result);
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
