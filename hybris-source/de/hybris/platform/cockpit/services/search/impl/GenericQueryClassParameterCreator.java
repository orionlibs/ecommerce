package de.hybris.platform.cockpit.services.search.impl;

import de.hybris.platform.catalog.jalo.classification.util.FeatureValueCondition;
import de.hybris.platform.catalog.jalo.classification.util.TypedFeature;
import de.hybris.platform.cockpit.model.search.Operator;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.ArrayList;
import java.util.List;

public class GenericQueryClassParameterCreator extends GenericQueryParameterCreator
{
    private List<Operator> supportedOperators = null;


    protected GenericQueryClassParameterCreator(ClassAttributeSearchDescriptor descriptor)
    {
        super((GenericSearchParameterDescriptor)descriptor);
    }


    public ClassAttributeSearchDescriptor getDescriptor()
    {
        return (ClassAttributeSearchDescriptor)super.getDescriptor();
    }


    protected GenericCondition createSingleTokenCondition(String typeCode, String attribute, Object value, Operator operator)
    {
        FeatureValueCondition featureValueCondition;
        if(operator == null)
        {
            GenericCondition ret = GenericCondition.equals(attribute, value);
        }
        else if("contains".equalsIgnoreCase(operator.getQualifier()) &&
                        getSupportedOperators().contains(Operator.CONTAINS))
        {
            featureValueCondition = FeatureValueCondition.contains(getDescriptor().getAttributeAssignment(), value.toString());
        }
        else if("startswith".equalsIgnoreCase(operator.getQualifier()) &&
                        getSupportedOperators().contains(Operator.STARTS_WITH))
        {
            featureValueCondition = FeatureValueCondition.startsWith(getDescriptor().getAttributeAssignment(), value.toString());
        }
        else if("endswith".equalsIgnoreCase(operator.getQualifier()) &&
                        getSupportedOperators().contains(Operator.ENDS_WITH))
        {
            featureValueCondition = FeatureValueCondition.endsWith(getDescriptor().getAttributeAssignment(), value.toString());
        }
        else if("equals".equalsIgnoreCase(operator.getQualifier()) &&
                        getSupportedOperators().contains(Operator.EQUAL))
        {
            EnumerationValue type = getDescriptor().getAttributeAssignment().getAttributeType();
            if(type != null && "string".equals(type.getCode()))
            {
                featureValueCondition = FeatureValueCondition.contains(getDescriptor().getAttributeAssignment(), value.toString());
            }
            else
            {
                featureValueCondition = FeatureValueCondition.equals(getDescriptor().getAttributeAssignment(), value);
            }
        }
        else if("greater".equalsIgnoreCase(operator.getQualifier()) &&
                        getSupportedOperators().contains(Operator.GREATER))
        {
            featureValueCondition = FeatureValueCondition.greater(getDescriptor().getAttributeAssignment(), value);
        }
        else if("less".equalsIgnoreCase(operator.getQualifier()) &&
                        getSupportedOperators().contains(Operator.LESS))
        {
            featureValueCondition = FeatureValueCondition.less(getDescriptor().getAttributeAssignment(), value);
        }
        else
        {
            throw new IllegalArgumentException("Unsupported operator " + operator.getQualifier() + " for descriptor " +
                            getDescriptor().getQualifier());
        }
        return (GenericCondition)featureValueCondition;
    }


    protected List<Operator> getSupportedOperators()
    {
        if(this.supportedOperators == null)
        {
            this
                            .supportedOperators = new ArrayList<>(FeatureValueCondition.getValidOperators(TypedFeature.FeatureType.toEnum(getDescriptor().getAttributeAssignment()
                            .getAttributeType())));
            this.supportedOperators.remove(Operator.IN);
            this.supportedOperators.remove(Operator.NOT_IN);
        }
        return this.supportedOperators;
    }
}
