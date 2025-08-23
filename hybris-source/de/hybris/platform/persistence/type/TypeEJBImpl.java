package de.hybris.platform.persistence.type;

import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.type.Type;

public abstract class TypeEJBImpl extends TypeManagerManagedEJBImpl implements Type.TypeImpl
{
    public TypeEJBImpl(Tenant tenant, TypeRemote remoteObject)
    {
        super(tenant, (TypeManagerManagedRemote)remoteObject);
    }


    public String getCode()
    {
        return getTypeRemote().getCode();
    }


    protected TypeRemote getTypeRemote()
    {
        return (TypeRemote)getRemote();
    }
}
