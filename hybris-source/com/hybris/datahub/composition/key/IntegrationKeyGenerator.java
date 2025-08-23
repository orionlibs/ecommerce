package com.hybris.datahub.composition.key;

import com.hybris.datahub.model.CanonicalItem;
import com.hybris.datahub.model.RawItem;
import java.util.Map;
import java.util.Set;

public interface IntegrationKeyGenerator
{
    String calculateIntegrationKey(String paramString, CanonicalItem paramCanonicalItem);


    String calculateIntegrationKeyFromRawItem(String paramString, RawItem paramRawItem);


    String calculateIntegrationKey(String paramString, Map<String, ?> paramMap);


    boolean validateKeyFields(String paramString, Set<String> paramSet);
}
