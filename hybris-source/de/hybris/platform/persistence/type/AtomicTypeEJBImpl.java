package de.hybris.platform.persistence.type;

import de.hybris.platform.core.ItemWrapperCreator;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.jalo.type.AtomicType;
import java.util.Set;

public class AtomicTypeEJBImpl extends TypeEJBImpl implements AtomicType.AtomicTypeImpl
{
    static
    {
        WrapperFactory.registerItemWrapperCreator(81, (ItemWrapperCreator)new Object());
    }

    public AtomicTypeEJBImpl(Tenant tenant, AtomicTypeRemote remoteObject)
    {
        super(tenant, (TypeRemote)remoteObject);
    }


    public Class getJavaClass()
    {
        return getAtomicTypeRemote().getJavaClass();
    }


    public AtomicType getSuperType()
    {
        return (AtomicType)wrap(getAtomicTypeRemote().getSuperType());
    }


    public Set getSubTypes()
    {
        return (Set)wrap(getTypeManagerEJB().getSubTypes((HierarchieTypeRemote)getAtomicTypeRemote()));
    }


    public boolean isAbstract()
    {
        return getAtomicTypeRemote().isAbstract();
    }


    public String toString()
    {
        return "Atomic(" + getCode() + "," + getJavaClass().getName() + ")";
    }


    protected AtomicTypeRemote getAtomicTypeRemote()
    {
        return (AtomicTypeRemote)getRemote();
    }
}
