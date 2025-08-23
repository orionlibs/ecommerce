package de.hybris.platform.cockpit.model.meta.impl;

import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ExtendedType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collection;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultTypedObject extends AbstractTypedObject
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultTypedObject.class);
    private Collection<ObjectTemplate> assignedTemplates = null;
    private Collection<ObjectTemplate> potentialTemplates = null;
    private Collection<ExtendedType> extendedTypes = null;


    public DefaultTypedObject(BaseType type, Object object)
    {
        if(type == null)
        {
            throw new IllegalArgumentException("type was null");
        }
        this.type = type;
        this.object = object;
    }


    public Object getObject()
    {
        Object ret = this.object;
        if(ret != null && ret instanceof ItemModel)
        {
            PK pk = ((ItemModel)ret).getPk();
            if(pk != null)
            {
                if(getModelService().isRemoved(ret))
                {
                    ret = null;
                    LOG.warn("Can not retrieve object. Reason: Item has been removed.");
                }
                else if(!(this.type instanceof ItemType) || !((ItemType)this.type).isJaloOnly())
                {
                    ret = getModelService().get(pk);
                }
            }
        }
        return ret;
    }


    public ModelService getModelService()
    {
        return TypeTools.getModelService();
    }


    public BaseType getType()
    {
        return this.type;
    }


    public boolean instanceOf(ObjectType type)
    {
        return (type != null && getType().isAssignableFrom(type));
    }


    public void setAssignedTemplates(Collection<ObjectTemplate> assignedTemplates)
    {
        this.assignedTemplates = assignedTemplates;
    }


    public Collection<ObjectTemplate> getAssignedTemplates()
    {
        return (this.assignedTemplates == null) ? Collections.EMPTY_LIST : this.assignedTemplates;
    }


    public void setExtendedTypes(Collection<ExtendedType> extendedTypes)
    {
        this.extendedTypes = extendedTypes;
    }


    public Collection<ExtendedType> getExtendedTypes()
    {
        return (this.extendedTypes == null) ? Collections.EMPTY_LIST : this.extendedTypes;
    }


    public void setPotentialTemplates(Collection<ObjectTemplate> potentialTemplates)
    {
        this.potentialTemplates = potentialTemplates;
    }


    public Collection<ObjectTemplate> getPotentialTemplates()
    {
        return (this.potentialTemplates == null) ? Collections.EMPTY_LIST : this.potentialTemplates;
    }
}
