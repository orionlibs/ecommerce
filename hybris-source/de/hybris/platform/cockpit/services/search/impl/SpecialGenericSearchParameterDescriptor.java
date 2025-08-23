package de.hybris.platform.cockpit.services.search.impl;

import de.hybris.platform.cockpit.model.search.Operator;
import de.hybris.platform.cockpit.model.search.impl.AbstractSearchParameterDescriptor;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import org.springframework.beans.factory.annotation.Required;

public class SpecialGenericSearchParameterDescriptor extends AbstractSearchParameterDescriptor implements GenericSearchParameterDescriptor
{
    private String requiredComposedType;
    private String attributeQualifier;
    private boolean simpleSearchProperty;
    private GenericQueryParameterCreator queryCreator;


    protected GenericQueryParameterCreator createCreator()
    {
        return new GenericQueryParameterCreator(this);
    }


    protected GenericQueryParameterCreator getQueryCreator()
    {
        if(this.queryCreator == null)
        {
            this.queryCreator = createCreator();
        }
        return this.queryCreator;
    }


    public String getRequiredComposedTypeCode()
    {
        return this.requiredComposedType;
    }


    @Required
    public void setRequiredComposedTypeCode(String requiredComposedType)
    {
        this.requiredComposedType = requiredComposedType;
    }


    public String getAttributeQualifier()
    {
        return (this.attributeQualifier == null) ? getQualifier() : this.attributeQualifier;
    }


    public void setAttributeQualifier(String attributeQualifier)
    {
        this.attributeQualifier = attributeQualifier;
    }


    public boolean isSimpleSearchProperty()
    {
        return this.simpleSearchProperty;
    }


    public void setSimpleSearchProperty(boolean simpleSearchProperty)
    {
        this.simpleSearchProperty = simpleSearchProperty;
    }


    public ComposedTypeModel getRequiredComposedType()
    {
        return (this.requiredComposedType == null) ? null : TypeTools.getCoreTypeService().getComposedType(this.requiredComposedType);
    }


    public GenericCondition createCondition(GenericQuery query, Object value, Operator operator)
    {
        return getQueryCreator().createCondition(value, operator);
    }


    public Operator getDefaultOperator()
    {
        return (getOperators() == null || getOperators().isEmpty()) ? null : getOperators().get(0);
    }


    public String getName(String languageIso)
    {
        return this.attributeQualifier;
    }


    public boolean isReadable()
    {
        return true;
    }


    public boolean isWritable()
    {
        return false;
    }
}
