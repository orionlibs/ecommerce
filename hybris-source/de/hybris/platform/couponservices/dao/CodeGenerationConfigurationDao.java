package de.hybris.platform.couponservices.dao;

import de.hybris.platform.couponservices.model.CodeGenerationConfigurationModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.Optional;

public interface CodeGenerationConfigurationDao extends Dao
{
    Optional<CodeGenerationConfigurationModel> findCodeGenerationConfigurationByName(String paramString);
}
