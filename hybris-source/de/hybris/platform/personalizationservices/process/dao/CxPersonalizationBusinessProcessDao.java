package de.hybris.platform.personalizationservices.process.dao;

import de.hybris.platform.personalizationservices.model.process.CxPersonalizationProcessModel;
import java.util.List;

public interface CxPersonalizationBusinessProcessDao
{
    List<CxPersonalizationProcessModel> findActiveBusinessProcesses(String paramString1, String paramString2);
}
