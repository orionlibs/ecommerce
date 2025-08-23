package de.hybris.platform.persistence.type;

import de.hybris.platform.core.PK;

public abstract class HierarchieTypeEJB extends TypeEJB implements HierarchieTypeRemote, HierarchieTypeHome
{
    public abstract PK getSuperTypePK();


    public abstract void setSuperTypePK(PK paramPK);


    public abstract String getInheritancePathStringInternal();


    public abstract void setInheritancePathStringInternal(String paramString);
}
