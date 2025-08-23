package de.hybris.platform.servicelayer.model;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.servicelayer.internal.model.impl.ModelValueHistory;
import de.hybris.platform.servicelayer.model.strategies.DefaultFetchStrategy;
import de.hybris.platform.servicelayer.model.strategies.FetchStrategy;
import de.hybris.platform.servicelayer.model.strategies.SerializationStrategy;
import de.hybris.platform.servicelayer.model.strategies.SerializationStrategyLeanImpl;

public class DefaultNewModelContextFactory implements AbstractItemModel.NewModelContextFactory
{
    private final ItemTypeNamesCache itemTypeNamesCache = new ItemTypeNamesCache();


    public ItemModelInternalContext createNew(Class clazz)
    {
        return createNewBuilder(clazz).build();
    }


    private static final SerializationStrategy LEAN_SERIALIZATION_STRATEGY = (SerializationStrategy)new SerializationStrategyLeanImpl();


    protected ItemContextBuilder createNewBuilder(Class clazz)
    {
        ItemContextBuilder builder = new ItemContextBuilder();
        builder.setItemType(getItemTypeForClass(clazz));
        builder.setTenantID(getTenantId());
        builder.setValueHistory(new ModelValueHistory());
        builder.setFetchStrategy((FetchStrategy)new DefaultFetchStrategy());
        builder.setSerializationStrategy(LEAN_SERIALIZATION_STRATEGY);
        return builder;
    }


    protected String getTenantId()
    {
        Tenant currentTenantNoFallback = Registry.getCurrentTenantNoFallback();
        return (currentTenantNoFallback == null) ? null : currentTenantNoFallback.getTenantID();
    }


    protected String getItemTypeForClass(Class modelClass)
    {
        return this.itemTypeNamesCache.getItemTypeName(modelClass);
    }
}
