package com.hybris.datahub.domain;

import java.util.Set;

public interface CanonicalItemMetadata extends ItemMetadata
{
    Set<CanonicalAttributeModelDefinition> getCanonicalAttributeDefinitions();


    @Deprecated(since = "ages", forRemoval = true)
    void setCanonicalAttributeDefinitions(Set<CanonicalAttributeModelDefinition> paramSet);


    void addCanonicalAttributeDefinition(CanonicalAttributeModelDefinition paramCanonicalAttributeModelDefinition);


    Set<TargetItemMetadata> getTargetItemTypes();


    @Deprecated(since = "ages", forRemoval = true)
    void setTargetItemTypes(Set<TargetItemMetadata> paramSet);


    void addTargetItemType(TargetItemMetadata paramTargetItemMetadata);


    String getDocumentId();


    void setDocumentId(String paramString);
}
