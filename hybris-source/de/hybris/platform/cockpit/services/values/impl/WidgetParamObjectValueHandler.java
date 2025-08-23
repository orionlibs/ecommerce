package de.hybris.platform.cockpit.services.values.impl;

import de.hybris.platform.cockpit.model.DynamicWidgetPreferencesModel;
import de.hybris.platform.cockpit.model.WidgetParameterModel;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.WidgetParameterPropertyDescriptor;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ObjectValueHandler;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

public class WidgetParamObjectValueHandler implements ObjectValueHandler
{
    private TypeService cockpitTypeService;
    private ModelService modelService;


    public void loadValues(ObjectValueContainer valueContainer, ObjectType type, Object source, Set<PropertyDescriptor> descriptors, Set<String> languageIsoCodes) throws ValueHandlerException
    {
        for(PropertyDescriptor pd : descriptors)
        {
            if(pd instanceof WidgetParameterPropertyDescriptor && source instanceof TypedObject)
            {
                Object sourceObject = ((TypedObject)source).getObject();
                if(sourceObject instanceof DynamicWidgetPreferencesModel)
                {
                    WidgetParameterModel parameter = getParameterModel(pd.getQualifier(), (DynamicWidgetPreferencesModel)sourceObject);
                    valueContainer.addValue(pd, null, TypeTools.item2Container(this.cockpitTypeService, parameter.getValue()));
                }
            }
        }
    }


    private WidgetParameterModel getParameterModel(String qualifier, DynamicWidgetPreferencesModel preferences)
    {
        WidgetParameterModel parameter = null;
        String parameterString = qualifier.substring(qualifier.indexOf(".") + 1);
        for(WidgetParameterModel param : preferences.getParameters())
        {
            if(StringUtils.equals(parameterString, param.getName()))
            {
                parameter = param;
                break;
            }
        }
        return parameter;
    }


    protected ItemModel fetchItem(Object source) throws ValueHandlerException
    {
        if(source instanceof de.hybris.platform.jalo.Item)
        {
            return (ItemModel)this.modelService.get(source);
        }
        if(source instanceof ItemModel)
        {
            return (ItemModel)source;
        }
        if(source instanceof PK)
        {
            return (ItemModel)this.modelService.get((PK)source);
        }
        if(source instanceof TypedObject)
        {
            return (ItemModel)((TypedObject)source).getObject();
        }
        throw new ValueHandlerException("invalid source object " + source + " (class = " + (
                        (source != null) ? source.getClass().getName() : "n/a") + ") - cannot fetch item", Collections.EMPTY_SET);
    }


    public void storeValues(ObjectValueContainer container) throws ValueHandlerException
    {
        storeValues(container, false);
    }


    public void storeValues(ObjectValueContainer container, boolean forceWrite) throws ValueHandlerException
    {
        ItemModel item = fetchItem(container.getObject());
        if(item instanceof DynamicWidgetPreferencesModel)
        {
            DynamicWidgetPreferencesModel preferences = (DynamicWidgetPreferencesModel)item;
            Set<PropertyDescriptor> processedProperties = new HashSet<>();
            try
            {
                for(ObjectValueContainer.ObjectValueHolder vh : container.getAllValues())
                {
                    if((forceWrite || vh.isModified()) && vh.getPropertyDescriptor() instanceof WidgetParameterPropertyDescriptor)
                    {
                        WidgetParameterPropertyDescriptor propertyDescriptor = (WidgetParameterPropertyDescriptor)vh.getPropertyDescriptor();
                        processedProperties.add(propertyDescriptor);
                        WidgetParameterModel parameterModel = getParameterModel(propertyDescriptor.getQualifier(), preferences);
                        parameterModel.setValue(TypeTools.container2Item(this.cockpitTypeService, vh.getCurrentValue()));
                        this.modelService.save(parameterModel);
                    }
                }
            }
            catch(Exception e)
            {
                throw new ValueHandlerException(e.getLocalizedMessage(), e, (processedProperties.size() == 1) ?
                                Collections.singleton((PropertyDescriptor)processedProperties.iterator().next()) : Collections.EMPTY_SET);
            }
        }
    }


    public void updateValues(ObjectValueContainer container, Set<String> languageIsoCodes) throws ValueHandlerException
    {
    }


    public void updateValues(ObjectValueContainer container, Set<String> languageIsoCodes, Set<PropertyDescriptor> descriptors) throws ValueHandlerException
    {
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public void setCockpitTypeService(TypeService cockpitTypeService)
    {
        this.cockpitTypeService = cockpitTypeService;
    }
}
