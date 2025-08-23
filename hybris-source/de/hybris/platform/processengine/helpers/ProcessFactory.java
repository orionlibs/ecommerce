package de.hybris.platform.processengine.helpers;

import java.util.Map;

public interface ProcessFactory
{
    <T extends de.hybris.platform.processengine.model.BusinessProcessModel> T createProcessModel(String paramString1, String paramString2, Map<String, Object> paramMap);
}
