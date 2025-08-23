package de.hybris.platform.servicelayer.internal.model;

import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import de.hybris.platform.servicelayer.internal.converter.PersistenceObject;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper;
import java.util.Collection;
import java.util.Set;
import java.util.Vector;

public interface ModelContext
{
    public static final ThreadLocal<Vector<PK>> loading = new ThreadLocal<>();


    void afterPersist(Collection<ModelWrapper> paramCollection);


    void attach(Object paramObject1, Object paramObject2, ModelConverter paramModelConverter);


    void detach(Object paramObject1, Object paramObject2, ModelConverter paramModelConverter);


    Object getAttached(Object paramObject, ModelConverter paramModelConverter);


    boolean isAttached(Object paramObject, ModelConverter paramModelConverter);


    void clear();


    Set<Object> getNew();


    Set<Object> getModified();


    void afterDirectPersist(Collection<ModelWrapper> paramCollection);


    void afterDirectPersist(ModelWrapper paramModelWrapper);


    default boolean isLoading(PersistenceObject source)
    {
        if(loading.get() != null)
        {
            return ((Vector)loading.get()).contains(source.getPK());
        }
        return false;
    }


    default void markLoading(PersistenceObject source)
    {
        if(loading.get() == null)
        {
            loading.set(new Vector<>());
        }
        ((Vector<PK>)loading.get()).add(source.getPK());
    }


    default void clearLoading(PersistenceObject source)
    {
        if(loading.get() != null)
        {
            ((Vector)loading.get()).remove(source.getPK());
        }
    }
}
