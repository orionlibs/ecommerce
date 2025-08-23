package de.hybris.platform.search.restriction.impl;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.SearchRestrictionModel;
import de.hybris.platform.jalo.flexiblesearch.ContextQueryFilter;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.search.restriction.dao.SearchRestrictionDao;
import de.hybris.platform.search.restriction.session.SessionSearchRestriction;
import de.hybris.platform.search.restriction.session.converter.SessionSearchRestrictionConverter;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSearchRestrictionService implements SearchRestrictionService
{
    public static final String CTX_SEARCH_RESTRICTIONS = "ctxSearchRestrictions";
    public static final String DISABLE_RESTRICTIONS = "disableRestrictions";
    private SessionService sessionService;
    private SearchRestrictionDao searchRestrictionDao;
    private SessionSearchRestrictionConverter converter;


    public void addSessionSearchRestrictions(Collection<SessionSearchRestriction> restrictions)
    {
        ServicesUtil.validateParameterNotNull(restrictions, "restrictions is required! null given.");
        Collection<ContextQueryFilter> restrictionsToAdd = this.converter.convertFromRestrictions(restrictions);
        Collection<ContextQueryFilter> existingRestrictions = (Collection<ContextQueryFilter>)this.sessionService.getAttribute("ctxSearchRestrictions");
        if(existingRestrictions == null || existingRestrictions.isEmpty())
        {
            this.sessionService.setAttribute("ctxSearchRestrictions", restrictionsToAdd);
        }
        else
        {
            Collection<ContextQueryFilter> _existingRestrictions = new ArrayList<>(existingRestrictions);
            _existingRestrictions.addAll(restrictionsToAdd);
            this.sessionService.setAttribute("ctxSearchRestrictions", _existingRestrictions);
        }
    }


    public void addSessionSearchRestrictions(SessionSearchRestriction... restrictions)
    {
        addSessionSearchRestrictions(Arrays.asList(restrictions));
    }


    public void clearSessionSearchRestrictions()
    {
        this.sessionService.removeAttribute("ctxSearchRestrictions");
    }


    public void disableSearchRestrictions()
    {
        this.sessionService.setAttribute("disableRestrictions", Boolean.TRUE);
    }


    public void enableSearchRestrictions()
    {
        this.sessionService.setAttribute("disableRestrictions", Boolean.FALSE);
    }


    public Collection<SearchRestrictionModel> getActiveSearchRestrictions(PrincipalModel principal, boolean includePrincipalGroups, Collection<ComposedTypeModel> types)
    {
        return this.searchRestrictionDao.findActiveSearchRestrictionsByPrincipalAndType(principal, includePrincipalGroups, types);
    }


    public Collection<SearchRestrictionModel> getInactiveSearchRestrictions(PrincipalModel principal, boolean includePrincipalGroups, Collection<ComposedTypeModel> types)
    {
        return this.searchRestrictionDao.findInactiveSearchRestrictionsByPrincipalAndType(principal, includePrincipalGroups, types);
    }


    public Collection<SearchRestrictionModel> getSearchRestrictions(PrincipalModel principal, boolean includePrincipalGroups, Collection<ComposedTypeModel> types)
    {
        return this.searchRestrictionDao.findSearchRestrictionsByPrincipalAndType(principal, includePrincipalGroups, types);
    }


    public Collection<SearchRestrictionModel> getSearchRestrictionsForType(ComposedTypeModel type)
    {
        return this.searchRestrictionDao.findSearchRestrictionsByType(type);
    }


    public Collection<SessionSearchRestriction> getSessionSearchRestrictions()
    {
        Collection<ContextQueryFilter> filters = (Collection<ContextQueryFilter>)this.sessionService.getAttribute("ctxSearchRestrictions");
        if(filters == null || filters.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        return Collections.unmodifiableCollection(this.converter.convertFromFilters(filters));
    }


    public Collection<SessionSearchRestriction> getSessionSearchRestrictions(ComposedTypeModel type)
    {
        Collection<SessionSearchRestriction> sessionSearchRestrictions = getSessionSearchRestrictions();
        Collection<SessionSearchRestriction> result = new ArrayList<>();
        for(SessionSearchRestriction restriction : sessionSearchRestrictions)
        {
            if(restriction.getRestrictedType().equals(type))
            {
                result.add(restriction);
            }
        }
        return result;
    }


    public boolean hasRestrictions(PrincipalModel principal, boolean includePrincipalGroups, ComposedTypeModel type)
    {
        Collection<ComposedTypeModel> types = new ArrayList<>();
        types.add(type);
        return (!getSessionSearchRestrictions(type).isEmpty() ||
                        !getSearchRestrictions(principal, includePrincipalGroups, types).isEmpty());
    }


    public boolean isSearchRestrictionsEnabled()
    {
        Object disableRestrictions = this.sessionService.getAttribute("disableRestrictions");
        if(disableRestrictions == null)
        {
            return true;
        }
        return Boolean.FALSE.equals(disableRestrictions);
    }


    public void removeSessionSearchRestrictions(Collection<SessionSearchRestriction> restrictions)
    {
        if(restrictions != null && !restrictions.isEmpty())
        {
            Collection<ContextQueryFilter> filtersToRemove = this.converter.convertFromRestrictions(restrictions);
            Collection<ContextQueryFilter> currentFilters = (Collection<ContextQueryFilter>)this.sessionService.getAttribute("ctxSearchRestrictions");
            if(currentFilters != null)
            {
                ArrayList<ContextQueryFilter> currentFiltersList = new ArrayList<>(currentFilters);
                currentFiltersList.removeAll(filtersToRemove);
                clearSessionSearchRestrictions();
                this.sessionService.setAttribute("ctxSearchRestrictions", currentFiltersList);
            }
        }
    }


    public void removalAllSessionSearchRestrictions()
    {
        this.sessionService.removeAttribute("ctxSearchRestrictions");
    }


    @Required
    public void setConverter(SessionSearchRestrictionConverter converter)
    {
        this.converter = converter;
    }


    @Required
    public void setSearchRestrictionDao(SearchRestrictionDao searchRestrictionDao)
    {
        this.searchRestrictionDao = searchRestrictionDao;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }
}
