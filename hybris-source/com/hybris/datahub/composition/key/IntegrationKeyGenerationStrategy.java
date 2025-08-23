package com.hybris.datahub.composition.key;

import java.util.Map;

public interface IntegrationKeyGenerationStrategy
{
    String generateKeyValue(String paramString, Map<String, ?> paramMap);
}
