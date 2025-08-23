package de.hybris.platform.couponservices.dao.impl;

import de.hybris.platform.couponservices.dao.CodeGenerationConfigurationDao;
import de.hybris.platform.couponservices.model.CodeGenerationConfigurationModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;

public class DefaultCodeGenerationConfigurationDao extends DefaultGenericDao<CodeGenerationConfigurationModel> implements CodeGenerationConfigurationDao
{
    public DefaultCodeGenerationConfigurationDao()
    {
        super("CodeGenerationConfiguration");
    }


    public Optional<CodeGenerationConfigurationModel> findCodeGenerationConfigurationByName(String name)
    {
        ServicesUtil.validateParameterNotNull(name, "String name cannot be null");
        Map<String, String> params = Collections.singletonMap("name", name);
        List<CodeGenerationConfigurationModel> configurationModels = find(params);
        return Optional.ofNullable(CollectionUtils.isNotEmpty(configurationModels) ? configurationModels.get(0) : null);
    }
}
