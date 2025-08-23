package de.hybris.platform.cockpit.services.search.impl;

import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.util.FeatureValueCondition;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.search.Operator;
import de.hybris.platform.cockpit.model.search.SearchParameterDescriptor;
import de.hybris.platform.cockpit.model.search.SearchParameterValue;
import de.hybris.platform.cockpit.services.search.ConditionTranslatorContext;
import de.hybris.platform.cockpit.services.search.GenericQueryConditionTranslator;
import de.hybris.platform.core.CoreAlgorithms;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class FeatureGenericQueryConditionTranslator implements GenericQueryConditionTranslator
{
    private ModelService modelService;


    public GenericCondition translate(SearchParameterValue paramValue, ConditionTranslatorContext ctx)
    {
        GenericConditionList genericConditionList;
        GenericCondition ret = null;
        SearchParameterDescriptor searchDescriptor = paramValue.getParameterDescriptor();
        if(searchDescriptor instanceof ClassAttributeSearchDescriptor && paramValue.getValue() instanceof FeatureValue)
        {
            Operator operator = paramValue.getOperator();
            FeatureValue featureValue = (FeatureValue)paramValue.getValue();
            ClassAttributeAssignmentModel assignmentModel = ((ClassAttributeSearchDescriptor)searchDescriptor).getClassAttributeAssignment();
            ClassAttributeAssignment assignment = toClassAttributeAssignment(assignmentModel);
            if(operator == null || Operator.EQUALS.equals(operator))
            {
                ClassificationAttributeTypeEnum type = assignmentModel.getAttributeType();
                if(type != null && ClassificationAttributeTypeEnum.STRING.equals(type))
                {
                    FeatureValueCondition featureValueCondition = FeatureValueCondition.contains(assignment, valueToString(featureValue.getValue()));
                }
                else
                {
                    FeatureValueCondition featureValueCondition = FeatureValueCondition.equals(assignment,
                                    prepareValue(adjustValueIfUnitDiffers(featureValue, assignmentModel.getUnit()).getValue()));
                }
            }
            else if(Operator.CONTAINS.equals(operator))
            {
                FeatureValueCondition featureValueCondition = FeatureValueCondition.contains(((ClassAttributeSearchDescriptor)searchDescriptor).getAttributeAssignment(),
                                valueToString(featureValue.getValue()));
            }
        }
        else if(searchDescriptor instanceof ClassAttributeSearchDescriptor && paramValue.getValue() instanceof Collection &&
                        !((Collection)paramValue.getValue()).isEmpty())
        {
            Operator operator = paramValue.getOperator();
            if(Operator.BETWEEN.equals(operator))
            {
                Collection<FeatureValue> values = (Collection<FeatureValue>)paramValue.getValue();
                Iterator<FeatureValue> iterator = values.iterator();
                FeatureValue first = iterator.next();
                FeatureValue second = iterator.next();
                List<GenericCondition> conditions = new ArrayList<>();
                ClassAttributeAssignmentModel assignmentModel = ((ClassAttributeSearchDescriptor)searchDescriptor).getClassAttributeAssignment();
                ClassAttributeAssignment assignment = toClassAttributeAssignment(assignmentModel);
                if(paramValue.getParameterDescriptor().getMultiplicity() == PropertyDescriptor.Multiplicity.RANGE)
                {
                    if(first != null)
                    {
                        conditions.add(createRangeValueGreaterEqualCondition(
                                        adjustValueIfUnitDiffers(first, assignmentModel.getUnit()), assignment));
                    }
                    if(second != null)
                    {
                        conditions.add(createRangeValueLessEqualCondition(adjustValueIfUnitDiffers(second, assignmentModel.getUnit()), assignment));
                    }
                }
                else
                {
                    if(first != null)
                    {
                        conditions.add(createSingleValueGreaterEqualCondition(first, assignment));
                    }
                    if(second != null)
                    {
                        conditions.add(createSingleValueLessEqualCondition(second, assignment));
                    }
                }
                if(!conditions.isEmpty())
                {
                    genericConditionList = FeatureValueCondition.and(conditions);
                }
            }
        }
        return (GenericCondition)genericConditionList;
    }


    protected ClassAttributeAssignment toClassAttributeAssignment(ClassAttributeAssignmentModel assignmentModel)
    {
        return (ClassAttributeAssignment)getModelService().getSource(assignmentModel);
    }


    private GenericCondition createRangeValueGreaterEqualCondition(FeatureValue value, ClassAttributeAssignment assignment)
    {
        return (GenericCondition)FeatureValueCondition.or(new GenericCondition[] {(GenericCondition)FeatureValueCondition.greaterForRange(assignment, prepareValue(value.getValue()), FeatureValueCondition.RangeBoundary.LOW),
                        (GenericCondition)FeatureValueCondition.equalsForRange(assignment, prepareValue(value.getValue()), FeatureValueCondition.RangeBoundary.LOW)});
    }


    private GenericCondition createRangeValueLessEqualCondition(FeatureValue value, ClassAttributeAssignment assignment)
    {
        return (GenericCondition)FeatureValueCondition.or(new GenericCondition[] {(GenericCondition)FeatureValueCondition.lessForRange(assignment, prepareValue(value.getValue()), FeatureValueCondition.RangeBoundary.UP),
                        (GenericCondition)FeatureValueCondition.equalsForRange(assignment, prepareValue(value.getValue()), FeatureValueCondition.RangeBoundary.UP)});
    }


    private GenericCondition createSingleValueGreaterEqualCondition(FeatureValue value, ClassAttributeAssignment assignment)
    {
        return (GenericCondition)FeatureValueCondition.or(new GenericCondition[] {(GenericCondition)FeatureValueCondition.greater(assignment, prepareValue(value.getValue())),
                        (GenericCondition)FeatureValueCondition.equals(assignment, prepareValue(value.getValue()))});
    }


    private GenericCondition createSingleValueLessEqualCondition(FeatureValue value, ClassAttributeAssignment assignment)
    {
        return (GenericCondition)FeatureValueCondition.or(new GenericCondition[] {(GenericCondition)FeatureValueCondition.less(assignment, prepareValue(value.getValue())),
                        (GenericCondition)FeatureValueCondition.equals(assignment, prepareValue(value.getValue()))});
    }


    private String valueToString(Object value)
    {
        return (value == null) ? null : value.toString();
    }


    private Object prepareValue(Object value)
    {
        if(value == null)
        {
            return null;
        }
        if(value instanceof de.hybris.platform.core.model.ItemModel)
        {
            return getModelService().getSource(value);
        }
        return value;
    }


    private FeatureValue adjustValueIfUnitDiffers(FeatureValue featureValue, ClassificationAttributeUnitModel expectedUnit)
    {
        if(featureValue != null)
        {
            ClassificationAttributeUnitModel featureUnit = featureValue.getUnit();
            if(featureUnit != null && expectedUnit != null && !expectedUnit.equals(featureUnit))
            {
                if(featureValue.getValue() instanceof Number)
                {
                    double featureUnitConvertionFactor = featureUnit.getConversionFactor().doubleValue();
                    double expectedUnitConvertionFactor = expectedUnit.getConversionFactor().doubleValue();
                    double value = ((Number)featureValue.getValue()).doubleValue();
                    Number convertedValue = Double.valueOf(CoreAlgorithms.convert(featureUnitConvertionFactor, expectedUnitConvertionFactor, value));
                    return new FeatureValue(convertedValue, featureValue.getDescription(), expectedUnit);
                }
            }
            return featureValue;
        }
        return null;
    }


    private ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
