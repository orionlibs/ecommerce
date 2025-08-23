package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.undo.UndoableOperation;
import de.hybris.platform.cockpit.model.undo.impl.ItemChangeUndoableOperation;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ObjectValueHandler;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.services.values.ValueHandlerPermissionException;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UndoTools;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultValueHandler implements ValueHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultValueHandler.class);
    private final PropertyDescriptor propertyDescriptor;


    public DefaultValueHandler(PropertyDescriptor propertyDescriptor)
    {
        this.propertyDescriptor = propertyDescriptor;
    }


    public Object getValue(TypedObject item)
    {
        return getValue(item, UISessionUtils.getCurrentSession().getSystemService().getCurrentLanguage().getIsocode());
    }


    public Object getValue(TypedObject item, String languageIso)
    {
        Object ret = null;
        ObjectValueContainer currentObjectValues = getValueContainer((ObjectType)item.getType(), item);
        try
        {
            for(ObjectValueHandler valueHandler : UISessionUtils.getCurrentSession().getValueHandlerRegistry()
                            .getValueHandlerChain((ObjectType)item.getType()))
            {
                Set<PropertyDescriptor> pds = new HashSet<>(1);
                pds.add(this.propertyDescriptor);
                Set<String> langs = new HashSet<>(1);
                langs.add(languageIso);
                valueHandler.loadValues(currentObjectValues, (ObjectType)item.getType(), item, pds, langs);
            }
            ret = currentObjectValues.getValue(this.propertyDescriptor, this.propertyDescriptor.isLocalized() ? languageIso : null).getOriginalValue();
        }
        catch(IllegalArgumentException iae)
        {
            LOG.debug("Could not get value for item '" + item + "' and language '" + languageIso + "'.", iae);
        }
        catch(ValueHandlerPermissionException pe)
        {
            ret = ValueHandler.NOT_READABLE_VALUE;
        }
        catch(ValueHandlerException vhe)
        {
            LOG.warn("Could not get value for item '" + item + "' and language '" + languageIso + "'.");
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Could not get value for item '" + item + "' and language '" + languageIso + "'.", (Throwable)vhe);
            }
        }
        return ret;
    }


    public void setValue(TypedObject item, Object value) throws ValueHandlerException
    {
        setValue(item, value, UISessionUtils.getCurrentSession().getSystemService().getCurrentLanguage().getIsocode());
    }


    public void setValue(TypedObject item, Object value, String languageIso) throws ValueHandlerException
    {
        ObjectValueContainer currentObjectValues = getValueContainer((ObjectType)item.getType(), item);
        Object oldValue = getValue(item, languageIso);
        currentObjectValues.addValue(this.propertyDescriptor, this.propertyDescriptor.isLocalized() ? languageIso : null, oldValue);
        currentObjectValues.getValue(this.propertyDescriptor, this.propertyDescriptor.isLocalized() ? languageIso : null)
                        .setLocalValue(value);
        if(currentObjectValues.isModified())
        {
            for(ObjectValueHandler valueHandler : UISessionUtils.getCurrentSession().getValueHandlerRegistry()
                            .getValueHandlerChain((ObjectType)item.getType()))
            {
                valueHandler.storeValues(currentObjectValues);
            }
            UndoTools.addUndoOperationAndEvent(UISessionUtils.getCurrentSession().getUndoManager(), (UndoableOperation)new ItemChangeUndoableOperation(item, currentObjectValues), this);
        }
    }


    public ObjectValueContainer getValueContainer(ObjectType type, TypedObject object)
    {
        return new ObjectValueContainer(type, object.getObject());
    }
}
