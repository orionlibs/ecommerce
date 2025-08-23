package de.hybris.platform.cockpit.services.values.impl;

import de.hybris.platform.cockpit.model.listview.impl.DefaultValueHandler;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.undo.UndoableOperation;
import de.hybris.platform.cockpit.model.undo.impl.ItemChangeUndoableOperation;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ObjectValueHandler;
import de.hybris.platform.cockpit.services.values.ObjectValueHandlerRegistry;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.services.values.ValueService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UndoTools;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class DefaultValueService implements ValueService
{
    private ObjectValueHandlerRegistry valueHandlerRegistry;
    private SystemService systemService;


    public Object getValue(TypedObject typedObject, PropertyDescriptor propertyDescriptor) throws ValueHandlerException
    {
        DefaultValueHandler defaultValueHandler = new DefaultValueHandler(propertyDescriptor);
        if(propertyDescriptor.isLocalized())
        {
            return defaultValueHandler.getValue(typedObject, UISessionUtils.getCurrentSession().getGlobalDataLanguageIso());
        }
        return defaultValueHandler.getValue(typedObject);
    }


    public ObjectValueContainer getValues(TypedObject typedObject, Set<PropertyDescriptor> propertyDescriptors, Set<String> languageIsos) throws ValueHandlerException
    {
        ObjectValueContainer ret = new ObjectValueContainer((ObjectType)typedObject.getType(), typedObject.getObject());
        for(ObjectValueHandler valueHandler : getValueHandlerRegistry().getValueHandlerChain((ObjectType)typedObject.getType()))
        {
            valueHandler.loadValues(ret, (ObjectType)typedObject.getType(), typedObject, propertyDescriptors, languageIsos);
        }
        return ret;
    }


    public void setValue(TypedObject typedObject, PropertyDescriptor propertyDescriptor, Object value) throws ValueHandlerException
    {
        DefaultValueHandler defaultValueHandler = new DefaultValueHandler(propertyDescriptor);
        defaultValueHandler.setValue(typedObject, value);
    }


    public void setValue(TypedObject typedObject, PropertyDescriptor propertyDescriptor, Object value, String langIso) throws ValueHandlerException
    {
        DefaultValueHandler defaultValueHandler = new DefaultValueHandler(propertyDescriptor);
        defaultValueHandler.setValue(typedObject, value, langIso);
    }


    public void setValues(TypedObject typedObject, ObjectValueContainer values) throws ValueHandlerException
    {
        for(ObjectValueHandler valueHandler : getValueHandlerRegistry().getValueHandlerChain((ObjectType)typedObject.getType()))
        {
            valueHandler.storeValues(values);
        }
        UndoTools.addUndoOperationAndEvent(UISessionUtils.getCurrentSession().getUndoManager(), (UndoableOperation)new ItemChangeUndoableOperation(typedObject, values), this);
    }


    public void updateValues(TypedObject typedObject, ObjectValueContainer values) throws ValueHandlerException
    {
        if(typedObject != null)
        {
            for(ObjectValueHandler valueHandler : getValueHandlerRegistry().getValueHandlerChain((ObjectType)typedObject.getType()))
            {
                valueHandler.updateValues(values, getSystemService().getAllWriteableLanguageIsos());
            }
        }
    }


    protected ObjectValueHandlerRegistry getValueHandlerRegistry()
    {
        return this.valueHandlerRegistry;
    }


    @Required
    public void setValueHandlerRegistry(ObjectValueHandlerRegistry valueHandlerRegistry)
    {
        this.valueHandlerRegistry = valueHandlerRegistry;
    }


    @Required
    public void setSystemService(SystemService systemService)
    {
        this.systemService = systemService;
    }


    public SystemService getSystemService()
    {
        return this.systemService;
    }
}
