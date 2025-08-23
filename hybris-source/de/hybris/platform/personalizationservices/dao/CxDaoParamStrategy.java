package de.hybris.platform.personalizationservices.dao;

import java.util.Map;

public interface CxDaoParamStrategy
{
    Map<String, Object> expandParams(Map<String, Object> paramMap, Map<String, String> paramMap1);
}
