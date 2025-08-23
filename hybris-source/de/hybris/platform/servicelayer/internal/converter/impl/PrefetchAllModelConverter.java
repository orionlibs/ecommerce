package de.hybris.platform.servicelayer.internal.converter.impl;

import de.hybris.platform.directpersistence.selfhealing.SelfHealingService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.model.impl.SourceTransformer;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.model.strategies.SerializationStrategy;

public class PrefetchAllModelConverter extends ItemModelConverter
{
    public PrefetchAllModelConverter(ModelService modelService, I18NService i18nService, CommonI18NService commonI18NService, String type, Class<? extends AbstractItemModel> modelClass, SerializationStrategy serializationStrategy, SourceTransformer sourceTransformer,
                    SelfHealingService selfHealingService)
    {
        super(modelService, i18nService, commonI18NService, type, modelClass, serializationStrategy, sourceTransformer, selfHealingService);
    }


    protected AttributePrefetchMode readPrefetchSettings()
    {
        return DefaultPrefetchAttributeMode.ATTRIBUTE_PREFETCH_MODE_ALL;
    }
}
