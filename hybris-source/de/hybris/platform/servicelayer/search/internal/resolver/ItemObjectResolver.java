package de.hybris.platform.servicelayer.search.internal.resolver;

import de.hybris.platform.core.PK;
import java.io.Serializable;
import java.util.List;

public interface ItemObjectResolver<T> extends Serializable
{
    T resolve(int paramInt, Object paramObject, List<Class> paramList);


    T resolve(Object paramObject, List<Class> paramList);


    Object unresolve(T paramT);


    boolean preloadItems(List<PK> paramList);
}
