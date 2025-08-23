package de.hybris.platform.servicelayer.search.internal.resolver.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultModelService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.internal.resolver.ItemObjectResolver;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class DefaultModelResolver implements ItemObjectResolver<Object>
{
    private ModelService modelService;


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public Object resolve(int expectedColumnIndex, Object cachedIdentifier, List<Class<?>> expectedClassList)
    {
        return cachedIdentifier;
    }


    public Object resolve(Object cachedIdentifier, List<Class<?>> expectedClassList)
    {
        if(cachedIdentifier instanceof de.hybris.platform.jalo.enumeration.EnumerationValue || isEnumValuePK(cachedIdentifier))
        {
            return resolveEnumerationValue(cachedIdentifier, expectedClassList);
        }
        return (cachedIdentifier instanceof PK) ? this.modelService.get((PK)cachedIdentifier) : this.modelService.get(cachedIdentifier);
    }


    private boolean isEnumValuePK(Object cachedIdentifier)
    {
        return (cachedIdentifier instanceof PK && ((PK)cachedIdentifier).getTypeCode() == 91);
    }


    private Object resolveEnumerationValue(Object cachedIdentifier, List<Class<?>> expectedClassList)
    {
        if(expectedClassList != null)
        {
            if(HybrisEnumValue.class.isAssignableFrom(expectedClassList.get(0)))
            {
                return this.modelService.get(cachedIdentifier);
            }
        }
        return this.modelService.get(cachedIdentifier, "EnumerationValue");
    }


    public Object unresolve(Object model)
    {
        return this.modelService.toPersistenceLayer(model);
    }


    public boolean preloadItems(List<PK> pks)
    {
        Preconditions.checkState(this.modelService instanceof DefaultModelService);
        return ((DefaultModelService)this.modelService).preloadItems(pks);
    }
}
