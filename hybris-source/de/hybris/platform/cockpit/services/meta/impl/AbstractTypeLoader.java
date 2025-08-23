package de.hybris.platform.cockpit.services.meta.impl;

import de.hybris.platform.cockpit.model.meta.ExtendedType;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.ItemType;
import de.hybris.platform.cockpit.services.meta.ExtendedTypeLoader;
import de.hybris.platform.cockpit.services.meta.PropertyService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractTypeLoader implements ExtendedTypeLoader
{
    private TypeService typeService;
    private ModelService modelService;
    private PropertyService propertyService;


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setCockpitPropertyService(PropertyService propertyService)
    {
        this.propertyService = propertyService;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected PropertyService getPropertyService()
    {
        return this.propertyService;
    }


    public List<Object> getAvailableValues(TypeModel valueType, PropertyDescriptor propertyDescriptor, TypeService typeService)
    {
        return null;
    }


    public Collection<ExtendedType> getExtendedTypesForTemplate(ItemType type, String templateCode, TypeService typeService)
    {
        return null;
    }


    public TypeModel getValueType(ObjectType enclosingType, PropertyDescriptor propertyDescriptor, TypeService typeService)
    {
        return null;
    }


    public String getAttributeCodeFromPropertyQualifier(String propertyQualifier)
    {
        return null;
    }


    public String getTypeCodeFromPropertyQualifier(String propertyQualifier)
    {
        return null;
    }
}
