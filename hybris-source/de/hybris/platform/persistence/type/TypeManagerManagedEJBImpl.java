package de.hybris.platform.persistence.type;

import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.type.TypeManagerManaged;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.persistence.c2l.LocalizableItemEJBImpl;
import de.hybris.platform.persistence.c2l.LocalizableItemRemote;

public abstract class TypeManagerManagedEJBImpl extends LocalizableItemEJBImpl implements TypeManagerManaged.TypeManagerManagedImpl
{
    public TypeManagerManagedEJBImpl(Tenant tenant, TypeManagerManagedRemote remoteObject)
    {
        super(tenant, (LocalizableItemRemote)remoteObject);
    }


    protected void removeInternal(SessionContext ctx) throws ConsistencyCheckException
    {
        ((TypeManagerEJB)TypeManager.getInstance().getRemote()).removeItem((ItemRemote)getRemote());
    }


    protected TypeManagerEJB getTypeManagerEJB()
    {
        return (TypeManagerEJB)TypeManager.getInstance().getRemote();
    }


    public TypeManagerManagedRemote getRemote()
    {
        return (TypeManagerManagedRemote)super.getRemote();
    }
}
