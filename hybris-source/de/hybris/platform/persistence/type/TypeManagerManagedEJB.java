package de.hybris.platform.persistence.type;

import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.persistence.c2l.LocalizableItemEJB;

public abstract class TypeManagerManagedEJB extends LocalizableItemEJB implements TypeManagerManagedRemote
{
    protected TypeManagerEJB getTypeManager()
    {
        return SystemEJB.getInstance().getTypeManager();
    }


    protected Object pGetInternalProperty(String name)
    {
        return getProperty(name);
    }


    protected void pSetInternalProperty(String name, Object value)
    {
        setProperty(name, value);
    }


    protected boolean equals(Object o1, Object o2)
    {
        return (o1 == o2 || (o1 != null && o1.equals(o2)));
    }
}
