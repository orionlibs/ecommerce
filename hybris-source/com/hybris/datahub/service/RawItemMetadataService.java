package com.hybris.datahub.service;

import com.hybris.datahub.domain.RawAttributeModelDefinition;
import com.hybris.datahub.domain.RawItemMetadata;
import java.util.List;

public interface RawItemMetadataService
{
    RawItemMetadata createNew();


    RawAttributeModelDefinition findAttributeDefinition(RawItemMetadata paramRawItemMetadata, String paramString);


    RawAttributeModelDefinition findAttributeDefinition(String paramString1, String paramString2);


    List<RawAttributeModelDefinition> findAttributeDefinitionsByRawType(String paramString);


    RawAttributeModelDefinition newAttributeDefinition();


    RawItemMetadata findRawItemMetadata(String paramString);


    List<RawItemMetadata> findRawItemMetadata();
}
