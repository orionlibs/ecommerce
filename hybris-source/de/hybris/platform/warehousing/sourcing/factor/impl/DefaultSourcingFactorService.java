package de.hybris.platform.warehousing.sourcing.factor.impl;

import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingFactor;
import de.hybris.platform.warehousing.data.sourcing.SourcingFactorIdentifiersEnum;
import de.hybris.platform.warehousing.model.SourcingConfigModel;
import de.hybris.platform.warehousing.sourcing.factor.SourcingFactorService;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class DefaultSourcingFactorService implements SourcingFactorService
{
    private static final int ONE_HUNDRED = 100;
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSourcingFactorService.class);


    public SourcingFactor getSourcingFactor(SourcingFactorIdentifiersEnum sourcingFactorId, BaseStoreModel baseStore)
    {
        ServicesUtil.validateParameterNotNull(baseStore, "BaseStore cannot be null");
        ServicesUtil.validateParameterNotNull(sourcingFactorId, "SourcingFactorId cannot be null");
        Map<SourcingFactorIdentifiersEnum, SourcingFactor> sourcingFactorsMapForBaseStore = getSourcingFactorsMapForBaseStore(baseStore);
        return sourcingFactorsMapForBaseStore.get(sourcingFactorId);
    }


    public Set<SourcingFactor> getAllSourcingFactorsForBaseStore(BaseStoreModel baseStore)
    {
        ServicesUtil.validateParameterNotNull(baseStore, "BaseStore cannot be null");
        Set<SourcingFactor> result = new HashSet<>();
        getSourcingFactorsMapForBaseStore(baseStore).forEach((sourcingFactorId, sourcingFactor) -> result.add(sourcingFactor));
        return result;
    }


    protected Map<SourcingFactorIdentifiersEnum, SourcingFactor> getSourcingFactorsMapForBaseStore(BaseStoreModel baseStore)
    {
        ServicesUtil.validateParameterNotNull(baseStore, "BaseStore cannot be null");
        Map<SourcingFactorIdentifiersEnum, SourcingFactor> sourcingFactorsMapForBaseStore = new HashMap<>();
        Arrays.<SourcingFactorIdentifiersEnum>stream(SourcingFactorIdentifiersEnum.values()).forEach(sourcingFactorId -> sourcingFactorsMapForBaseStore.put(sourcingFactorId, createSourcingFactorFromSourcingConfig(sourcingFactorId, baseStore.getSourcingConfig())));
        int totalWeightage = sourcingFactorsMapForBaseStore.keySet().stream().mapToInt(sourcingFactorId -> ((SourcingFactor)sourcingFactorsMapForBaseStore.get(sourcingFactorId)).getWeight()).sum();
        if(100 != totalWeightage)
        {
            throw new IllegalArgumentException("Factor weights are percentages, therefore the sum of the factor weights should equal 100.");
        }
        return sourcingFactorsMapForBaseStore;
    }


    protected SourcingFactor createSourcingFactorFromSourcingConfig(SourcingFactorIdentifiersEnum sourcingFactorId, SourcingConfigModel sourcingConfig)
    {
        ServicesUtil.validateParameterNotNull(sourcingFactorId, "SourcingFactorId cannot be null");
        ServicesUtil.validateParameterNotNull(sourcingConfig, "SourcingConfig cannot be null");
        SourcingFactor sourcingFactor = new SourcingFactor();
        sourcingFactor.setFactorId(sourcingFactorId);
        sourcingFactor.setWeight(loadFactorValue(sourcingFactorId, sourcingConfig));
        return sourcingFactor;
    }


    protected int loadFactorValue(SourcingFactorIdentifiersEnum sourcingFactorId, SourcingConfigModel sourcingConfig)
    {
        ServicesUtil.validateParameterNotNull(sourcingFactorId, "SourcingFactorId cannot be null");
        ServicesUtil.validateParameterNotNull(sourcingConfig, "SourcingConfig cannot be null");
        try
        {
            Optional<PropertyDescriptor> propertyDescriptorOptional = Arrays.<PropertyDescriptor>stream(Introspector.getBeanInfo(sourcingConfig.getClass()).getPropertyDescriptors())
                            .filter(propDescriptor -> (propDescriptor.getReadMethod() != null && propDescriptor.getName().toLowerCase().contains(sourcingFactorId.toString().toLowerCase()))).findFirst();
            if(propertyDescriptorOptional.isPresent())
            {
                Object result = ((PropertyDescriptor)propertyDescriptorOptional.get()).getReadMethod().invoke(sourcingConfig, new Object[0]);
                if(result instanceof Integer)
                {
                    int factorValue = ((Integer)result).intValue();
                    Assert.isTrue((factorValue >= 0), "Negative weight has been found in sourcing factor, please reset to positive.");
                    return factorValue;
                }
            }
        }
        catch(IntrospectionException | java.lang.reflect.InvocationTargetException | IllegalAccessException e)
        {
            LOGGER.error(String.format("Sourcing Config failed to interpret sourcing factor. Please make sure the SourcingConfig has appropriate weight defined for this sourcingFactor: [%s]. Returning 0 as weight for this sourcingFactor", new Object[] {sourcingFactorId}));
        }
        return 0;
    }
}
