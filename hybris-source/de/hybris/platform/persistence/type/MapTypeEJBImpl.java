package de.hybris.platform.persistence.type;

import de.hybris.platform.core.ItemWrapperCreator;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.MapType;
import de.hybris.platform.jalo.type.Type;

public class MapTypeEJBImpl extends TypeEJBImpl implements MapType.MapTypeImpl
{
    static
    {
        WrapperFactory.registerItemWrapperCreator(84, (ItemWrapperCreator)new Object());
    }

    public MapTypeEJBImpl(Tenant tenant, MapTypeRemote remoteObject)
    {
        super(tenant, (TypeRemote)remoteObject);
    }


    public Type getArgumentType(SessionContext ctx)
    {
        return (Type)wrap(getMapTypeRemote().getArgumentType());
    }


    public Type getReturnType(SessionContext ctx)
    {
        return (Type)wrap(getMapTypeRemote().getReturnType());
    }


    protected MapTypeRemote getMapTypeRemote()
    {
        return (MapTypeRemote)getRemote();
    }
}
