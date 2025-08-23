package de.hybris.platform.testframework;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.ItemLifecycleListener;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.persistence.framework.PersistencePool;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ItemCreationLifecycleListener implements PersistencePool.PersistenceListener, ItemLifecycleListener
{
    private final List<PersistencePool.PersistenceListener> listeners = new CopyOnWriteArrayList<>();


    public ItemCreationLifecycleListener()
    {
        Registry.getCurrentTenant().getPersistencePool().registerPersistenceListener(this);
    }


    public void afterItemCreation(SessionContext ctx, ComposedType type, Item createdItem, Item.ItemAttributeMap attributes)
    {
        this.listeners.stream().forEach(listener -> listener.entityCreated((PK)attributes.get(Item.PK)));
    }


    public void entityCreated(PK pk)
    {
        this.listeners.stream().forEach(listener -> listener.entityCreated(pk));
    }


    public void registerPersistenceListener(PersistencePool.PersistenceListener listener)
    {
        this.listeners.add(listener);
    }


    public void unregisterPersistenceListener(PersistencePool.PersistenceListener listener)
    {
        this.listeners.remove(listener);
    }
}
