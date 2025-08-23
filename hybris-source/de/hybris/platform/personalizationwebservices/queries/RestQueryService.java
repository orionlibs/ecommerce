package de.hybris.platform.personalizationwebservices.queries;

import java.util.Map;

public interface RestQueryService
{
    Object executeQuery(String paramString, Map<String, String> paramMap);
}
