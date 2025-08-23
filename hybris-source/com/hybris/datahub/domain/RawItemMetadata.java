package com.hybris.datahub.domain;

import java.util.Optional;
import java.util.Set;

public interface RawItemMetadata extends ItemMetadata
{
    public static final String _TYPECODE = "RawItemMetadata";


    Set<RawAttributeModelDefinition> getRawAttributeDefinitions();


    @Deprecated(since = "ages", forRemoval = true)
    void setRawAttributeDefinitions(Set<RawAttributeModelDefinition> paramSet);


    void addRawAttributeDefinition(RawAttributeModelDefinition paramRawAttributeModelDefinition);


    Optional<RawAttributeModelDefinition> getRawAttributeDefinition(String paramString);
}
