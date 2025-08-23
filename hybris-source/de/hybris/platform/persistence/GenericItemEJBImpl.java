package de.hybris.platform.persistence;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.ItemWrapperCreator;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.c2l.LocalizableItemEJBImpl;
import de.hybris.platform.persistence.c2l.LocalizableItemRemote;
import de.hybris.platform.persistence.property.EJBPropertyContainer;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import de.hybris.platform.persistence.type.TypeManagerEJB;
import de.hybris.platform.util.JaloPropertyContainer;
import java.util.Collection;

public class GenericItemEJBImpl extends LocalizableItemEJBImpl implements GenericItem.GenericItemImpl
{
    private static final ItemWrapperCreator WRAPPER_CREATOR = (ItemWrapperCreator)new Object();

    static
    {
        WrapperFactory.registerItemWrapperCreator(99, WRAPPER_CREATOR);
    }

    public static GenericItem createGenericItem(Tenant tenant, PK pk, ComposedType genericItemType, JaloPropertyContainer props)
    {
        try
        {
            GenericItemHome home = (GenericItemHome)tenant.getPersistencePool().getHomeProxy(genericItemType
                            .getItemTypeCode());
            Cache cache = tenant.getCache();
            return (GenericItem)WrapperFactory.wrap(cache, home
                            .create(pk,
                                            (ComposedTypeRemote)WrapperFactory.unwrap(cache, genericItemType),
                                            (EJBPropertyContainer)WrapperFactory.unwrap(cache, props)));
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
    }


    protected GenericItemEJBImpl(Tenant tenant, GenericItemRemote remoteObject)
    {
        super(tenant, (LocalizableItemRemote)remoteObject);
    }


    protected void removeInternal(SessionContext ctx) throws ConsistencyCheckException
    {
        ((TypeManagerEJB)TypeManager.getInstance().getRemote()).removeItem((ItemRemote)getRemote());
    }


    protected GenericItemRemote getRemote()
    {
        return (GenericItemRemote)super.getRemote();
    }


    public <T extends de.hybris.platform.jalo.Item> Collection<T> getRelatedItems(String relationQualifier)
    {
        return null;
    }


    public <T extends de.hybris.platform.jalo.Item> boolean setRelatedItems(String relationQualifier, Collection<T> values)
    {
        return false;
    }


    protected boolean isHandlingOwnerAsReferenceSupported()
    {
        return true;
    }
}
