package de.hybris.platform.warehousing.atp.formula.services.impl;

import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.warehousing.atp.dao.AvailableToPromiseDao;
import de.hybris.platform.warehousing.atp.formula.dao.AtpFormulaDao;
import de.hybris.platform.warehousing.atp.formula.services.AtpFormulaService;
import de.hybris.platform.warehousing.model.AtpFormulaModel;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAtpFormulaService implements AtpFormulaService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAtpFormulaService.class);
    protected static final String EXTERNAL = "external";
    protected static final String RETURNED = "returned";
    protected static final List<String> operatorList = Arrays.asList(new String[] {"+", "-"});
    private AvailableToPromiseDao availableToPromiseDao;
    private BaseStoreService baseStoreService;
    private ModelService modelService;
    private AtpFormulaDao atpFormulaDao;
    private Map<String, String> atpFormulaVar2ArithmeticOperatorMap;


    public AtpFormulaModel getAtpFormulaByCode(String formulaCode)
    {
        return null;
    }


    public Collection<AtpFormulaModel> getAllAtpFormula()
    {
        return getAtpFormulaDao().getAllAtpFormula();
    }


    public AtpFormulaModel createAtpFormula(AtpFormulaModel atpFormula)
    {
        return null;
    }


    public AtpFormulaModel updateAtpFormula(AtpFormulaModel atpFormula)
    {
        return null;
    }


    public void deleteAtpFormula(String formulaCode)
    {
        if(getBaseStoreService().getAllBaseStores().stream()
                        .anyMatch(baseStore -> baseStore.getDefaultAtpFormula().getCode().equals(formulaCode)))
        {
            LOGGER.info("The formula with code {} has not been deleted because it is use by at least one base store as a default formula.", formulaCode);
        }
        else
        {
            getModelService().remove(getAtpFormulaByCode(formulaCode));
            LOGGER.info("The formula with code {} has been deleted.", formulaCode);
        }
    }


    public Long getAtpValueFromFormula(AtpFormulaModel atpFormula, Map<String, Object> params)
    {
        List<Long> results = new ArrayList<>();
        results.add(Long.valueOf(0L));
        Set<PropertyDescriptor> propertyDescriptors = new HashSet<>();
        if(atpFormula != null)
        {
            try
            {
                Arrays.<PropertyDescriptor>stream(Introspector.getBeanInfo(atpFormula.getClass()).getPropertyDescriptors())
                                .filter(descriptor -> descriptor.getPropertyType().equals(Boolean.class))
                                .forEach(propertyDescriptor -> propertyDescriptors.add(propertyDescriptor));
            }
            catch(IntrospectionException e)
            {
                LOGGER.error("Sourcing failed to interpret the ATP formula.");
            }
            Collection<String> atpAvailabilityVariables = Arrays.asList(new String[] {"returned", "external"});
            propertyDescriptors.stream()
                            .filter(propertyDescriptor -> !atpAvailabilityVariables.contains(propertyDescriptor.getName().toLowerCase()))
                            .forEach(formulaVarPropDescriptor -> getAtpFormulaVariableValue(params, results, formulaVarPropDescriptor, atpFormula));
        }
        return Long.valueOf(results.stream().mapToLong(Long::longValue).sum());
    }


    protected List<Long> getAtpFormulaVariableValue(Map<String, Object> params, List<Long> results, PropertyDescriptor formulaVarPropDescriptor, AtpFormulaModel atpFormula)
    {
        boolean isExceptionCaught = false;
        List<Long> errorList = new ArrayList<>();
        try
        {
            Object formulaVarValue = formulaVarPropDescriptor.getReadMethod().invoke(atpFormula, new Object[0]);
            if(formulaVarValue instanceof Boolean)
            {
                Boolean isIncluded = (Boolean)formulaVarValue;
                if(isIncluded.booleanValue())
                {
                    String atpFormulaVar = formulaVarPropDescriptor.getName().toLowerCase();
                    loopThroughAtpMethods(params, results, atpFormulaVar);
                }
            }
        }
        catch(SecurityException | InvocationTargetException | IllegalAccessException e)
        {
            isExceptionCaught = true;
            errorList.add(Long.valueOf(0L));
            LOGGER.error("Sourcing failed to interpret the ATP formula. Please review your formula variable: {}", formulaVarPropDescriptor
                            .getName());
            LOGGER.info(String.valueOf(e));
        }
        return isExceptionCaught ? errorList : results;
    }


    protected void loopThroughAtpMethods(Map<String, Object> params, List<Long> results, String atpFormulaVar) throws IllegalAccessException, InvocationTargetException
    {
        if(getAtpFormulaVar2ArithmeticOperatorMap().get(atpFormulaVar) != null && operatorList
                        .contains(getAtpFormulaVar2ArithmeticOperatorMap().get(atpFormulaVar)))
        {
            for(Method method : getAvailableToPromiseDao().getClass().getMethods())
            {
                appendResults(params, results, atpFormulaVar, method);
            }
        }
        else
        {
            LOGGER.error("Failed to interpret the Arithmetic sign for ATP formula variable: [{}] -> [{}]. Please update your formula variable with appropriate sign (+ or -) in atpFormulaVar2ArithmeticOperatorMap", atpFormulaVar,
                            getAtpFormulaVar2ArithmeticOperatorMap().get(atpFormulaVar));
        }
    }


    protected void appendResults(Map<String, Object> params, List<Long> results, String atpFormulaVar, Method method) throws IllegalAccessException, InvocationTargetException
    {
        if(method.getName().toLowerCase().contains(atpFormulaVar))
        {
            Object result = method.invoke(getAvailableToPromiseDao(), new Object[] {params});
            if(result instanceof Long)
            {
                if("-".equals(getAtpFormulaVar2ArithmeticOperatorMap().get(atpFormulaVar)))
                {
                    result = Long.valueOf(((Long)result).longValue() * -1L);
                }
                results.add((Long)result);
                LOGGER.debug("ATP value calculated for {}: {}", atpFormulaVar, result);
            }
        }
    }


    protected AvailableToPromiseDao getAvailableToPromiseDao()
    {
        return this.availableToPromiseDao;
    }


    @Required
    public void setAvailableToPromiseDao(AvailableToPromiseDao availableToPromiseDao)
    {
        this.availableToPromiseDao = availableToPromiseDao;
    }


    protected BaseStoreService getBaseStoreService()
    {
        return this.baseStoreService;
    }


    @Required
    public void setBaseStoreService(BaseStoreService baseStoreService)
    {
        this.baseStoreService = baseStoreService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected AtpFormulaDao getAtpFormulaDao()
    {
        return this.atpFormulaDao;
    }


    @Required
    public void setAtpFormulaDao(AtpFormulaDao atpFormulaDao)
    {
        this.atpFormulaDao = atpFormulaDao;
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
