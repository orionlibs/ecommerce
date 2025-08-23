package de.hybris.platform.persistence.type;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.c2l.LocalizableItemEJB;

public abstract class SearchRestrictionEJB extends LocalizableItemEJB implements SearchRestrictionRemote, SearchRestrictionHome
{
    public abstract PK getPrincipalPK();


    public abstract void setPrincipalPK(PK paramPK);


    public abstract PK getRestrictedTypePK();


    public abstract void setRestrictedTypePK(PK paramPK);


    public abstract String getQuery();


    public abstract void setQuery(String paramString);


    protected int typeCode()
    {
        return 90;
    }


    public PK ejbCreate(PK principalPK, PK typePK, String query, String code, Boolean active)
    {
        doCreateInternal(null, null, null, null);
        setPrincipalPK(principalPK);
        setRestrictedTypePK(typePK);
        setQuery(query);
        setProperty("code", code);
        setProperty("active", active);
        return null;
    }


    public void ejbPostCreate(PK principalPK, PK typePK, String query, String code, Boolean active)
    {
        doPostCreateInternal(null, null, null);
    }
}
