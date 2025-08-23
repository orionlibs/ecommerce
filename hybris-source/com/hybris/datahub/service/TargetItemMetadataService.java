package com.hybris.datahub.service;

import com.hybris.datahub.domain.TargetAttributeDefinition;
import com.hybris.datahub.domain.TargetItemMetadata;
import java.util.List;

public interface TargetItemMetadataService
{
    TargetItemMetadata findActiveTargetItemMetadata(String paramString1, String paramString2);


    TargetAttributeDefinition findAttributeDefinition(String paramString1, String paramString2, String paramString3);


    List<TargetItemMetadata> findTargetItemMetadata();
}
