package de.hybris.platform.cockpit.services.meta.impl;

import de.hybris.platform.cockpit.model.DynamicWidgetPreferencesModel;
import de.hybris.platform.cockpit.model.meta.ExtendedType;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.ItemType;
import de.hybris.platform.cockpit.model.meta.impl.WidgetParameterPropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.WidgetType;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WidgetTypeLoader extends AbstractTypeLoader
{
    private static final Logger LOG = LoggerFactory.getLogger(WidgetTypeLoader.class);


    public TypeModel getValueType(ObjectType enclosingType, PropertyDescriptor propertyDescriptor, TypeService typeService)
    {
        if(enclosingType instanceof WidgetType && propertyDescriptor instanceof WidgetParameterPropertyDescriptor)
        {
            TypeModel valueType = ((WidgetParameterPropertyDescriptor)propertyDescriptor).getWidgetParameter().getType();
            if(propertyDescriptor.isLocalized() && valueType instanceof MapTypeModel)
            {
                valueType = ((MapTypeModel)valueType).getReturntype();
            }
            return valueType;
        }
        return null;
    }


    public Set<String> getExtendedTypeCodes(TypedObject item)
    {
        if(item == null)
        {
            throw new IllegalArgumentException("Item must not be null.");
        }
        Object itemModel = item.getObject();
        if(itemModel instanceof DynamicWidgetPreferencesModel)
        {
            return Collections.singleton("_widget_DynamicWidgetPreferences." + ((DynamicWidgetPreferencesModel)itemModel)
                            .getPk().getLongValueAsString());
        }
        return null;
    }


    public List<Object> getAvailableValues(TypeModel type, PropertyDescriptor propertyDescriptor, TypeService typeService)
    {
        return null;
    }


    public ExtendedType loadType(String code, TypeService typeService)
    {
        if(code.startsWith("_widget_"))
        {
            return (ExtendedType)loadWidgetType(code);
        }
        return null;
    }


    protected WidgetType loadWidgetType(String code)
    {
        ComposedTypeModel composedType = getTypeService().getComposedTypeForCode(code
                        .substring("_widget_".length(), code.indexOf(".")));
        String pkString = code.substring(code.indexOf(".") + 1);
        return (WidgetType)new Object(this, composedType, pkString, composedType);
    }


    public String getAttributeCodeFromPropertyQualifier(String propertyQualifier)
    {
        if(propertyQualifier.startsWith("_widget_"))
        {
            return propertyQualifier.substring(propertyQualifier.indexOf(".") + 1);
        }
        return null;
    }


    public String getTypeCodeFromPropertyQualifier(String propertyQualifier)
    {
        if(propertyQualifier.startsWith("_widget_"))
        {
            return propertyQualifier;
        }
        return null;
    }


    public Collection<ExtendedType> getExtendedTypesForTemplate(ItemType type, String templateCode, TypeService typeService)
    {
        return null;
    }
}
