package de.hybris.platform.warehousing.atp.handlers;

import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.warehousing.model.AtpFormulaModel;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class AtpFormulaStringHandler implements DynamicAttributeHandler<String, AtpFormulaModel>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AtpFormulaStringHandler.class);
    protected static final String AVAILABILITY = "availability";
    protected static final List<String> operatorList = Arrays.asList(new String[] {"+", "-"});
    private Map<String, String> atpFormulaVar2ArithmeticOperatorMap;


    protected static Predicate<PropertyDescriptor> isAvailable()
    {
        return propertyDescriptor -> "availability".equalsIgnoreCase(propertyDescriptor.getName());
    }


    public String get(AtpFormulaModel atpFormulaModel)
    {
        StringBuilder formulaString = new StringBuilder();
        Set<PropertyDescriptor> propertyDescriptors = new HashSet<>();
        if(atpFormulaModel != null)
        {
            try
            {
                Objects.requireNonNull(propertyDescriptors);
                Arrays.<PropertyDescriptor>stream(Introspector.getBeanInfo(atpFormulaModel.getClass()).getPropertyDescriptors()).filter(descriptor -> descriptor.getPropertyType().equals(Boolean.class)).forEach(propertyDescriptors::add);
            }
            catch(IntrospectionException e)
            {
                LOGGER.error("Failed to interpret the ATP formula");
            }
        }
        prepareFormulaString(atpFormulaModel, formulaString, propertyDescriptors);
        return formulaString.toString();
    }


    protected void prepareFormulaString(AtpFormulaModel atpFormulaModel, StringBuilder formulaString, Set<PropertyDescriptor> propertyDescriptors)
    {
        if(CollectionUtils.isNotEmpty(propertyDescriptors))
        {
            Optional<PropertyDescriptor> availablePropertyDesc = propertyDescriptors.stream().filter(isAvailable()).findFirst();
            if(availablePropertyDesc.isPresent() && atpFormulaModel.getAvailability().booleanValue())
            {
                formulaString
                                .append(getAtpFormulaVar2ArithmeticOperatorMap().get(((PropertyDescriptor)availablePropertyDesc.get()).getName().toLowerCase()))
                                .append(StringUtils.capitalize(((PropertyDescriptor)availablePropertyDesc.get()).getName().toLowerCase()));
            }
            propertyDescriptors.stream().filter(isAvailable().negate()).forEach(formulaVarPropDescriptor -> interpretFormulaVariable(atpFormulaModel, formulaString, formulaVarPropDescriptor));
        }
    }


    protected void interpretFormulaVariable(AtpFormulaModel atpFormulaModel, StringBuilder formulaString, PropertyDescriptor formulaVarPropDescriptor)
    {
        try
        {
            Boolean formulaVarValue = (Boolean)formulaVarPropDescriptor.getReadMethod().invoke(atpFormulaModel, new Object[0]);
            if(formulaVarValue != null && formulaVarValue.booleanValue())
            {
                String atpFormulaVar = formulaVarPropDescriptor.getName().toLowerCase();
                if(getAtpFormulaVar2ArithmeticOperatorMap().get(atpFormulaVar) != null && operatorList
                                .contains(getAtpFormulaVar2ArithmeticOperatorMap().get(atpFormulaVar)))
                {
                    formulaString.append(getAtpFormulaVar2ArithmeticOperatorMap().get(atpFormulaVar))
                                    .append(StringUtils.capitalize(atpFormulaVar));
                }
                else
                {
                    LOGGER.error("Failed to interpret the Arithmetic sign for ATP formula variable: [{}] -> [{}]. Please update your formula variable with appropriate sign (+ or -) in atpFormulaVar2ArithmeticOperatorMap", atpFormulaVar,
                                    getAtpFormulaVar2ArithmeticOperatorMap().get(atpFormulaVar));
                }
            }
        }
        catch(IllegalAccessException | java.lang.reflect.InvocationTargetException e)
        {
            LOGGER.error("Failed to interpret the ATP formula. Please review your formula variable: {}", formulaVarPropDescriptor
                            .getName());
        }
    }


    public void set(AtpFormulaModel atpFormulaModel, String formulaString)
    {
        throw new UnsupportedOperationException();
    }


    protected Map<String, String> getAtpFormulaVar2ArithmeticOperatorMap()
    {
        return this.atpFormulaVar2ArithmeticOperatorMap;
    }


    @Required
    public void setAtpFormulaVar2ArithmeticOperatorMap(Map<String, String> atpFormulaVar2ArithmeticOperatorMap)
    {
        this.atpFormulaVar2ArithmeticOperatorMap = atpFormulaVar2ArithmeticOperatorMap;
    }
}
