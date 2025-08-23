package de.hybris.platform.persistence.type;

import de.hybris.platform.core.ItemWrapperCreator;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.SearchRestriction;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.persistence.c2l.LocalizableItemEJBImpl;
import de.hybris.platform.persistence.c2l.LocalizableItemRemote;

public class SearchRestrictionEJBImpl extends LocalizableItemEJBImpl implements SearchRestriction.SearchRestrictionImpl
{
    static
    {
        WrapperFactory.registerItemWrapperCreator(90, (ItemWrapperCreator)new Object());
    }

    protected SearchRestrictionEJBImpl(Tenant tenant, SearchRestrictionRemote res)
    {
        super(tenant, (LocalizableItemRemote)res);
    }


    protected TypeManagerEJB typeManager()
    {
        return (TypeManagerEJB)TypeManager.getInstance().getRemote();
    }


    public SearchRestrictionRemote getRemote()
    {
        return (SearchRestrictionRemote)super.getRemote();
    }


    protected void removeInternal(SessionContext ctx) throws ConsistencyCheckException
    {
        typeManager().removeItem((ItemRemote)getRemote());
    }


    public String getQuery(SessionContext ctx)
    {
        return getRemote().getQuery();
    }


    public void setQuery(SessionContext ctx, String query)
    {
        getRemote().setQuery(query);
    }


    public Principal getPrincipal()
    {
        try
        {
            return (Principal)getSession().getItem(getRemote().getPrincipalPK());
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
        catch(IllegalArgumentException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public void setPrincipal(Principal principal)
    {
        getRemote().setPrincipalPK((principal != null) ? principal.getPK() : null);
    }


    public ComposedType getRestrictedType()
    {
        try
        {
            return (ComposedType)getSession().getItem(getRemote().getRestrictedTypePK());
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
        catch(IllegalArgumentException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public void setRestrictedType(ComposedType type)
    {
        getRemote().setRestrictedTypePK((type != null) ? type.getPK() : null);
    }
}
