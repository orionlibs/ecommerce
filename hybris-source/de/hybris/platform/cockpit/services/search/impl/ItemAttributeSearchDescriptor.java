package de.hybris.platform.cockpit.services.search.impl;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.model.search.Operator;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemAttributeSearchDescriptor implements GenericSearchParameterDescriptor
{
    private List<Operator> operators;
    private boolean simpleSearchProperty;
    private GenericQueryParameterCreator queryCreator;
    private final ItemAttributePropertyDescriptor propertyDescriptor;


    public ItemAttributeSearchDescriptor(ItemAttributePropertyDescriptor propertyDescriptor)
    {
        this.propertyDescriptor = propertyDescriptor;
    }


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


    public GenericCondition createCondition(GenericQuery query, Object value, Operator operator)
    {
        if(getAttributeDescriptors().size() > 1)
        {
            List<AttributeDescriptorModel> ads = getAttributeDescriptors();
            int index = 0;
            String prevTypeCode = null;
            for(AttributeDescriptorModel ad : getAttributeDescriptors())
            {
                if(index++ < ads.size() - 1 && ad.getAttributeType() instanceof ComposedTypeModel)
                {
                    ComposedTypeModel newType = (ComposedTypeModel)ad.getAttributeType();
                    query.addOuterJoin(newType.getCode(),
                                    GenericCondition.createJoinCondition(new GenericSearchField(prevTypeCode, ad.getQualifier()), new GenericSearchField(newType
                                                    .getCode(), "pk")));
                    prevTypeCode = newType.getCode();
                }
            }
        }
        return getQueryCreator().createCondition(value, operator);
    }


    public ComposedTypeModel getRequiredComposedType()
    {
        return this.propertyDescriptor.getEnclosingType();
    }


    public boolean isSimpleSearchProperty()
    {
        return this.simpleSearchProperty;
    }


    public void setSimpleSearchProperty(boolean simpleSearchProperty)
    {
        this.simpleSearchProperty = simpleSearchProperty;
    }


    @Deprecated
    public void setEditorType(String type)
    {
        this.propertyDescriptor.setEditorType(type);
    }


    public void setOperators(List<Operator> operators)
    {
        this.operators = (operators == null) ? null : new ArrayList<>(operators);
    }


    public List<Operator> getOperators()
    {
        return (this.operators == null) ? Collections.EMPTY_LIST : Collections.<Operator>unmodifiableList(this.operators);
    }


    public Operator getDefaultOperator()
    {
        Operator operator = null;
        if(getEditorType().equals("TEXT"))
        {
            operator = Operator.CONTAINS;
        }
        else if(getEditorType().equals("INTEGER"))
        {
            operator = Operator.EQUALS;
        }
        else if(getEditorType().equals("BOOLEAN"))
        {
            operator = Operator.EQUALS;
        }
        else if(getEditorType().equals("DATE"))
        {
            operator = Operator.EQUALS;
        }
        else if(getEditorType().equals("DECIMAL"))
        {
            operator = Operator.EQUALS;
        }
        else if(getEditorType().equals("ENUM"))
        {
            operator = Operator.EQUALS;
        }
        else if(getEditorType().equals("REFERENCE"))
        {
            operator = Operator.EQUALS;
        }
        else if(getEditorType().equals("PK"))
        {
            operator = Operator.EQUALS;
        }
        return operator;
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


    public String getAttributeQualifier()
    {
        return this.propertyDescriptor.getAttributeQualifier();
    }


    public List<AttributeDescriptorModel> getAttributeDescriptors()
    {
        return this.propertyDescriptor.getAttributeDescriptors();
    }


    public AttributeDescriptorModel getFirstAttributeDescriptor()
    {
        return this.propertyDescriptor.getFirstAttributeDescriptor();
    }


    public AttributeDescriptorModel getLastAttributeDescriptor()
    {
        return this.propertyDescriptor.getLastAttributeDescriptor();
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


    public boolean equals(Object obj)
    {
        if(!(obj instanceof ItemAttributeSearchDescriptor))
        {
            return false;
        }
        ItemAttributeSearchDescriptor other = (ItemAttributeSearchDescriptor)obj;
        return (this.simpleSearchProperty == other.simpleSearchProperty && (this.propertyDescriptor == other.propertyDescriptor || (this.propertyDescriptor != null && this.propertyDescriptor
                        .equals(other.propertyDescriptor))) &&
                        compareOpList(other.operators));
    }


    private boolean compareOpList(List<Operator> ops)
    {
        if(this.operators == null)
        {
            return (ops == null);
        }
        if(ops == null || this.operators.size() != ops.size())
        {
            return false;
        }
        for(int i = 0; i < this.operators.size(); i++)
        {
            if(!((Operator)this.operators.get(i)).equals(ops.get(i)))
            {
                return false;
            }
        }
        return true;
    }


    public int hashCode()
    {
        return this.propertyDescriptor.hashCode() + Boolean.valueOf(this.simpleSearchProperty).hashCode();
    }


    public String getSelectionOf()
    {
        return null;
    }
}
