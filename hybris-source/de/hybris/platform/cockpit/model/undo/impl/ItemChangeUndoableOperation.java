package de.hybris.platform.cockpit.model.undo.impl;

import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.undo.UndoableOperation;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ObjectValueHandler;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UndoTools;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

public class ItemChangeUndoableOperation implements UndoableOperation
{
    protected TypedObject typedObject;
    protected ObjectValueContainer valueContainer;
    protected ObjectValueContainer redoValueContainer;
    protected String undoPresentationName;
    protected String redoPresentationName;


    public ItemChangeUndoableOperation(TypedObject typedObject, ObjectValueContainer valueContainer)
    {
        this.typedObject = typedObject;
        initUndoValueContainer(valueContainer);
    }


    private void initUndoValueContainer(ObjectValueContainer initContainer)
    {
        this.valueContainer = new ObjectValueContainer(initContainer.getType(), initContainer.getObject());
        for(ObjectValueContainer.ObjectValueHolder value : initContainer.getModifiedValues())
        {
            this.valueContainer.addValue(value.getPropertyDescriptor(), value.getLanguageIso(), value.getLocalValue())
                            .setLocalValue(value.getOriginalValue());
        }
    }


    public boolean canUndo()
    {
        return true;
    }


    public boolean canRedo()
    {
        return true;
    }


    public String getUndoPresentationName()
    {
        if(StringUtils.isBlank(this.undoPresentationName))
        {
            int counter = 0;
            StringBuilder name = new StringBuilder();
            for(ObjectValueContainer.ObjectValueHolder value : this.valueContainer.getModifiedValues())
            {
                if(counter > 0)
                {
                    name.append(", ");
                }
                name.append(getPropertyName(value.getPropertyDescriptor()));
                if(counter >= 3)
                {
                    name.append("...");
                    break;
                }
                counter++;
            }
            this.undoPresentationName = name.toString();
        }
        return this.undoPresentationName;
    }


    public String getRedoPresentationName()
    {
        if(StringUtils.isBlank(this.redoPresentationName))
        {
            this.redoPresentationName = getUndoPresentationName();
        }
        return this.redoPresentationName;
    }


    public void undo() throws CannotUndoException
    {
        if(UndoTools.isItemValid(this.typedObject))
        {
            for(ObjectValueHandler valueHandler : UISessionUtils.getCurrentSession().getValueHandlerRegistry()
                            .getValueHandlerChain((ObjectType)this.typedObject.getType()))
            {
                try
                {
                    valueHandler.storeValues(this.valueContainer);
                }
                catch(ValueHandlerException e)
                {
                    throw new CannotUndoException(e.getLocalizedMessage(), e);
                }
            }
            Set<PropertyDescriptor> modifiedProperties = new HashSet<>();
            for(ObjectValueContainer.ObjectValueHolder value : this.valueContainer.getAllValues())
            {
                if(value.isModified())
                {
                    modifiedProperties.add(value.getPropertyDescriptor());
                }
            }
            if(!modifiedProperties.isEmpty())
            {
                UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this, this.typedObject, modifiedProperties));
            }
        }
    }


    public void redo() throws CannotRedoException
    {
        if(UndoTools.isItemValid(this.typedObject))
        {
            ObjectValueContainer redoContainer = getRedoValueContainer();
            for(ObjectValueHandler valueHandler : UISessionUtils.getCurrentSession().getValueHandlerRegistry()
                            .getValueHandlerChain((ObjectType)this.typedObject.getType()))
            {
                try
                {
                    valueHandler.storeValues(redoContainer);
                }
                catch(ValueHandlerException e)
                {
                    throw new CannotRedoException(e.getLocalizedMessage(), e);
                }
            }
            Set<PropertyDescriptor> modifiedProperties = new HashSet<>();
            for(ObjectValueContainer.ObjectValueHolder value : redoContainer.getAllValues())
            {
                if(value.isModified())
                {
                    modifiedProperties.add(value.getPropertyDescriptor());
                }
            }
            if(!modifiedProperties.isEmpty())
            {
                UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(this, this.typedObject, modifiedProperties));
            }
        }
    }


    protected ObjectValueContainer getRedoValueContainer()
    {
        if(this.redoValueContainer == null)
        {
            this.redoValueContainer = new ObjectValueContainer(this.valueContainer.getType(), this.valueContainer.getObject());
            for(ObjectValueContainer.ObjectValueHolder value : this.valueContainer.getAllValues())
            {
                if(value.isModified())
                {
                    this.redoValueContainer.addValue(value.getPropertyDescriptor(), value.getLanguageIso(), value.getLocalValue())
                                    .setLocalValue(value.getOriginalValue());
                }
            }
        }
        return this.redoValueContainer;
    }


    private String getPropertyName(PropertyDescriptor propertyDescriptor)
    {
        String name = propertyDescriptor.getName();
        if(StringUtils.isBlank(name))
        {
            name = UISessionUtils.getCurrentSession().getTypeService().getAttributeCodeFromPropertyQualifier(propertyDescriptor.getQualifier());
        }
        return name;
    }


    public String getUndoContextDescription()
    {
        String contextDescr = "n/a";
        if(UndoTools.isItemValid(this.typedObject))
        {
            contextDescr = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(this.typedObject);
        }
        return contextDescr;
    }


    public String getRedoContextDescription()
    {
        return getUndoContextDescription();
    }
}
