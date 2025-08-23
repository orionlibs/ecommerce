package de.hybris.platform.persistence.type;

import de.hybris.platform.core.ItemWrapperCreator;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.Type;

public class CollectionTypeEJBImpl extends TypeEJBImpl implements CollectionType.CollectionTypeImpl
{
    static
    {
        WrapperFactory.registerItemWrapperCreator(83, (ItemWrapperCreator)new Object());
    }

    public CollectionTypeEJBImpl(Tenant tenant, CollectionTypeRemote remoteObject)
    {
        super(tenant, (TypeRemote)remoteObject);
    }


    public Type getElementType(SessionContext ctx)
    {
        return (Type)wrap(getCollectionTypeRemote().getElementType());
    }


    public int getTypeOfCollection()
    {
        return translateToJaloCollectionType(getCollectionTypeRemote().getTypeOfCollection());
    }


    public String toString()
    {
        return "Collection(" + getElementType(null).getCode() + ")";
    }


    private int translateToJaloCollectionType(int ejbCollectionType)
    {
        switch(ejbCollectionType)
        {
            case 0:
                return 0;
            case 1:
                return 1;
            case 3:
                return 3;
            case 2:
                return 2;
        }
        throw new JaloSystemException(null, "unexpected collection type " + ejbCollectionType + " obtained.", 4711);
    }


    protected CollectionTypeRemote getCollectionTypeRemote()
    {
        return (CollectionTypeRemote)getRemote();
    }
}
