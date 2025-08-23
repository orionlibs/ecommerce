package de.hybris.platform.persistence.type;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.c2l.LocalizableItemRemote;

public interface SearchRestrictionRemote extends LocalizableItemRemote
{
    PK getPrincipalPK();


    void setPrincipalPK(PK paramPK);


    PK getRestrictedTypePK();


    void setRestrictedTypePK(PK paramPK);


    String getQuery();


    void setQuery(String paramString);
}
