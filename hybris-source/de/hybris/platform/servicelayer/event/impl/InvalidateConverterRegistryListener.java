package de.hybris.platform.servicelayer.event.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.event.events.InvalidateModelConverterRegistryEvent;
import de.hybris.platform.servicelayer.internal.converter.ConverterRegistry;
import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import de.hybris.platform.servicelayer.internal.converter.impl.TypeSystemAwareModelConverter;
import de.hybris.platform.servicelayer.type.TypeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public abstract class InvalidateConverterRegistryListener extends AbstractEventListener<InvalidateModelConverterRegistryEvent>
{
    private static final Logger LOG = Logger.getLogger(InvalidateConverterRegistryListener.class);


    protected void onEvent(InvalidateModelConverterRegistryEvent event)
    {
        String type = event.getComposedTypeCode();
        if(type == null)
        {
            ConverterRegistry registry = getConverterRegistry();
            synchronized(registry)
            {
                for(ModelConverter converter : registry.getModelConverters())
                {
                    refreshModelConverter(converter);
                }
            }
        }
        else if(event.isRefresh())
        {
            ConverterRegistry registry = getConverterRegistry();
            synchronized(registry)
            {
                refreshModelConverterForRegistry(registry, event.getComposedTypeCode());
                TypeService typeService = (TypeService)Registry.getCoreApplicationContext().getBean("typeService", TypeService.class);
                ComposedTypeModel ctm = typeService.getComposedTypeForCode(event.getComposedTypeCode());
                for(ComposedTypeModel ctmSubtype : ctm.getAllSubTypes())
                {
                    refreshModelConverterForRegistry(registry, ctmSubtype.getCode());
                }
            }
        }
        else
        {
            getConverterRegistry().removeModelConverterBySourceType(event.getComposedTypeCode());
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Removed type " + event.getComposedTypeCode() + " from converter registry " +
                                getConverterRegistry());
            }
        }
    }


    private void refreshModelConverterForRegistry(ConverterRegistry registry, String composedTypeCodeToRefresh)
    {
        ModelConverter converter = registry.getModelConverterBySourceType(composedTypeCodeToRefresh);
        refreshModelConverter(converter);
    }


    private void refreshModelConverter(ModelConverter converter)
    {
        if(converter instanceof TypeSystemAwareModelConverter)
        {
            ((TypeSystemAwareModelConverter)converter).typeSystemChanged();
        }
    }


    @Required
    public abstract ConverterRegistry getConverterRegistry();
}
