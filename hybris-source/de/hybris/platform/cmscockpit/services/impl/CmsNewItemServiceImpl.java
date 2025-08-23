package de.hybris.platform.cmscockpit.services.impl;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.impl.WorkflowNewItemServiceImpl;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;

public class CmsNewItemServiceImpl extends WorkflowNewItemServiceImpl
{
    public TypedObject createNewItem(ObjectValueContainer valueContainer, ObjectTemplate template) throws ValueHandlerException
    {
        setTemplate(valueContainer, template);
        return super.createNewItem(valueContainer, template);
    }
}
