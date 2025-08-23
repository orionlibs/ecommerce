package com.hybris.backoffice.searchservices.providers.impl;

import com.hybris.backoffice.proxy.LabelServiceProxy;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.searchservices.indexer.service.impl.AbstractSnIndexerValueProvider;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class ItemtypeLabelSnIndexerValueProvider extends AbstractSnIndexerValueProvider<ItemModel, Void>
{
    protected static final Set<Class<?>> SUPPORTED_QUALIFIER_CLASSES = Set.of(Locale.class);
    private LabelServiceProxy labelServiceProxy;
    private TypeService typeService;


    public Set<Class<?>> getSupportedQualifierClasses() throws SnIndexerException
    {
        return SUPPORTED_QUALIFIER_CLASSES;
    }


    protected Object getFieldValue(SnIndexerContext indexerContext, SnIndexerFieldWrapper fieldWrapper, ItemModel source, Void data) throws SnIndexerException
    {
        String itemtype = source.getItemtype();
        ComposedTypeModel composedTypeModel = getTypeService().getComposedTypeForCode(itemtype);
        if(fieldWrapper.isLocalized())
        {
            Map<Locale, Object> localizedValue = new HashMap<>();
            fieldWrapper.getQualifiers().stream().forEach(qualifier -> {
                Locale locale = (Locale)qualifier.getAs(Locale.class);
                String label = getLabelServiceProxy().getObjectLabel(type, locale);
                if(StringUtils.isNotBlank(label))
                {
                    localizedValue.put(locale, label);
                }
            });
            return localizedValue;
        }
        throw new SnIndexerException(
                        String.format("Indexed property must be localized and of type string to use %s while %s is not", new Object[] {ItemtypeLabelSnIndexerValueProvider.class.getSimpleName(), fieldWrapper.getFieldId()}));
    }


    public LabelServiceProxy getLabelServiceProxy()
    {
        return this.labelServiceProxy;
    }


    public void setLabelServiceProxy(LabelServiceProxy labelServiceProxy)
    {
        this.labelServiceProxy = labelServiceProxy;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
