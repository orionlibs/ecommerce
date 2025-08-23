package de.hybris.platform.cockpit.services.search.impl;

import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.ClassAttributePropertyDescriptor;
import de.hybris.platform.cockpit.model.search.Operator;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import java.util.ArrayList;
import java.util.List;

public class ClassAttributeSearchDescriptor implements GenericSearchParameterDescriptor
{
    private final ClassAttributePropertyDescriptor propertyDescriptor;
    private List<Operator> operators = null;
    private boolean simpleSearchProperty = false;
    private GenericQueryClassParameterCreator queryCreator = null;


    public ClassAttributeSearchDescriptor(ClassAttributePropertyDescriptor propertyDescriptor)
    {
        this.propertyDescriptor = propertyDescriptor;
    }


    protected GenericQueryClassParameterCreator getQueryCreator()
    {
        if(this.queryCreator == null)
        {
            this.queryCreator = new GenericQueryClassParameterCreator(this);
        }
        return this.queryCreator;
    }


    public ClassAttributePropertyDescriptor getPropertyDescriptor()
    {
        return this.propertyDescriptor;
    }


    public GenericCondition createCondition(GenericQuery query, Object value, Operator operator)
    {
        return getQueryCreator().createCondition(value, operator);
    }


    public ComposedTypeModel getRequiredComposedType()
    {
        return null;
    }


    public boolean isSimpleSearchProperty()
    {
        return this.simpleSearchProperty;
    }


    public void setSimpleSearchProperty(boolean simpleSearchProperty)
    {
        this.simpleSearchProperty = simpleSearchProperty;
    }


    public void setOperators(List<Operator> operators)
    {
        this.operators = (operators == null) ? null : new ArrayList<>(operators);
    }


    public List<Operator> getOperators()
    {
        if(this.operators == null || this.operators.isEmpty())
        {
            this.operators = new ArrayList<>();
            this.operators.add(getDefaultOperator());
        }
        return this.operators;
    }


    public Operator getDefaultOperator()
    {
        return Operator.EQUALS;
    }


    @Deprecated
    public ClassAttributeAssignment getAttributeAssignment()
    {
        return this.propertyDescriptor.getAttributeAssignment();
    }


    public ClassAttributeAssignmentModel getClassAttributeAssignment()
    {
        return (ClassAttributeAssignmentModel)TypeTools.getModelService().get(this.propertyDescriptor.getAttributeAssignment());
    }


    public String getAttributeQualifier()
    {
        return this.propertyDescriptor.getAttributeQualifier();
    }


    public String getDescription()
    {
        return this.propertyDescriptor.getDescription();
    }


    public String getEditorType()
    {
        return this.propertyDescriptor.getEditorType();
    }


    public PropertyDescriptor.Multiplicity getMultiplicity()
    {
        return this.propertyDescriptor.getMultiplicity();
    }


    public String getName()
    {
        return this.propertyDescriptor.getName();
    }


    public PropertyDescriptor.Occurrence getOccurence()
    {
        return this.propertyDescriptor.getOccurence();
    }


    public String getQualifier()
    {
        return this.propertyDescriptor.getQualifier();
    }


    public boolean isLocalized()
    {
        return this.propertyDescriptor.isLocalized();
    }


    public String getName(String languageIso)
    {
        return this.propertyDescriptor.getName(languageIso);
    }


    public boolean isReadable()
    {
        return this.propertyDescriptor.isReadable();
    }


    public boolean isWritable()
    {
        return this.propertyDescriptor.isWritable();
    }


    public String getSelectionOf()
    {
        return null;
    }
}
